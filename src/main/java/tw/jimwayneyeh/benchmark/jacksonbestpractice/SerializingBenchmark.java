package tw.jimwayneyeh.benchmark.jacksonbestpractice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import tw.jimwayneyeh.benchmark.jacksonbestpractice.benchmark.ObjectMapperSerializingBenchmark;
import tw.jimwayneyeh.benchmark.jacksonbestpractice.benchmark.ObjectWriterSerializingBenchmark;
import tw.jimwayneyeh.benchmark.jacksonbestpractice.objects.SimpleObject;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class SerializingBenchmark<T> {
    private static final int DEFAULT_WARMUP_TIMES = 10;

    protected abstract Class getSerializingClass();

    public void benchmark(List<T> objects) throws Exception {
        ObjectMapperSerializingBenchmark<T> mapperBenchmark =
                new ObjectMapperSerializingBenchmark(new ObjectMapper(), objects);
        ObjectWriterSerializingBenchmark<T> writerBenchmark =
                new ObjectWriterSerializingBenchmark<>(new ObjectMapper(), objects, getSerializingClass());

        ObjectMapperSerializingBenchmark<T> afterburnerMapperBenchmark =
                new ObjectMapperSerializingBenchmark(new ObjectMapper().registerModule(new AfterburnerModule()), objects);
        ObjectWriterSerializingBenchmark<T> afterburnerWriterBenchmark =
                new ObjectWriterSerializingBenchmark<>(new ObjectMapper().registerModule(new AfterburnerModule()), objects, getSerializingClass());

        log.info("Warming up....");
        mapperBenchmark.warmUp(DEFAULT_WARMUP_TIMES);
        writerBenchmark.warmUp(DEFAULT_WARMUP_TIMES);
        afterburnerMapperBenchmark.warmUp(DEFAULT_WARMUP_TIMES);
        afterburnerWriterBenchmark.warmUp(DEFAULT_WARMUP_TIMES);

        log.info("Start benchmark between ObjectMapper and ObjectReader...");

        List<Pair<Duration, Duration>> results = new ArrayList<>(objects.size());
        for (int i=0; i<200; i++) {
            Duration durationOfMapper = mapperBenchmark.benchmark();
            Duration durationOfWriter = writerBenchmark.benchmark();

            log.info("Result #{} of ObjectMapper vs. ObjectWriter: {} vs. {}", i, durationOfMapper, durationOfWriter);
            results.add(Pair.of(durationOfMapper, durationOfWriter));
        }

        List<Pair<Duration, Duration>> afterburnerResults = new ArrayList<>(objects.size());
        for (int i=0; i<200; i++) {
            Duration durationOfMapper = afterburnerMapperBenchmark.benchmark();
            Duration durationOfWriter = afterburnerWriterBenchmark.benchmark();

            log.info("Result #{} of ObjectMapper vs. ObjectWriter (afterburner): {} vs. {}", i, durationOfMapper, durationOfWriter);
            afterburnerResults.add(Pair.of(durationOfMapper, durationOfWriter));
        }

        log.info("Average serializing time for ObjectMapper:\t{}ms",
                results.parallelStream().map(Pair::getLeft).mapToLong(Duration::toMillis).average());
        log.info("Average serializing time for ObjectWriter:\t{}ms",
                results.parallelStream().map(Pair::getRight).mapToLong(Duration::toMillis).average());
        log.info("Average serializing time for ObjectMapper (afterburner):\t{}ms",
                afterburnerResults.parallelStream().map(Pair::getLeft).mapToLong(Duration::toMillis).average());
        log.info("Average serializing time for ObjectWriter (afterburner):\t{}ms",
                afterburnerResults.parallelStream().map(Pair::getRight).mapToLong(Duration::toMillis).average());
    }
}

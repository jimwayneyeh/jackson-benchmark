package tw.jimwayneyeh.benchmark.jacksonbestpractice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import tw.jimwayneyeh.benchmark.jacksonbestpractice.benchmark.ObjectMapperDeserializingBenchmark;
import tw.jimwayneyeh.benchmark.jacksonbestpractice.benchmark.ObjectMapperSerializingBenchmark;
import tw.jimwayneyeh.benchmark.jacksonbestpractice.benchmark.ObjectReaderDeserializingBenchmark;
import tw.jimwayneyeh.benchmark.jacksonbestpractice.benchmark.ObjectWriterSerializingBenchmark;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class DeserializingBenchmark<T> {
    private static final int DEFAULT_WARMUP_TIMES = 10;

    protected abstract Class getDeserializingClass();

    public void benchmark(List<String> strings) throws Exception {
        ObjectMapperDeserializingBenchmark mapperBenchmark =
                new ObjectMapperDeserializingBenchmark(new ObjectMapper(), strings, getDeserializingClass());
        ObjectReaderDeserializingBenchmark readerBenchmark =
                new ObjectReaderDeserializingBenchmark(new ObjectMapper(), strings, getDeserializingClass());

        ObjectMapperDeserializingBenchmark afterburnerMapperBenchmark =
                new ObjectMapperDeserializingBenchmark(new ObjectMapper().registerModule(new AfterburnerModule()), strings, getDeserializingClass());
        ObjectReaderDeserializingBenchmark afterburnerReaderBenchmark =
                new ObjectReaderDeserializingBenchmark(new ObjectMapper().registerModule(new AfterburnerModule()), strings, getDeserializingClass());

        log.info("Warming up....");
        mapperBenchmark.warmUp(DEFAULT_WARMUP_TIMES);
        readerBenchmark.warmUp(DEFAULT_WARMUP_TIMES);
        afterburnerMapperBenchmark.warmUp(DEFAULT_WARMUP_TIMES);
        afterburnerReaderBenchmark.warmUp(DEFAULT_WARMUP_TIMES);

        log.info("Start benchmark between ObjectMapper and ObjectReader...");

        List<Pair<Duration, Duration>> results = new ArrayList<>(strings.size());
        for (int i=0; i<200; i++) {
            Duration durationOfMapper = mapperBenchmark.benchmark();
            Duration durationOfReader = readerBenchmark.benchmark();

            log.info("Result #{} of ObjectMapper vs. ObjectReader: {} vs. {}", i, durationOfMapper, durationOfReader);
            results.add(Pair.of(durationOfMapper, durationOfReader));
        }

        List<Pair<Duration, Duration>> afterburnerResults = new ArrayList<>(strings.size());
        for (int i=0; i<200; i++) {
            Duration durationOfMapper = afterburnerMapperBenchmark.benchmark();
            Duration durationOfReader = afterburnerReaderBenchmark.benchmark();

            log.info("Result #{} of ObjectMapper vs. ObjectReader (afterburner): {} vs. {}", i, durationOfMapper, durationOfReader);
            afterburnerResults.add(Pair.of(durationOfMapper, durationOfReader));
        }

        log.info("Average deserializing time for ObjectMapper:\t{}ms",
                results.parallelStream().map(Pair::getLeft).mapToLong(Duration::toMillis).average());
        log.info("Average deserializing time for ObjectReader:\t{}ms",
                results.parallelStream().map(Pair::getRight).mapToLong(Duration::toMillis).average());
        log.info("Average deserializing time for ObjectMapper (afterburner):\t{}ms",
                afterburnerResults.parallelStream().map(Pair::getLeft).mapToLong(Duration::toMillis).average());
        log.info("Average deserializing time for ObjectReader (afterburner):\t{}ms",
                afterburnerResults.parallelStream().map(Pair::getRight).mapToLong(Duration::toMillis).average());
    }
}

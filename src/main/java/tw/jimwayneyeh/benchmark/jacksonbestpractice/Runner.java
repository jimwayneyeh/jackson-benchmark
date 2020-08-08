package tw.jimwayneyeh.benchmark.jacksonbestpractice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tw.jimwayneyeh.benchmark.jacksonbestpractice.benchmark.ObjectMapperSerializingBenchmark;
import tw.jimwayneyeh.benchmark.jacksonbestpractice.benchmark.ObjectWriterSerializingBenchmark;
import tw.jimwayneyeh.benchmark.jacksonbestpractice.objects.SimpleObject;
import tw.jimwayneyeh.benchmark.jacksonbestpractice.utilities.TestDataGenerator;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Slf4j
public class Runner implements CommandLineRunner {

    private static final int DEFAULT_WARMUP_TIMES = 10;
    @Override
    public void run(String... args) throws Exception {
        log.info("Generating test object...");
        TestDataGenerator<SimpleObject> simpleObjectGenerator = new TestDataGenerator<>();
        List<SimpleObject> simpleObjects = IntStream.range(0, 100_000)
                .mapToObj(count -> simpleObjectGenerator.generateLargeJson(new SimpleObject()))
                .collect(Collectors.toList());
        log.info("{} SimpleObjects are generated.", simpleObjects.size());

        benchmarkSimpleObject(simpleObjects);
    }

    public void benchmarkSimpleObject(List<SimpleObject> simpleObjects) throws Exception {
        ObjectMapperSerializingBenchmark<SimpleObject> mapperBenchmark =
                new ObjectMapperSerializingBenchmark(new ObjectMapper(), simpleObjects);
        ObjectWriterSerializingBenchmark<SimpleObject> writerBenchmark =
                new ObjectWriterSerializingBenchmark<>(new ObjectMapper(), simpleObjects, SimpleObject.class);

        ObjectMapperSerializingBenchmark<SimpleObject> afterburnerMapperBenchmark =
                new ObjectMapperSerializingBenchmark(new ObjectMapper().registerModule(new AfterburnerModule()), simpleObjects);
        ObjectWriterSerializingBenchmark<SimpleObject> afterburnerWriterBenchmark =
                new ObjectWriterSerializingBenchmark<>(new ObjectMapper().registerModule(new AfterburnerModule()), simpleObjects, SimpleObject.class);

        log.info("Warming up....");
        mapperBenchmark.warmUp(DEFAULT_WARMUP_TIMES);
        writerBenchmark.warmUp(DEFAULT_WARMUP_TIMES);
        afterburnerMapperBenchmark.warmUp(DEFAULT_WARMUP_TIMES);
        afterburnerWriterBenchmark.warmUp(DEFAULT_WARMUP_TIMES);

        log.info("Start benchmark between ObjectMapper and ObjectReader...");

        List<Pair<Duration, Duration>> results = new ArrayList<>(simpleObjects.size());
        for (int i=0; i<200; i++) {
            Duration durationOfMapper = mapperBenchmark.benchmark();
            Duration durationOfWriter = writerBenchmark.benchmark();

            log.info("Result #{} of ObjectMapper vs. ObjectWriter: {} vs. {}", i, durationOfMapper, durationOfWriter);
            results.add(Pair.of(durationOfMapper, durationOfWriter));
        }

        List<Pair<Duration, Duration>> afterburnerResults = new ArrayList<>(simpleObjects.size());
        for (int i=0; i<200; i++) {
            Duration durationOfMapper = afterburnerMapperBenchmark.benchmark();
            Duration durationOfWriter = afterburnerWriterBenchmark.benchmark();

            log.info("Result #{} of ObjectMapper vs. ObjectWriter (afterburner): {} vs. {}", i, durationOfMapper, durationOfWriter);
            afterburnerResults.add(Pair.of(durationOfMapper, durationOfWriter));
        }

        log.info("Average serializing time for ObjectMapper:\t{}ms",
                results.parallelStream().map(pair -> pair.getLeft()).mapToLong(Duration::toMillis).average());
        log.info("Average serializing time for ObjectWriter:\t{}ms",
                results.parallelStream().map(pair -> pair.getRight()).mapToLong(Duration::toMillis).average());
        log.info("Average serializing time for ObjectMapper (afterburner):\t{}ms",
                afterburnerResults.parallelStream().map(pair -> pair.getLeft()).mapToLong(Duration::toMillis).average());
        log.info("Average serializing time for ObjectWriter (afterburner):\t{}ms",
                afterburnerResults.parallelStream().map(pair -> pair.getRight()).mapToLong(Duration::toMillis).average());
    }
}

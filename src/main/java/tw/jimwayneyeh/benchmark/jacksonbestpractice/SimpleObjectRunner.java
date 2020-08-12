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
public class SimpleObjectRunner extends SerializingBenchmark<SimpleObject> implements CommandLineRunner {

    private static final int DEFAULT_WARMUP_TIMES = 10;
    @Override
    public void run(String... args) throws Exception {
        log.info("Generating test object...");
        TestDataGenerator<SimpleObject> simpleObjectGenerator = new TestDataGenerator<>();
        List<SimpleObject> simpleObjects = IntStream.range(0, 100_000)
                .mapToObj(count -> simpleObjectGenerator.generateLargeJson(new SimpleObject()))
                .collect(Collectors.toList());
        log.info("{} SimpleObjects are generated.", simpleObjects.size());

        benchmark(simpleObjects);
    }

    @Override
    protected Class getSerializingClass() {
        return SimpleObject.class;
    }
}

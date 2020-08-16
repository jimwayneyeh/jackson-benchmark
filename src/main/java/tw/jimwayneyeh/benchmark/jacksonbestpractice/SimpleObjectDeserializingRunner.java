package tw.jimwayneyeh.benchmark.jacksonbestpractice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tw.jimwayneyeh.benchmark.jacksonbestpractice.objects.SimpleObject;
import tw.jimwayneyeh.benchmark.jacksonbestpractice.utilities.TestDataGenerator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Slf4j
public class SimpleObjectDeserializingRunner extends DeserializingBenchmark<SimpleObject> implements CommandLineRunner {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int DEFAULT_WARMUP_TIMES = 10;
    @Override
    public void run(String... args) throws Exception {
        log.info("Generating test object...");
        TestDataGenerator<SimpleObject> simpleObjectGenerator = new TestDataGenerator<>();
        List<String> strings = IntStream.range(0, 100_000)
                .mapToObj(count -> simpleObjectGenerator.generateLargeJson(new SimpleObject()))
                .map(obj -> {
                    try {
                        return OBJECT_MAPPER.writeValueAsString(obj);
                    } catch (Throwable t) {
                        log.error("Cannot map object to string: {}", t.getMessage());
                        return StringUtils.EMPTY;
                    }
                })
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        log.info("{} SimpleObjects are generated.", strings.size());

        benchmark(strings);
    }

    @Override
    protected Class getDeserializingClass() {
        return SimpleObject.class;
    }
}

package tw.jimwayneyeh.benchmark.jacksonbestpractice.benchmark;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Accessors(fluent = true)
public class ObjectMapperDeserializingBenchmark extends Benchmark<Long> {
    private final ObjectMapper objectMapper;
    private final List<String> strings;
    private final Class clazz;

    public ObjectMapperDeserializingBenchmark(ObjectMapper objectMapper, List<String> strings, Class clazz) {
        this.objectMapper = objectMapper;
        this.strings = strings;
        this.clazz = clazz;
    }

    @Override
    protected Long execute() throws Exception {
        long totalCount = strings.size();
        for (String str : strings) {
            objectMapper.readValue(str, clazz);
        }
        return totalCount;
    }
}

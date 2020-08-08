package tw.jimwayneyeh.benchmark.jacksonbestpractice.benchmark;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Accessors(fluent = true)
public class ObjectMapperSerializingBenchmark<T> extends Benchmark<Long> {
    private final ObjectMapper objectMapper;
    private final List<T> objects;

    public ObjectMapperSerializingBenchmark(ObjectMapper objectMapper, List<T> objects) {
        this.objectMapper = objectMapper;
        this.objects = objects;
    }

    @Override
    protected Long execute() throws Exception {
        long totalLength = 0;
        for (T obj : objects) {
            totalLength += objectMapper.writeValueAsString(obj).length();
        }
        return totalLength;
    }
}

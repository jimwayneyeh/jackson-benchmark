package tw.jimwayneyeh.benchmark.jacksonbestpractice.benchmark;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Accessors(fluent = true)
public class ObjectWriterSerializingBenchmark<T> extends Benchmark<Long> {
    private final ObjectWriter objectWriter;
    private final List<T> objects;

    public ObjectWriterSerializingBenchmark(ObjectMapper objectMapper, List<T> objects, Class clazz) {
        this.objectWriter = objectMapper.writerFor(clazz);
        this.objects = objects;
    }

    @Override
    protected Long execute() throws Exception {
        long totalLength = 0;
        for (T obj : objects) {
            totalLength += objectWriter.writeValueAsString(obj).length();
        }
        return totalLength;
    }
}

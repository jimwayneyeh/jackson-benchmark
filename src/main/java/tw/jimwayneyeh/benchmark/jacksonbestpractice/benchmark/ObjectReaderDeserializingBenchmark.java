package tw.jimwayneyeh.benchmark.jacksonbestpractice.benchmark;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Accessors(fluent = true)
public class ObjectReaderDeserializingBenchmark extends Benchmark<Long> {
    private final ObjectReader objectReader;
    private final List<String> strings;

    public ObjectReaderDeserializingBenchmark(ObjectMapper objectMapper, List<String> strings, Class clazz) {
        this.objectReader = objectMapper.readerFor(clazz);
        this.strings = strings;
    }

    @Override
    protected Long execute() throws Exception {
        long totalLength = strings.size();
        for (String str : strings) {
            objectReader.readValue(str);
        }
        return totalLength;
    }
}

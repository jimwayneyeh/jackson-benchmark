package tw.jimwayneyeh.benchmark.jacksonbestpractice.benchmark;

import java.time.Duration;
import java.time.Instant;

public abstract class Benchmark<T> {
    public void warmUp(int times) throws Exception {
        for (int i=0; i<times; i++) {
            execute();
        }
    }

    public Duration benchmark() throws Exception {
        Instant start = Instant.now();
        execute();
        return Duration.between(start, Instant.now());
    }

    protected abstract T execute() throws Exception;
}

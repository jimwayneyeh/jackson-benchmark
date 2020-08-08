package tw.jimwayneyeh.benchmark.jacksonbestpractice.utilities;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.Arrays;

@Slf4j
@Setter
@Accessors(fluent = true)
public class TestDataGenerator<T> {
    private final SecureRandom random = new SecureRandom();
    // Means that the generated string will be length between 400 characters and 600 characters.
    private int baseLengthOfString = 400;
    private int variatyLengthOfString = 200;

    public T generateLargeJson(T obj) {
        long numOfMethods = Arrays.stream(obj.getClass().getMethods())
                .filter(method -> method.getName().startsWith("set"))
                .map(method -> injectRandomValue(obj, method))
                .count();
        //log.info("{} values are injected to {}: {}", numOfMethods, obj.getClass());
        return obj;
    }

    private Method injectRandomValue(Object obj, Method method) {
        try {
            Class<?> parameterType = method.getParameterTypes()[0];

            if (method.getParameterTypes()[0].isAssignableFrom(String.class)) {
                method.invoke(obj, RandomStringUtils.random(
                        baseLengthOfString + random.nextInt(variatyLengthOfString), true, true));
            } else if (method.getParameterTypes()[0].isAssignableFrom(Long.class)) {
                method.invoke(obj, random.nextLong());
            } else if (method.getParameterTypes()[0].isAssignableFrom(Integer.class)) {
                method.invoke(obj, random.nextInt());
            } else if (method.getParameterTypes()[0].isAssignableFrom(Double.class)) {
                method.invoke(obj, random.nextDouble());
            } else if (method.getParameterTypes()[0].isAssignableFrom(Boolean.class)) {
                method.invoke(obj, random.nextBoolean());
            }
        } catch (Throwable t) {
            log.error("Cannot inject value.", t);
        }
        return method;
    }
}

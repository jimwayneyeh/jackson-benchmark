This is a simple benchmark project for testing the performance of serializing and deserializing using Jackson.

### Test Data

| Generated Data          |
| Primitive Wrapper Types | String, Integer, Long, Float, Double |
| Primitive Types         | String, int, long, float, double     |

### Result of Benchmark

The result shows here is not a strict environment. I just run the test on my own machine with IDEA. So the results should be used for reference only. However, you should be able to run your own test results if you're interesting in it.

| Object Contents         | ObjectMapper | ObjectWriter | ObjectMapper with afterburner | ObjectWriter with afterburner |
| ----------------------- | ------------ | ------------ | ----------------------------- | ----------------------------- |
| Primitive Wrapper Types | 695.84ms     | 692.435ms    | 644.99ms                      | 638.955ms                     |
| Primitive Types         | 457.195ms    | 452.015ms    | 423.02ms                      | 414.585ms                     |

package at.willhaben;


import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiter;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Resilience4jWrapper {

    private final Callable<Integer> callable;
    private final CircuitBreaker circuitBreaker;
    private final TimeLimiter timeLimiter;

    public Resilience4jWrapper(String param)  {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(20)
                .ringBufferSizeInClosedState(20)
                .waitDurationInOpenState(Duration.ofSeconds(10)).build();
        timeLimiter = TimeLimiter.of(Duration.ofMillis(500));
        Callable<Integer> timeRestricted =
                TimeLimiter.decorateFutureSupplier(timeLimiter, () -> executorService.submit(() -> UnstableApi.call(param)));
        circuitBreaker = CircuitBreaker.of("test", circuitBreakerConfig);
        callable = CircuitBreaker.decorateCallable(circuitBreaker, timeRestricted);
    }

    public Integer run() {
        try {
            return callable.call();
        } catch (Exception e) {
            System.out.println(circuitBreaker.getMetrics());
            return -1;
        }
    }
}

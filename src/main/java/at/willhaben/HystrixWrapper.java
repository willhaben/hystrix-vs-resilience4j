package at.willhaben;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

public class HystrixWrapper extends HystrixCommand<Integer> {

    private final String param;

    public HystrixWrapper(String param) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(HystrixWrapper.class.getName()))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(500)
                        .withCircuitBreakerErrorThresholdPercentage(20)));
        this.param = param;
    }

    protected Integer run() throws Exception {
        return UnstableApi.call(param);
    }

    protected Integer getFallback() {
        System.out.println(this.circuitBreaker.isOpen() ? "OPEN" : "CLOSED");
        return 1;
    }
}

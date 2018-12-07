package at.willhaben;

public class UnstableApi {

    public static int call(String someParameter) throws Exception {
        long timeout = (long) (Math.random() * 1000);
        if (timeout < 100) throw new RuntimeException();
        Thread.sleep(timeout);
        return someParameter.length();
    }

}

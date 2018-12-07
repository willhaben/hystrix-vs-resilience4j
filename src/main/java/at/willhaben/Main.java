package at.willhaben;

public class Main {

    public static void main(String args[]) throws Exception {

        Resilience4jWrapper test = new Resilience4jWrapper("test");

        while (true) {
            System.out.println(new HystrixWrapper("test").execute());
            System.out.println(test.run());
        }

    }
}

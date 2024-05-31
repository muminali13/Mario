package util;

public class Time {

    public static long timeStarted = System.nanoTime();

    public static float getTime() {
        return (float) (System.nanoTime() - timeStarted)/1_000_000_000.0f;
    }
}

package bsuir.slizh.cos1.signal;

public final class Waves {

    public static double sinWave(double A, double f, double n, double N) {
        return A * Math.sin(2.0 * Math.PI * f * n / N);
    }

    public static double squareWave(double A, double f, double n, double N, double dc) {
        return ((2.0 * Math.PI * f * n / N) % (2 * Math.PI)) / (2 * Math.PI) < dc ? A : -A;
    }

    public static double triangleWave(double A, double f, double n, double N) {
        return 2 * A / Math.PI / Math.sin(Math.sin(2 * Math.PI * f * n / N));
    }

    public static double sawtoothWave(double A, double f, double n, double N) {
        return -2 * A / Math.PI / Math.tan(1 / Math.tan(Math.PI * f * n / N));
    }

    public static double randomWave(double A) {
        return A * Math.random() * 2 - 1;
    }
}

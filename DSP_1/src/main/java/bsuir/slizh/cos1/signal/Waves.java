package bsuir.slizh.cos1.signal;

public final class Waves {

    public static double sinWave(double A, double f, double u0, double n, double N) {
        return A * Math.sin(2.0 * Math.PI * f * n / N + u0);
    }

    public static double squareWave(double A, double f, double u0, double n, double N, double dc) {
        return ((2.0 * Math.PI * f * n / N + u0) % (2 * Math.PI)) / (2 * Math.PI) < dc ? A : -A;
    }

    public static double triangleWave(double A, double f, double u0, double n, double N) {
        //return 2 * A / Math.PI / Math.sin(Math.sin(2 * Math.PI * f * n / N + u0));
        return A * (4.0 * Math.abs((2 * Math.PI * f * n / N + u0) / (2 * Math.PI) - 0.25 - ((int) ((2 * Math.PI * f * n / N + u0) / (2 * Math.PI) - 0.25)) - 0.5) - 1);
    }

    public static double sawtoothWave(double A, double f, double u0, double n, double N) {
        //return -2 * A / Math.PI / Math.tan(1 / Math.tan(Math.PI * f * n / N + u0));
        return A * (2 * ((2 * Math.PI * f * n / N + u0) / (2 * Math.PI) - 0.5 - ((int) ((2 * Math.PI * f * n / N + u0) / (2 * Math.PI) - 0.5))) - 1);
    }

    public static double randomWave(double A) {
        return A * Math.random() * 2 - 1;
    }
}

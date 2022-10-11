package bsuir.slizh.cos1.signal;

public final class Modulation {

    public static double amplitudeModulation(double um, double uc, double Am, double m) {
        return uc * (1.0 + m * um / Am);
    }
}

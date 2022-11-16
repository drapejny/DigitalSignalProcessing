package by.slizh.dft;

import org.knowm.xchart.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {
    // x(i) = 100cos(2pi*20i/N - pi/4)
    // значения для выбора апплитуд {2,3,5,9,10,12,15}
    // значения для выбора нач. фаз pi / 6,4,3,2, 3pi/4, pi

    private static double[] cosTable;
    private static int N = 256;

    private static double[] As = {2.0, 3.0, 5.0, 9.0, 10.0, 12.0, 15.0};
    private static double[] fis = {Math.PI / 6, Math.PI / 5, Math.PI / 6, Math.PI / 8, Math.PI / 10, Math.PI / 12};

    public static void main(String[] args) throws IOException {


        cosTable = new double[N];

        for (int i = 0; i < N; i++) {
            cosTable[i] = Math.cos(2 * Math.PI * i / N);
        }

        //------------------------------------ 1 ------------------------------------

        double[] x = generateSignal();

        double[] Ac = new double[N];
        double[] As = new double[N];

        double[] A = new double[N / 2];
        double[] fi = new double[N / 2];

        for (int j = 0; j < N / 2; j++) {
            double sumC = 0.0f;
            double sumS = 0.0f;
            for (int i = 0; i < N; i++) {
                sumC += x[i] * getCos(j * i);
                sumS += x[i] * getSin(j * i);
            }
            Ac[j] = 2.0 / N * sumC;
            As[j] = 2.0 / N * sumS;
            A[j] = Math.sqrt(Math.pow(Ac[j], 2) + Math.pow(As[j], 2));
            fi[j] = Math.atan(As[j] / Ac[j]);
        }

        double[] regeneratedSignal = new double[N];

        for (int i = 0; i < N; i++) {
            double sum = 0.0;
            for (int j = 0; j < N / 2; j++) {
                sum += A[j] * Math.cos(2 * Math.PI * j * i / N - fi[j]);
            }
            regeneratedSignal[i] = sum;
        }

        double[] xData = new double[N];
        double[] yData;

        // Just fill x values
        for (int i = 0; i < N; i++) {
            xData[i] = i;
        }

        // Generated signal chart
        double[][] yyData = new double[][]{x, regeneratedSignal};
        String[] names = {"1. Generated signal", "1. Regenerated signal"};
        XYChart chart = QuickChart.getChart("1.", "X", "Y", names, xData, yyData);
        new SwingWrapper(chart).displayChart();


        //------------------------------------ 2 ------------------------------------

        Ac = new double[N];
        As = new double[N];

        A = new double[N / 2];
        fi = new double[N / 2];

        double[] polyharmonicSignal = generatePolyharmonicSignal(N);

        for (int j = 0; j < N / 2; j++) {
            double sumC = 0.0f;
            double sumS = 0.0f;
            for (int i = 0; i < N; i++) {
                sumC += polyharmonicSignal[i] * getCos(j * i);
                sumS += polyharmonicSignal[i] * getSin(j * i);
            }
            Ac[j] = 2.0 / N * sumC;
            As[j] = 2.0 / N * sumS;
            A[j] = Math.sqrt(Math.pow(Ac[j], 2) + Math.pow(As[j], 2));
            fi[j] = Math.atan(As[j] / Ac[j]);
        }

        regeneratedSignal = new double[N];

        for (int i = 0; i < N; i++) {
            double sum = 0.0;
            for (int j = 0; j < N / 2; j++) {
                sum += A[j] * Math.cos(2 * Math.PI * j * i / N - fi[j]);
            }
            regeneratedSignal[i] = sum;
        }

        double[] regeneratedSignalWithoutFi = new double[N];

        for (int i = 0; i < N; i++) {
            double sum = 0.0;
            for (int j = 0; j < N / 2; j++) {
                sum += A[j] * Math.cos(2 * Math.PI * j * i / N);
            }
            regeneratedSignalWithoutFi[i] = sum;
        }

        yyData = new double[][]{polyharmonicSignal, regeneratedSignal, regeneratedSignalWithoutFi};
        names = new String[]{"2. Polyharmonic signal", "2. Regenerated polyharmonic signal", "2. Regenerated polyharmonic signal without fi"};
        chart = QuickChart.getChart("2.", "X", "Y", names, xData, yyData);
        new SwingWrapper(chart).displayChart();

        xData = new double[N / 2];

        // Just fill x values
        for (int i = 0; i < N / 2; i++) {
            xData[i] = i;
        }

        yData = A;
        chart = QuickChart.getChart("2.", "j", "A", "Amplitude spectre", xData, yData);
        new SwingWrapper(chart).displayChart();

        yData = fi;
        chart = QuickChart.getChart("2.", "j", "fi", "Phase spectre", xData, yData);
        new SwingWrapper(chart).displayChart();


        //------------------------------------ 3 ------------------------------------

        // Fast Fourier
        polyharmonicSignal = generatePolyharmonicSignal(N);
        DoublePair[] coefs = fourierParams(polyharmonicSignal);
        A = new double[N];
        fi = new double[N];

        for (int i = 0; i < coefs.length; i++) {
            A[i] = coefs[i].item1;
            if (Math.abs(A[i]) > 0.0001) {
                fi[i] = coefs[i].item2;
            } else {
                fi[i] = 0;
            }
        }


        regeneratedSignal = new double[N];

        for (int i = 0; i < N; i++) {
            double sum = 0.0;
            for (int j = 0; j < N / 2; j++) {
                sum += A[j] * Math.cos(2 * Math.PI * j * i / N - fi[j]);
            }
            regeneratedSignal[i] = sum;
        }

        xData = new double[N];

        // Just fill x values
        for (int i = 0; i < N; i++) {
            xData[i] = i;
        }

        yyData = new double[][]{polyharmonicSignal, regeneratedSignal};
        names = new String[]{"3. Polyharmonic signal", "3. Regenerated polyharmonic signal"};
        chart = QuickChart.getChart("3.", "X", "Y", names, xData, yyData);
        new SwingWrapper(chart).displayChart();

        double[] LF = new double[N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j <= 10; j++) {
                LF[i] += A[j] * Math.cos(2 * Math.PI * j * i / N - fi[j]);
            }
        }

        double[] MF = new double[N];

        for (int i = 0; i < N; i++) {
            for (int j = 11; j < 20; j++) {
                MF[i] += A[j] * Math.cos(2 * Math.PI * j * i / N - fi[j]);
            }
        }

        double[] HF = new double[N];

        for (int i = 0; i < N; i++) {
            for (int j = 20; j <= 30; j++) {
                HF[i] += A[j] * Math.cos(2 * Math.PI * j * i / N - fi[j]);
            }
        }

        yyData = new double[][]{polyharmonicSignal, LF, MF, HF};
        names = new String[]{"4. Polyharmonic signal", "4. LF", "4. MF", "4. HF"};
        chart = QuickChart.getChart("4.", "X", "Y", names, xData, yyData);
        new SwingWrapper(chart).displayChart();


    }


    private static DoublePair[] fourierParams(double[] data) {
        Complex[] complexes = getComplexes(data);
        Complex[] cRes = fourierParams(complexes);
        DoublePair[] result = new DoublePair[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = new DoublePair(magnitudeComplex(cRes[i]) * 2 / data.length, -phaseComplex(cRes[i]));
        }
        return result;
    }

    private static Complex[] fourierParams(Complex[] x) {
        int L = x.length;
        if (L == 2) {
            return new Complex[]{sumComplex(x[0], x[1]), substractComplex(x[0], x[1])};
        }
        Complex[] g0 = new Complex[L / 2];
        Complex[] g1 = new Complex[L / 2];
        for (int i = 0; i < L / 2; i++) {
            g0[i] = x[2 * i];
            g1[i] = x[2 * i + 1];
        }
        Complex[] g0Params = fourierParams(g0);
        Complex[] g1Params = fourierParams(g1);
        Complex[] result = new Complex[L];
        for (int i = 0; i < L / 2; i++) {
            double arg = -2 * Math.PI * i / L;
            Complex coef = new Complex(getCos(i * (N / L)), -getSin(i * (N / L)));
            result[i] = sumComplex(g0Params[i], multiplyComplex(coef, g1Params[i]));
            result[i + L / 2] = substractComplex(g0Params[i], multiplyComplex(coef, g1Params[i]));
        }
        return result;
    }

    private static Complex[] getComplexes(double[] doubles) {
        Complex[] complexes = new Complex[doubles.length];
        for (int i = 0; i < doubles.length; i++) {
            complexes[i] = new Complex(doubles[i], 0);
        }
        return complexes;
    }

    private static double phaseComplex(Complex c) {
        return Math.atan2(c.i, c.r);
    }

    private static double magnitudeComplex(Complex c) {
        return Math.sqrt(Math.pow(c.r, 2) + Math.pow(c.i, 2));
    }

    private static Complex sumComplex(Complex c1, Complex c2) {
        return new Complex(c1.r + c2.r, c1.i + c2.i);
    }

    private static Complex substractComplex(Complex c1, Complex c2) {
        return new Complex(c1.r - c2.r, c1.i - c2.i);
    }

    private static Complex multiplyComplex(Complex c1, Complex c2) {
        double r = c1.r * c2.r - c1.i * c2.i;
        double i = c1.r * c2.i + c1.i * c2.r;
        return new Complex(r, i);
    }

    private static double[] generateSignal() {
        double[] signal = new double[N];
        for (int i = 0; i < N; i++) {
            signal[i] = 10 * Math.cos(2 * Math.PI * i / N + Math.PI / 3);
        }
        return signal;
    }

    private static double[] generatePolyharmonicSignal(int n) {
        double[] signal = new double[n];
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            double A = As[Math.abs(random.nextInt()) % As.length];
            double fi = fis[Math.abs(random.nextInt()) % fis.length];
            for (int j = 0; j < n; j++) {
                signal[j] += A * Math.cos(2 * Math.PI * j * i / n - fi);
            }
        }
        return signal;
    }

    private static double getCos(int i) {
        i %= N;
        return cosTable[i];
    }

    private static double getSin(int i) {
        i %= N;
        int t = (i - N / 4 + N) % N;
        return cosTable[t];
    }
}
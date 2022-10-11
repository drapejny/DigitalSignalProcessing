package bsuir.slizh.cos1;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

import static bsuir.slizh.cos1.signal.Waves.sinWave;
import static bsuir.slizh.cos1.signal.Waves.squareWave;

public class Main {

    private static int sampleRate = 44100;        // Samples per second
    private static double duration = 5.0;        // Seconds
    private static int validBits = 16;
    private static int numChannels = 1;
    private static String filePath = "example.wav";

    private static Scanner scanner;

    public static void main(String[] args) throws IOException, WavFileException {
        scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);
        System.out.println("1. Генерация несущего сигнала\n" +
                "2. Генераций полигармонических сигналов\n" +
                "3. Модуляция параметров");
        String s = scanner.nextLine();
        switch (s) {
            case "1" -> uiSignalGeneration();
            case "2" -> uiPolysignalGeneration();
            case "3" -> uiModulation();
        }
    }

    private static void uiSignalGeneration() throws IOException, WavFileException {

        // Calculate the number of frames required for specified duration
        long numFrames = (long) (duration * sampleRate);

        // Create a wav file with the name specified as the first argument
        WavFile wavFile = WavFile.newWavFile(new File(filePath), numChannels, numFrames, validBits, sampleRate);

        // Create a buffer of 100 frames
        double[][] buffer = new double[1][100];

        double f = 128;
        double A = 1;
        double dc = 0.5;

        System.out.println("1. Синусоида\n" +
                "2. Импульс\n" +
                "3. Треугольная\n" +
                "4. Пила\n" +
                "5. Шум");
        String signal = scanner.nextLine();

        switch (signal) {
            case "1", "3", "4" -> {
                System.out.println("Введите: A, f");
                A = scanner.nextDouble();
                f = scanner.nextInt();
            }
            case "2" -> {
                System.out.println("Введите: A, f, dc");
                A = scanner.nextDouble();
                f = scanner.nextInt();
                dc = scanner.nextDouble();
            }
            case "5" -> {
                System.out.println("Введите: A");
                A = scanner.nextDouble();
            }
        }

        // Initialise a local frame counter
        long frameCounter = 0;

        // Loop until all frames written
        while (frameCounter < numFrames) {
            // Determine how many frames to write, up to a maximum of the buffer size
            long remaining = wavFile.getFramesRemaining();
            int toWrite = (remaining > 100) ? 100 : (int) remaining;

            // Fill the buffer, one tone per channel
            for (int s = 0; s < toWrite; s++, frameCounter++) {
                switch (signal) {
                    case "1" -> buffer[0][s] = sinWave(A, f, frameCounter, sampleRate);
                    case "2" -> buffer[0][s] = squareWave(A, f, frameCounter, sampleRate, dc);
                    case "3" ->
                            buffer[0][s] = 2 * A / Math.PI / Math.sin(Math.sin(2 * Math.PI * f * frameCounter / sampleRate));
                    case "4" ->
                            buffer[0][s] = -2 * A / Math.PI / Math.tan(1 / Math.tan(Math.PI * f * frameCounter / sampleRate));
                    case "5" -> buffer[0][s] = A * Math.random() * 2 - 1;
                }
            }

            // Write the buffer
            wavFile.writeFrames(buffer, toWrite);
        }
        // Close the wavFile
        wavFile.close();
    }


    private static void uiPolysignalGeneration() throws IOException, WavFileException {

        // Calculate the number of frames required for specified duration
        long numFrames = (long) (duration * sampleRate);

        // Create a wav file with the name specified as the first argument
        WavFile wavFile = WavFile.newWavFile(new File(filePath), numChannels, numFrames, validBits, sampleRate);

        // Create a buffer of 100 frames
        double[][] buffer = new double[1][100];

        double f1 = 128;
        double A1 = 1;
        double dc1 = 0.5;

        double f2 = 128;
        double A2 = 1;
        double dc2 = 0.5;

        System.out.println("Первый сигнал:\n" +
                "1. Синусоида\n" +
                "2. Импульс\n" +
                "3. Треугольная\n" +
                "4. Пила\n" +
                "5. Шум");
        String signal1 = scanner.nextLine();

        switch (signal1) {
            case "1", "3", "4" -> {
                System.out.println("Введите: A, f");
                A1 = scanner.nextDouble();
                f1 = scanner.nextInt();
            }
            case "2" -> {
                System.out.println("Введите: A, f, dc");
                A1 = scanner.nextDouble();
                f1 = scanner.nextInt();
                dc1 = scanner.nextDouble();
            }
            case "5" -> {
                System.out.println("Введите: A");
                A1 = scanner.nextDouble();
            }
        }

        System.out.println("Второй сигнал:\n" +
                "1. Синусоида\n" +
                "2. Импульс\n" +
                "3. Треугольная\n" +
                "4. Пила\n" +
                "5. Шум");
        String signal2 = scanner.nextLine();
        signal2 = scanner.nextLine();// говносканер читает пустую строку, поэтому пусть читает дважды
        System.out.println(signal2);

        switch (signal2) {
            case "1", "3", "4" -> {
                System.out.println("Введите: A, f");
                A2 = scanner.nextDouble();
                f2 = scanner.nextInt();
            }
            case "2" -> {
                System.out.println("Введите: A, f, dc");
                A2 = scanner.nextDouble();
                f2 = scanner.nextInt();
                dc2 = scanner.nextDouble();
            }
            case "5" -> {
                System.out.println("Введите: A");
                A2 = scanner.nextDouble();
            }
        }

        // Initialise a local frame counter
        long frameCounter = 0;

        // Loop until all frames written
        while (frameCounter < numFrames) {
            // Determine how many frames to write, up to a maximum of the buffer size
            long remaining = wavFile.getFramesRemaining();
            int toWrite = (remaining > 100) ? 100 : (int) remaining;

            // Fill the buffer, one tone per channel
            for (int s = 0; s < toWrite; s++, frameCounter++) {
                switch (signal1) {
                    case "1" -> buffer[0][s] = sinWave(A1, f1, frameCounter, sampleRate);
                    case "2" -> buffer[0][s] = squareWave(A1, f1, frameCounter, sampleRate, dc1);
                    case "3" ->
                            buffer[0][s] = 2 * A1 / Math.PI / Math.sin(Math.sin(2 * Math.PI * f1 * frameCounter / sampleRate));
                    case "4" ->
                            buffer[0][s] = -2 * A1 / Math.PI / Math.tan(1 / Math.tan(Math.PI * f1 * frameCounter / sampleRate));
                    case "5" -> buffer[0][s] = A1 * Math.random() * 2 - 1;
                }
                switch (signal2) {
                    case "1" -> buffer[0][s] += sinWave(A2, f2, frameCounter, sampleRate);
                    case "2" -> buffer[0][s] += squareWave(A2, f2, frameCounter, sampleRate, dc2);
                    case "3" ->
                            buffer[0][s] += 2 * A2 / Math.PI / Math.sin(Math.sin(2 * Math.PI * f2 * frameCounter / sampleRate));
                    case "4" ->
                            buffer[0][s] += -2 * A2 / Math.PI / Math.tan(1 / Math.tan(Math.PI * f2 * frameCounter / sampleRate));
                    case "5" -> buffer[0][s] += A2 * Math.random() * 2 - 1;
                }
            }
            // Write the buffer
            wavFile.writeFrames(buffer, toWrite);
        }
        // Close the wavFile
        wavFile.close();

    }

    private static void uiModulation() throws IOException, WavFileException {
        // Calculate the number of frames required for specified duration
        long numFrames = (long) (duration * sampleRate);

        // Create a wav file with the name specified as the first argument
        WavFile wavFile = WavFile.newWavFile(new File(filePath), numChannels, numFrames, validBits, sampleRate);

        // Create a buffer of 100 frames
        double[][] buffer = new double[1][100];

        double f1 = 128;
        double A1 = 1;
        double dc1 = 0.5;

        double f2 = 128;
        double A2 = 1;
        double dc2 = 0.5;

        System.out.println("Модулирующий(информационный) сигнал:\n" +
                "1. Синусоида\n" +
                "2. Импульс\n" +
                "3. Треугольная\n" +
                "4. Пила\n" +
                "5. Шум");
        String signal1 = scanner.nextLine();

        switch (signal1) {
            case "1", "3", "4" -> {
                System.out.println("Введите: A, f");
                A1 = scanner.nextDouble();
                f1 = scanner.nextInt();
            }
            case "2" -> {
                System.out.println("Введите: A, f, dc");
                A1 = scanner.nextDouble();
                f1 = scanner.nextInt();
                dc1 = scanner.nextDouble();
            }
            case "5" -> {
                System.out.println("Введите: A");
                A1 = scanner.nextDouble();
            }
        }

        System.out.println("Модулируемый (несущий) сигнал:\n" +
                "1. Синусоида\n" +
                "2. Импульс\n" +
                "3. Треугольная\n" +
                "4. Пила\n" +
                "5. Шум");
        String signal2 = scanner.nextLine();
        signal2 = scanner.nextLine();// говносканер читает пустую строку, поэтому пусть читает дважды
        System.out.println(signal2);

        switch (signal2) {
            case "1", "3", "4" -> {
                System.out.println("Введите: A, f");
                A2 = scanner.nextDouble();
                f2 = scanner.nextInt();
            }
            case "2" -> {
                System.out.println("Введите: A, f, dc");
                A2 = scanner.nextDouble();
                f2 = scanner.nextInt();
                dc2 = scanner.nextDouble();
            }
            case "5" -> {
                System.out.println("Введите: A");
                A2 = scanner.nextDouble();
            }
        }

        // Initialise a local frame counter
        long frameCounter = 0;

        // Loop until all frames written
        while (frameCounter < numFrames) {
            // Determine how many frames to write, up to a maximum of the buffer size
            long remaining = wavFile.getFramesRemaining();
            int toWrite = (remaining > 100) ? 100 : (int) remaining;

            // Fill the buffer, one tone per channel
            for (int s = 0; s < toWrite; s++, frameCounter++) {
                double um = 0;
                double uc = 0;
                switch (signal1) {
                    case "1" -> um = sinWave(A1, f1, frameCounter, sampleRate);
                    case "2" -> um = squareWave(A1, f1, frameCounter, sampleRate, dc1);
                    case "3" ->
                            um = 2 * A1 / Math.PI / Math.sin(Math.sin(2 * Math.PI * f1 * frameCounter / sampleRate));
                    case "4" ->
                            um = -2 * A1 / Math.PI / Math.tan(1 / Math.tan(Math.PI * f1 * frameCounter / sampleRate));
                    case "5" -> um = A1 * Math.random() * 2 - 1;
                }
                System.out.println(um);
                switch (signal2) {
                    case "1" -> {
                        uc = sinWave(A2, f2 * ((um + A1) / (2 * A1)), frameCounter, sampleRate);
                    }
                    case "2" -> uc = squareWave(A2, f2 * ((um + A1) / (2 * A1)), frameCounter, sampleRate, dc2);
                    case "3" ->
                            uc = 2 * A2 / Math.PI / Math.sin(Math.sin(2 * Math.PI * f2 * ((um + A1) / (2 * A1)) * frameCounter / sampleRate));
                    case "4" ->
                            uc = -2 * A2 / Math.PI / Math.tan(1 / Math.tan(Math.PI * f2 * ((um + A1) / (2 * A1)) * frameCounter / sampleRate));
                    case "5" -> uc = A2 * Math.random() * 2 - 1;
                }
                double uam = uc;
                buffer[0][s] = uam;
            }
            // Write the buffer
            wavFile.writeFrames(buffer, toWrite);
        }
        // Close the wavFile
        wavFile.close();
    }
}
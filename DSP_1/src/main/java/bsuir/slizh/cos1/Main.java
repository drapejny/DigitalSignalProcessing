package bsuir.slizh.cos1;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {
            int sampleRate = 44100;        // Samples per second
            double duration = 3.0;        // Seconds

            // Calculate the number of frames required for specified duration
            long numFrames = (long) (duration * sampleRate);

            // Create a wav file with the name specified as the first argument
            WavFile wavFile = WavFile.newWavFile(new File(args[0]), 1, numFrames, 16, sampleRate);

            // Create a buffer of 100 frames
            double[][] buffer = new double[1][100];

            // Initialise a local frame counter
            long frameCounter = 0;

            // Loop until all frames written
            while (frameCounter < numFrames) {
                // Determine how many frames to write, up to a maximum of the buffer size
                long remaining = wavFile.getFramesRemaining();
                int toWrite = (remaining > 100) ? 100 : (int) remaining;

                double dc = 0.5;
                double f = 2048;
                double T = 1 / f;
                double A = 5;

                // Fill the buffer, one tone per channel
                for (int s = 0; s < toWrite; s++, frameCounter++) {
                    // Sin wave
                    //buffer[0][s] = A * Math.sin(2.0 * Math.PI * f * frameCounter / sampleRate);
                    // Square wave
                    //buffer[0][s] = ((((double) frameCounter / sampleRate) % T) / T) < dc ? A : -A;
                    // Triangle wave
                    //buffer[0][s] = 2 * A / Math.PI / Math.sin(Math.sin(2 * Math.PI * f * frameCounter / sampleRate));
                    // Sawtooth wave
                    //buffer[0][s] = -2 * A / Math.PI / Math.tan(1 / Math.tan(Math.PI * f * frameCounter / sampleRate));
                    // Random wave
                    //buffer[0][s] = A * Math.random() * 2 - 1;
                }

                // Write the buffer
                wavFile.writeFrames(buffer, toWrite);
            }

            // Close the wavFile
            wavFile.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
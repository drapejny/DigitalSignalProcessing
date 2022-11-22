package by.slizh.cos3.blur;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GaussianBlur implements Blur {


    private BufferedImage sourceImage;
    private double variance;
    private int iterations;
    private int windowSize;


    public GaussianBlur(BufferedImage sourceImage, double variance, int iterations) {
        this.sourceImage = sourceImage;
        this.variance = variance;
        this.iterations = iterations;
        windowSize = (int) Math.floor(6 * variance);
        if (windowSize % 2 == 0) {
            windowSize++;
        }
    }

    @Override
    public BufferedImage blur() {

        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();

        BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int radius = windowSize / 2; // Integer division!!!

        double[][] weightMatrix = generateWeightMatrix();

        for (int iteration = 0; iteration < iterations; iteration++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    double sumRed = 0;
                    double sumGreen = 0;
                    double sumBlue = 0;

                    for (int i = 0; i < windowSize; i++) {
                        if (y - radius + i < 0 || y - radius + i >= height) {
                            continue;
                        }
                        for (int j = 0; j < windowSize; j++) {
                            if (x - radius + j < 0 || x - radius + j >= width) {
                                continue;
                            }
                            sumRed += (((sourceImage.getRGB(x - radius + j, y - radius + i) & 0xff0000) >> 16) * weightMatrix[j][i]);
                            sumGreen += (((sourceImage.getRGB(x - radius + j, y - radius + i) & 0xff00) >> 8) * weightMatrix[j][i]);
                            sumBlue += ((sourceImage.getRGB(x - radius + j, y - radius + i) & 0xff) * weightMatrix[j][i]);
                        }
                    }

                    Color color = new Color((int) sumRed, (int) sumGreen, (int) sumBlue);

                    destImage.setRGB(x, y, color.getRGB());
                }
            }
            // For next iteration
            sourceImage = destImage;
        }
        return destImage;
    }

    private double[][] generateWeightMatrix() {
        double[][] weights = new double[windowSize][windowSize];
        int radius = windowSize / 2; // Integer division!!!
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = gaussianModel(i - radius, j - radius, variance);
                sum += weights[i][j];
                System.out.print(weights[i][j] + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = weights[i][j] / sum;
            }
        }
        return weights;
    }

    private double gaussianModel(int x, int y, double variance) {
        return Math.pow(Math.E, -(Math.pow(x, 2) + Math.pow(y, 2)) / (2 * Math.pow(variance, 2))) / (2 * Math.PI * Math.pow(variance, 2));
    }
}

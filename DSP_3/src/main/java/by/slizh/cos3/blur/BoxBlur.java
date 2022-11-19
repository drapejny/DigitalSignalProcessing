package by.slizh.cos3.blur;

import java.awt.image.BufferedImage;
import java.awt.*;


public class BoxBlur implements Blur {

    private BufferedImage sourceImage;
    private int windowSize;
    private int iterations;

    @Override
    public BufferedImage blur() {

        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();

        BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int radius = windowSize / 2; // Integer division!!!

        for (int iteration = 0; iteration < iterations; iteration++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    int sumRed = 0;
                    int sumGreen = 0;
                    int sumBlue = 0;

                    int pixelNum = 0;

                    int startI = Math.max(x - radius, 0);
                    int endI = x + radius >= width ? width - 1 : x + radius;
                    int startJ = Math.max(y - radius, 0);
                    int endJ = y + radius >= height ? height - 1 : y + radius;
                    for (int i = startI; i <= endI; i++) {
                        for (int j = startJ; j <= endJ; j++) {
                            sumRed += (sourceImage.getRGB(i, j) & 0xff0000) >> 16;
                            sumGreen += (sourceImage.getRGB(i, j) & 0xff00) >> 8;
                            sumBlue += sourceImage.getRGB(i, j) & 0xff;
                            pixelNum ++;
                        }
                    }

                    Color color = new Color(sumRed / pixelNum, sumGreen / pixelNum, sumBlue / pixelNum);

                    destImage.setRGB(x, y, color.getRGB());
                }
            }
            // For next iteration
            sourceImage = destImage;
        }

        return destImage;
    }

    public BoxBlur(BufferedImage sourceImage, int windowSize, int iterations) {
        this.sourceImage = sourceImage;
        this.windowSize = windowSize;
        this.iterations = iterations;
    }
}

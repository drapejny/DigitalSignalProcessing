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

                    for (int i = 0; i < windowSize; i++) {
                        if (y - radius + i < 0 || y - radius + i >= height) {
                            continue;
                        }
                        for (int j = 0; j < windowSize; j++) {
                            if (x - radius + j < 0 || x - radius + j >= width) {
                                continue;
                            }
                            sumRed += (sourceImage.getRGB(x - radius + j, y - radius + i) & 0xff0000) >> 16;
                            sumGreen += (sourceImage.getRGB(x - radius + j, y - radius + i) & 0xff00) >> 8;
                            sumBlue += sourceImage.getRGB(x - radius + j, y - radius + i) & 0xff;
                            pixelNum++;
                        }
                    }

//                    int startI = Math.max(y - radius, 0);
//                    int endI = y + radius >= height ? height - 1 : y + radius;
//                    int startJ = Math.max(x - radius, 0);
//                    int endJ = x + radius >= width ? width - 1 : x + radius;
//                    for (int i = startI; i <= endI; i++) {
//                        for (int j = startJ; j <= endJ; j++) {
//                            sumRed += (sourceImage.getRGB(j, i) & 0xff0000) >> 16;
//                            sumGreen += (sourceImage.getRGB(j, i) & 0xff00) >> 8;
//                            sumBlue += sourceImage.getRGB(j, i) & 0xff;
//                            pixelNum++;
//                        }
//                    }

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

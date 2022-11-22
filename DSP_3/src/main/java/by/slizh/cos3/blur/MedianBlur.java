package by.slizh.cos3.blur;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MedianBlur implements Blur {

    private BufferedImage sourceImage;
    private int windowSize;
    private int iterations;

    public MedianBlur(BufferedImage sourceImage, int windowSize, int iterations) {
        this.sourceImage = sourceImage;
        this.windowSize = windowSize;
        this.iterations = iterations;
    }

    @Override
    public BufferedImage blur() {

        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();

        BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int radius = windowSize / 2; // Integer division!!!

        for (int iteration = 0; iteration < iterations; iteration++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    List<Integer> redList = new ArrayList<>();
                    List<Integer> greenList = new ArrayList<>();
                    List<Integer> blueList = new ArrayList<>();

                    for (int i = 0; i < windowSize; i++) {
                        if (y - radius + i < 0 || y - radius + i >= height) {
                            continue;
                        }
                        for (int j = 0; j < windowSize; j++) {
                            if (x - radius + j < 0 || x - radius + j >= width) {
                                continue;
                            }
                            redList.add((sourceImage.getRGB(x - radius + j, y - radius + i) & 0xff0000) >> 16);
                            greenList.add((sourceImage.getRGB(x - radius + j, y - radius + i) & 0xff00) >> 8);
                            blueList.add(sourceImage.getRGB(x - radius + j, y - radius + i) & 0xff);
                        }
                    }
                    Collections.sort(redList);
                    Collections.sort(greenList);
                    Collections.sort(blueList);

                    Color color = new Color(redList.get(redList.size() / 2), greenList.get(greenList.size() / 2), blueList.get(blueList.size() / 2));

                    destImage.setRGB(x, y, color.getRGB());
                }
            }
            // For next iteration
            sourceImage = destImage;
        }
        return destImage;
    }
}

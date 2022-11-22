package by.slizh.cos3.blur;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SobelOperator implements Blur {

    private BufferedImage sourceImage;

    private final int WINDOW_SIZE = 3;

    public SobelOperator(BufferedImage sourceImage) {
        this.sourceImage = sourceImage;
    }

    @Override
    public BufferedImage blur() {

        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();

        BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int[][] greys = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                greys[i][j] = getGrey(sourceImage.getRGB(i, j));
            }
        }

        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                int gX = greys[i - 1][j + 1] + 2 * greys[i][j + 1] + greys[i + 1][j + 1]
                        - greys[i - 1][j - 1] - 2 * greys[i][j - 1] - greys[i + 1][j - 1];
                int gY = greys[i - 1][j - 1] + 2 * greys[i - 1][j] + greys[i - 1][j + 1]
                        - greys[i + 1][j - 1] - 2 * greys[i + 1][j] - greys[i + 1][j + 1];

                int g = (int) Math.sqrt(Math.pow(gX, 2) + Math.pow(gY, 2));

                int gg = g / 3;

                if (gg < 0) {
                    gg = 0;
                }
                if (gg > 255) {
                    gg = 255;
                }
                try {
                    destImage.setRGB(i, j, new Color(gg, gg, gg).getRGB());
                } catch (IllegalArgumentException e) {
                    System.out.println(g / 3);
                }
            }
        }
        return destImage;
    }

    private int getGrey(int rgb) {
        int red = (rgb & 0xff0000) >> 16;
        int green = (rgb & 0xff00) >> 8;
        int blue = rgb & 0xff;
        return (red + green + blue) / 3; // Integer division!!!
    }
}

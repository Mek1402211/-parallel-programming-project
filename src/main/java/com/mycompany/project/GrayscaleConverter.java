package com.mycompany.project;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author mohamed khamis
 */
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.stream.IntStream;

public class GrayscaleConverter {

    public static void main(String[] args) {
        try {
            // Load image
            BufferedImage inputImage = ImageIO.read(new File("G:/mokhamis/java/project/exampls/pexels-eberhardgross-1624496.jpg"));
            BufferedImage outputImage = new BufferedImage(
                    inputImage.getWidth(),
                    inputImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );

            long startTime1 = System.currentTimeMillis();
            // Sequential conversion
            for (int y = 0; y < inputImage.getHeight(); y++) {
                for (int x = 0; x < inputImage.getWidth(); x++) {
                    int rgb = inputImage.getRGB(x, y);
                    int gray = toGrayscale(rgb);
                    outputImage.setRGB(x, y, gray);
                }
            }
            long sequentialTime = System.currentTimeMillis() - startTime1;
            // Parallel grayscale conversion
            long startTime2 = System.currentTimeMillis();
            IntStream.range(0, inputImage.getHeight()).parallel().forEach(y -> {
                for (int x = 0; x < inputImage.getWidth(); x++) {
                    int rgb = inputImage.getRGB(x, y);
                    int gray = toGrayscale(rgb);
                    outputImage.setRGB(x, y, gray);
                }
            });
            long parallelTime = System.currentTimeMillis() - startTime2;
            // Save output
            ImageIO.write(outputImage, "jpg", new File("output/output_grayscale.jpg"));
            System.out.println("Sequential: " + sequentialTime + " ms");
            System.out.println("Parallel: " + parallelTime + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int toGrayscale(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        int gray = (int) (0.299 * r + 0.587 * g + 0.114 * b);
        return (gray << 16) | (gray << 8) | gray;
    }
}

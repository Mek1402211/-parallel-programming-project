/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.project;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ForkJoinPool;
import javax.imageio.ImageIO;

/**
 *
 * @author mohamed khamis
 */
public class Project {

    public static void main(String[] args) {
        try {
            BufferedImage inputImage = ImageIO.read(new File("G:/mokhamis/java/project/exampls/pexels-eberhardgross-1624496.jpg"));
            BufferedImage outputImage = new BufferedImage(
                    inputImage.getWidth(),
                    inputImage.getHeight(),
                    inputImage.getType()
            );
            long startTime = System.currentTimeMillis();
            // Sequential processing
            for (int x = 0; x < inputImage.getWidth(); x++) {
                for (int y = 0; y < inputImage.getHeight(); y++) {
                    outputImage.setRGB(x, y, applyBlur(inputImage, x, y));
                }
            }
            ForkJoinPool Pool = ForkJoinPool.commonPool();
         
        System.out.println("Number of active thread before invoking: "
            + Pool.getActiveThreadCount());
            long sequentialTime = System.currentTimeMillis() - startTime;
            startTime = System.currentTimeMillis();
            ForkJoinPool pool = new ForkJoinPool();
            pool.invoke(new BlurTask(
                    inputImage,
                    outputImage,
                    0, 0,
                    inputImage.getWidth(),
                    inputImage.getHeight()
            ));
            long parallelTime = System.currentTimeMillis() - startTime;
            System.out.println("Sequential: " + sequentialTime + " ms");
            System.out.println("Parallel: " + parallelTime + " ms");
            ImageIO.write(outputImage, "jpg", new File("output/output_blurred.jpg"));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int applyBlur(BufferedImage image, int x, int y) {
        int radius = 2; // Kernel radius (adjust for blur intensity)
        int sumRed = 0, sumGreen = 0, sumBlue = 0, count = 0;

        // Iterate over neighboring pixels within the kernel
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < image.getWidth() && ny >= 0 && ny < image.getHeight()) {
                    int rgb = image.getRGB(nx, ny);
                    sumRed += (rgb >> 16) & 0xFF;
                    sumGreen += (rgb >> 8) & 0xFF;
                    sumBlue += rgb & 0xFF;
                    count++;
                }
            }
        }

        // Average the RGB values
        int avgRed = sumRed / count;
        int avgGreen = sumGreen / count;
        int avgBlue = sumBlue / count;
        return (avgRed << 16) | (avgGreen << 8) | avgBlue;
    }
}

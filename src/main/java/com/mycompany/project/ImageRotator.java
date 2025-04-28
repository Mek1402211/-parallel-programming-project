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
import java.io.File;
import java.util.concurrent.ForkJoinPool;
import java.awt.image.BufferedImage;

public class ImageRotator {

    public static void main(String[] args) throws Exception {
        try {
            BufferedImage input = ImageIO.read(new File("G:/mokhamis/java/project/exampls/pexels-eberhardgross-1624496.jpg"));
            BufferedImage output = new BufferedImage(
                    input.getWidth(), input.getHeight(), input.getType()
            );
            double angleDegrees = 45; // Rotation angle in degrees
            double angleRadians = Math.toRadians(angleDegrees);
            int centerX = input.getWidth() / 2;
            int centerY = input.getHeight() / 2;
            //Sequential conversion
            long startTime = System.currentTimeMillis();
            for (int x = 0; x < input.getWidth(); x++) {
                for (int y = 0; y < input.getHeight(); y++) {
                    rotatePixel(input, output, x, y, angleRadians, centerX, centerY);
                }
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Sequential execution time: " + (endTime - startTime) + " ms");

            ImageIO.write(output, "png", new File("output/rotated_sequential.png"));
            long startTime2 = System.currentTimeMillis();
            // Parallel rotation
            ForkJoinPool pool = new ForkJoinPool();
            pool.invoke(new RotationTask(
                    input, output,
                    0, 0, input.getWidth(), input.getHeight(),
                    angleRadians, centerX, centerY
            ));
            long parallelTime = System.currentTimeMillis() - startTime2;
            ImageIO.write(output, "jpg", new File("output/rotated2.jpg"));
            System.out.println("parallel execution rotation Time: " + parallelTime + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void rotatePixel(BufferedImage input, BufferedImage output,
            int x, int y, double angle,
            int centerX, int centerY) {
        // Convert to relative coordinates
        double relX = x - centerX;
        double relY = y - centerY;

        // Apply rotation matrix
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        int srcX = (int) (relX * cos - relY * sin) + centerX;
        int srcY = (int) (relX * sin + relY * cos) + centerY;

        // Copy pixel if within bounds
        if (srcX >= 0 && srcX < input.getWidth() && srcY >= 0 && srcY < input.getHeight()) {
            output.setRGB(x, y, input.getRGB(srcX, srcY));
        } else {
            output.setRGB(x, y, 0xFFFFFF); // White for out-of-bounds
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.project;

/**
 *
 * @author mohamed khamis
 */
import javax.imageio.ImageIO;
import java.io.File;
import java.util.concurrent.ForkJoinPool;
import java.awt.image.BufferedImage;

public class SobelEdgeDetector {

    public static void main(String[] args) throws Exception {
        try {
            BufferedImage input = ImageIO.read(new File("G:/mokhamis/java/project/exampls/pexels-eberhardgross-1624496.jpg"));
            BufferedImage grayscale = convertToGrayscale(input);
            BufferedImage output = new BufferedImage(
                    grayscale.getWidth(),
                    grayscale.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );
            long start = System.currentTimeMillis();
            BufferedImage edges = sobelSequential(convertToGrayscale(input)); // Sequential
            System.out.println("Sequential: " + (System.currentTimeMillis() - start) + "ms");
             ImageIO.write(edges, "jpg", new File("output/edges_Sequential.jpg"));
            start = System.currentTimeMillis();
            // Parallel processing
            ForkJoinPool pool = new ForkJoinPool();
            pool.invoke(new SobelTask(grayscale, output,
                    0, 0, grayscale.getWidth(), grayscale.getHeight()));
            System.out.println("Parallel: " + (System.currentTimeMillis() - start) + "ms");
            ImageIO.write(output, "jpg", new File("output/edges_Parallel.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage convertToGrayscale(BufferedImage colorImage) {
        BufferedImage gray = new BufferedImage(
                colorImage.getWidth(),
                colorImage.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY
        );
        gray.getGraphics().drawImage(colorImage, 0, 0, null);
        return gray;
    }

    public static BufferedImage sobelSequential(BufferedImage gray) {
        BufferedImage edges = new BufferedImage(
                gray.getWidth(), gray.getHeight(), BufferedImage.TYPE_INT_RGB
        );

        // Define kernels explicitly
        int[][] gxKernel = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] gyKernel = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

        for (int x = 1; x < gray.getWidth() - 1; x++) {
            for (int y = 1; y < gray.getHeight() - 1; y++) {
                int gx = 0, gy = 0;

                // Convolve with kernels
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int pixel = gray.getRGB(x + i, y + j) & 0xFF;
                        gx += pixel * gxKernel[i + 1][j + 1];
                        gy += pixel * gyKernel[i + 1][j + 1];
                    }
                }

                int magnitude = (int) Math.sqrt(gx * gx + gy * gy);
                edges.setRGB(x, y, 0xFF000000 | (magnitude << 16) | (magnitude << 8) | magnitude);
            }
        }
        return edges;
    }
}

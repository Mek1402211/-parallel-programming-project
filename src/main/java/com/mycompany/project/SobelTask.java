/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.project;

/**
 *
 * @author mohamed khamis
 */
import java.awt.image.BufferedImage;
import java.util.concurrent.RecursiveAction;


public class SobelTask extends RecursiveAction {
    private static final int THRESHOLD = 10000; // Split if >100x100 pixels
    private final BufferedImage input;
    private final BufferedImage output;
    private final int startX, startY, endX, endY;
    int[][] gx = {{-1,0,1}, {-2,0,2}, {-1,0,1}};
    int[][] gy = {{-1,-2,-1}, {0,0,0}, {1,2,1}};
    public SobelTask(BufferedImage input, BufferedImage output, 
                    int startX, int startY, int endX, int endY) {
        this.input = input;
        this.output = output;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    @Override
    protected void compute() {
        int area = (endX - startX) * (endY - startY);
        if (area <= THRESHOLD) {
            applySobel();
        } else {
            int midX = startX + (endX - startX) / 2;
            int midY = startY + (endY - startY) / 2;
            invokeAll(
                new SobelTask(input, output, startX, startY, midX, midY),
                new SobelTask(input, output, midX, startY, endX, midY),
                new SobelTask(input, output, startX, midY, midX, endY),
                new SobelTask(input, output, midX, midY, endX, endY)
            );
        }
    }

    private void applySobel() {
        for (int x = Math.max(1, startX); x < Math.min(endX, input.getWidth() - 1); x++) {
            for (int y = Math.max(1, startY); y < Math.min(endY, input.getHeight() - 1); y++) {
                int gx = 0, gy = 0;
                
                // Convolve with Sobel kernels
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int rgb = input.getRGB(x + i, y + j);
                        int gray = (rgb & 0xFF); // Use green channel for grayscale
                        
                        // Gx kernel
                        gx += gray * ( (i == 0) ? 0 : (i * (j == 0 ? 2 : 1) ) * (j > 0 ? 1 : -1));
                        
                        // Gy kernel
                        gy += gray * ( (j == 0) ? 0 : (j * (i == 0 ? 2 : 1) ) * (i > 0 ? 1 : -1));
                    }
                }
                
                int magnitude = (int) Math.sqrt(gx * gx + gy * gy);
                magnitude = Math.min(255, magnitude); // Clamp to 0-255
                output.setRGB(x, y, (0xFF << 24) | (magnitude << 16) | (magnitude << 8) | magnitude);
            }
        }
    }
}
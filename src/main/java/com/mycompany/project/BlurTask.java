/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.project;

import java.awt.image.BufferedImage;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;
/**
 *
 * @author mohamed khamis
 */
public class BlurTask extends RecursiveAction{
    private BufferedImage input, output;
    private int startX, startY, endX, endY;
    private static final int THRESHOLD = 100; // Split if region > 100x100 pixels

    public BlurTask(BufferedImage input, BufferedImage output, int startX, int startY, int endX, int endY) {
        this.input = input;
        this.output = output;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    @Override
    protected void compute() {
        int width = endX - startX;
        int height = endY - startY;

        // If region is small enough, process directly
        if (width * height < THRESHOLD) {
            for (int x = startX; x < endX; x++) {
                for (int y = startY; y < endY; y++) {
                    output.setRGB(x, y, applyBlur(input, x, y));
                }
            }
        } else {
            // Split the region into quadrants
            int midX = startX + (width / 2);
            int midY = startY + (height / 2);
            invokeAll(
                new BlurTask(input, output, startX, startY, midX, midY),
                new BlurTask(input, output, midX, startY, endX, midY),
                new BlurTask(input, output, startX, midY, midX, endY),
                new BlurTask(input, output, midX, midY, endX, endY)
            );
        }
    }
    private static int applyBlur(BufferedImage image, int x, int y) {
    int radius = 5; // Kernel radius (adjust for blur intensity)
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

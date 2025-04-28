package com.mycompany.project;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author mohamed khamis
 */
import java.awt.image.BufferedImage;
import java.util.concurrent.RecursiveAction;

public class RotationTask extends RecursiveAction {
    
    private final BufferedImage input, output;
    private final int startX, startY, endX, endY;
    private final double angle;
    private final int centerX, centerY;
    private static final int THRESHOLD = 50; // Split if region > 50x50 pixels

    public RotationTask(BufferedImage input, BufferedImage output, 
                      int startX, int startY, int endX, int endY,
                      double angle, int centerX, int centerY) {
        this.input = input;
        this.output = output;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.angle = angle;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    @Override
    protected void compute() {
        int width = endX - startX;
        int height = endY - startY;

        if (width * height < THRESHOLD) {
            // Process small region directly
            for (int x = startX; x < endX; x++) {
                for (int y = startY; y < endY; y++) {
                    rotatePixel(x, y);
                }
            }
        } else {
            // Split into quadrants
            int midX = startX + (width / 2);
            int midY = startY + (height / 2);
            invokeAll(
                new RotationTask(input, output, startX, startY, midX, midY, angle, centerX, centerY),
                new RotationTask(input, output, midX, startY, endX, midY, angle, centerX, centerY),
                new RotationTask(input, output, startX, midY, midX, endY, angle, centerX, centerY),
                new RotationTask(input, output, midX, midY, endX, endY, angle, centerX, centerY)
            );
        }
    }

    private void rotatePixel(int x, int y) {
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
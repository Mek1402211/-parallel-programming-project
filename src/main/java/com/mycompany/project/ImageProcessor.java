/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.project;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
/**
 *
 * @author mohamed khamis
 */
public class ImageProcessor {
    public static void main(String[] args) {
        try {
            BufferedImage inputImage = ImageIO.read(new File("G:/mokhamis/java/task.png"));
            BufferedImage outputImage = new BufferedImage(
                inputImage.getWidth(),
                inputImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

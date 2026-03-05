package com.chat.util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class ImageCropUtil {
    public static Image cropToSquare(Image sourceImage) {
        // Get the original image dimensions
        double width = sourceImage.getWidth();
        double height = sourceImage.getHeight();

        // Determine the size of the square and the cropping start point
        double squareSize = Math.min(width, height); // Use the smaller dimension
        double startX = (width - squareSize) / 2;    // Center horizontally
        double startY = (height - squareSize) / 2;   // Center vertically

        // Create a PixelReader to read pixel data from the source image
        PixelReader pixelReader = sourceImage.getPixelReader();
        if (pixelReader == null) {
            throw new IllegalArgumentException("Cannot read pixels from the source image");
        }

        // Create a WritableImage for the cropped square
        WritableImage croppedImage = new WritableImage(pixelReader, (int) startX, (int) startY, (int) squareSize, (int) squareSize);

        return croppedImage;
    }
}
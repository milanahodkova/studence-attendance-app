package utils;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Util class for image scaling
 *
 * @author M.O. Hodkova
 * @version 1.0
 */
public class ImageScaller {
    /**
     * Scale image
     * @param imagePath path to image
     * @param newDimension new image size
     * @param hints Image scale algorithm
     * @return scaled Image instance
     */
    public static Image scaleImage(String imagePath,
                                   Dimension newDimension,
                                   int hints) {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(ImageScaller.class.getResource(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bufferedImage.getScaledInstance(newDimension.width, newDimension.height, hints);
    }

}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui_ocr;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 *
 * @author fiona
 */
public abstract class Filter {

    BufferedImage image;
    BufferedImage orginal;
    
    public static Filter FILTER_ACTIVE;

    public Filter(BufferedImage img) {
        image = img;
        orginal = cloneSelf(img);

    }

    public abstract BufferedImage apply();

    public void undo() {
        image = cloneSelf(orginal);
    }

    private BufferedImage cloneSelf(BufferedImage image) {

        ColorModel cm = image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);

    }
}

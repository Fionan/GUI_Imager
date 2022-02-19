/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui_ocr;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javafx.scene.image.Image;

/**
 *
 * @author fiona
 */
public final class BlackWhiteFilter extends Filter{

    public BlackWhiteFilter(BufferedImage img) {
        super(img);
        
    }

 

    @Override
    public BufferedImage apply() {
         BufferedImage result = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_BYTE_BINARY);

            Graphics2D graphic = result.createGraphics();
            graphic.drawImage(image, 0, 0, Color.WHITE, null);
            graphic.dispose();
            
            
            
            return  result;
    }
    
}

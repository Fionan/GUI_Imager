/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui_ocr;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author fiona
 */
public final class GridFilter extends Filter {

    Graphics2D graphic;
    Color lineColor =Color.RED; 
    int strokeSize = 1;

    public GridFilter(BufferedImage img) {
        super(img);
        graphic = img.createGraphics();
       
    }

    @Override
    public BufferedImage apply() {

        graphic.setStroke(new BasicStroke(strokeSize));
        graphic.setColor(lineColor);

        Integer[] linesHeight = findLinesHeight(image);

        for (Integer i : linesHeight) {
            graphic.drawLine(0, i-2, image.getWidth(), i-2);
        }

        graphic.dispose();

        return image;
    }

    static Integer[] findLinesHeight(BufferedImage image) {

        boolean lookingForBlack = true;
        boolean whiteON = false;
        boolean canStart = false;
       

        ArrayList<Integer> linesStart = new ArrayList<>();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int clr = image.getRGB(x, y);
                int red = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue = clr & 0x000000ff;
              
                //has black cell been found?

                if (red < 50 && green < 50 && blue < 50) {
                    whiteON = false;
                  

                    if (lookingForBlack) {
                        lookingForBlack = false;
                        whiteON = false;
                        canStart = true;
                        linesStart.add(y);
                    }

                    break;

                }

                //Cell is white?
                whiteON = true;
           
            }

            if (whiteON && canStart) {
                lookingForBlack = true;
                whiteON = false;
            }

        }
        return linesStart.toArray(new Integer[linesStart.size()]);

    }
}

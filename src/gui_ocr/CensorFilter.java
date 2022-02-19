/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui_ocr;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Color;

/**
 *
 * @author fiona
 */
public class CensorFilter extends Filter {

    Graphics2D graphic;
    Point p1, p2;

    public CensorFilter(Point p1, Point p2, BufferedImage img) {

        super(img);
        graphic = img.createGraphics();
        this.p1 = p1;
        this.p2 = p2;

    }

    @Override
    public BufferedImage apply() {
        graphic.setColor(Color.white);
        graphic.fill(new Rectangle(p1.x + 2, p1.y + 2, p2.x - 2, p2.y - 2));
        
        graphic.setColor(Color.black);
        graphic.draw(new Rectangle(p1.x, p1.y, p2.x, p2.y));

        graphic.dispose();

        return this.image;

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui_ocr;

import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 *
 * @author fiona
 */
public final class CropFilter extends Filter{
    Point p1,p2;
    
    public CropFilter(Point p1, Point p2,BufferedImage img) {
        super(img);
        this.p1=p1;
        this.p2=p2;
        
    }

    
    
    @Override
    public BufferedImage apply() {
        this.image = image.getSubimage(p1.x, p1.y, p2.x,p2.y);
        return this.image;
    }
    
}

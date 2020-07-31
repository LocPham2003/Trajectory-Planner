package Graphing;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Point {
    private double x;
    private double y;
    private double radius;
    private boolean selected;
    private Color color;

    public void setX(Double x) {
        this.x = x;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public void setRadius(Double radius){
        this.radius = radius;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius(){
        return this.radius;
    }

    public void draw(Graphics g2d) {
        Color prevColor = g2d.getColor();
        g2d.drawString("X : " + this.x + " Y : " + this.y , (int) (this.x + this.radius), (int) (this.getY() + this.radius));
        g2d.setColor(color);
        g2d.fillOval((int) x, (int) y, (int) radius, (int) radius);
        g2d.setColor(prevColor);
    }

    void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            color = Color.GREEN;
        } else {
            color = Color.RED;
        }
    }

    Rectangle2D getBounds() {
        return new Rectangle2D.Double(x, y, radius, radius);
    }

    boolean isSelected() {
        return selected;
    }



}

package Graphing;

import Splines.SplineFunctions;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DrawPanel extends JPanel {

//    @Override
//    protected void paintComponent(Graphics graphics) {
//        super.paintComponent(graphics);
//        Graphics2D g2d = (Graphics2D) graphics;
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        for (Point point : listOfInterpolatedPoints) {
//            point.draw(g2d);
//        }
//
//    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }
}

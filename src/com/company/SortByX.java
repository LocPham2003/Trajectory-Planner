package com.company;

import Splines.Point;

import java.util.Comparator;

public class SortByX implements Comparator<Point> {

    public int compare(Point o, Point o1) {
        return (int) (o.getX() - o1.getX());
    }
}

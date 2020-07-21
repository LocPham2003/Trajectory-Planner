package com.company;

import Matrices.Matrix;
import Splines.NaturalCubicSpline;

public class Main {
    public static void main(String[] args) {
        Matrix matrix = new Matrix();
        NaturalCubicSpline naturalCubicSpline = new NaturalCubicSpline();

        matrix.generateMatrix("leftSideValues.txt", "rightSideValues.txt");
        naturalCubicSpline.interpolate();
        //System.out.println(Arrays.deepToString(matrix.getLeftSideValues()));

        //System.out.println(Arrays.deepToString(matrix.solveMatrixByGaussianElimination()));

    }
}

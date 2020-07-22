package com.company;

import Matrices.Matrix;
import Splines.NaturalCubicSpline;

public class Main {
    public static void main(String[] args) {
        Matrix matrix = new Matrix();
        NaturalCubicSpline naturalCubicSpline = new NaturalCubicSpline();

        naturalCubicSpline.interpolate();

        matrix.generateMatrix(Constants.leftSideValuesFile, Constants.rightSideValuesFile);
        //System.out.println(Arrays.deepToString(matrix.getLeftSideValues()));

        //System.out.println(Arrays.deepToString(matrix.solveMatrixByGaussianElimination()));

    }
}

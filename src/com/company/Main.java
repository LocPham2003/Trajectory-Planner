package com.company;

import Matrices.Matrix;
import Splines.NaturalCubicSpline;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Matrix matrix = new Matrix();
        NaturalCubicSpline naturalCubicSpline = new NaturalCubicSpline();

        if (naturalCubicSpline.interpolate()){
            matrix.generateMatrix(Constants.leftSideValuesFile, Constants.rightSideValuesFile);
        }


        System.out.println(matrix.solveMatrixByGaussianElimination());
    }
}

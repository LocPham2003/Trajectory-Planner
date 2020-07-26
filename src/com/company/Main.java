package com.company;

import Matrices.Matrix;
import Splines.NaturalCubicSpline;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Matrix matrix = new Matrix();
        NaturalCubicSpline naturalCubicSpline = new NaturalCubicSpline();

        naturalCubicSpline.interpolate();

        matrix.generateMatrix(Constants.leftSideValuesFile, Constants.rightSideValuesFile);

        System.out.println(Arrays.toString(matrix.getRightSideValues()));

        matrix.solveMatrixByGaussianElimination();

        for (int i = 0; i < matrix.getLeftSideValues().length; i++){
            for (int k = 0; k < matrix.getLeftSideValues()[i].length; k++){
                System.out.print(matrix.getLeftSideValues()[i][k] + " ");
            }
            System.out.print(" = " + matrix.getRightSideValues()[i]);
            System.out.println();
        }

        //System.out.println(Arrays.deepToString(matrix.getLeftSideValues()));

        //System.out.println(Arrays.deepToString(matrix.solveMatrixByGaussianElimination()));

    }
}

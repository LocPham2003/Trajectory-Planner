package com.company;

import Matrices.Matrix;
import Splines.NaturalCubicSpline;

public class Main {
    public static void main(String[] args) {
        Matrix matrix = new Matrix();
        NaturalCubicSpline naturalCubicSpline = new NaturalCubicSpline();

        naturalCubicSpline.interpolate();

        matrix.generateMatrix(Constants.leftSideValuesFile, Constants.rightSideValuesFile);

        for (int i = 0; i < matrix.solveMatrixByGaussianElimination().length; i++){
            for (int k = 0; k < matrix.solveMatrixByGaussianElimination()[i].length; k++){
                System.out.print(matrix.solveMatrixByGaussianElimination()[i][k] + " ");
            }
            System.out.print(" = " + matrix.getRightSideValues()[i]);
            System.out.println();
        }


        //System.out.println(Arrays.deepToString(matrix.getLeftSideValues()));

        //System.out.println(Arrays.deepToString(matrix.solveMatrixByGaussianElimination()));

    }
}

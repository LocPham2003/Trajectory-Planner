import Matrices.Matrix;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Matrix matrix = new Matrix();

         matrix.generateMatrix("coefficients.txt");

        System.out.println(Arrays.deepToString(matrix.getSampleMatrix()));

        System.out.println(Arrays.deepToString(matrix.solveMatrixByGaussianElimination()));

    }
}

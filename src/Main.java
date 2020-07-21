import Matrices.Matrix;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Matrix matrix = new Matrix();

        matrix.generateMatrix("leftSideValues.txt", "rightSideValues.txt");

        System.out.println(Arrays.deepToString(matrix.getLeftSideValues()));

        System.out.println(Arrays.deepToString(matrix.solveMatrixByGaussianElimination()));

    }
}

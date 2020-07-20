package Matrices;

import java.util.ArrayList;
import java.util.Arrays;

public class Matrix {
    double[][] sampleMatrix = new double[0][0];

    public void generateMatrix(ArrayList<Double> values, int width, int height){
        this.sampleMatrix = new double[height][width];

        for (int i = 0; i < height; i++){
            for (int k = 0; k < width; k++) {
                this.sampleMatrix[i][k] = values.get(i * width + k);
            }
        }
    }

    private int recursion(int n){
        if (n == 0){
            return 0;
        }

        if (n == 1){
            return 1;
        }

        return recursion(n - 1) + recursion(n - 2);
    }

    public double[][] getSampleMatrix() {
        return sampleMatrix;
    }

    private boolean rowShuffle(int rowToSwitch){
        double[] row = this.sampleMatrix[rowToSwitch];

        int i;

       for (i = rowToSwitch; i < this.sampleMatrix.length; i++){
          if (this.sampleMatrix[i][rowToSwitch] != 0){
              this.sampleMatrix[rowToSwitch] = this.sampleMatrix[i];
              this.sampleMatrix[i] = row;
                break;
          }
       }

       return i == this.sampleMatrix.length;
    }

    public double[][]  solveMatrixByGaussianElimination(){
         int identifier = 0;
            while (identifier < this.sampleMatrix.length) {

                boolean newRowFound = false;

                for (int i = identifier + 1; i < this.sampleMatrix.length; i++){

                    if (this.sampleMatrix[identifier][identifier] == 0){
                        newRowFound = rowShuffle(identifier);
                    }
                    if (!newRowFound) {
                        double constant = this.sampleMatrix[i][identifier] / this.sampleMatrix[identifier][identifier];
                        for (int k = identifier; k < this.sampleMatrix[i].length; k++){

                            //double newNumber = Double.parseDouble(String.format("%.3g%n");

                            this.sampleMatrix[i][k] = (this.sampleMatrix[i][k] - this.sampleMatrix[identifier][k] * constant) / 100 * 100;
                        }
                    }

                }
                identifier += 1;
            }

        return this.sampleMatrix;
    }
}

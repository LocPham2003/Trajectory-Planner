package Matrices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Scanner;

public class Matrix {

    private double[][] leftSideValues = new double[0][0];
    private double[] rightSideValues;

    public void generateMatrix(String coefficientsFile, String constantFile){
        ArrayList<Double> values = new ArrayList<>();

        String[] listOfLeftSideValues = {};

        try (BufferedReader br = new BufferedReader(new FileReader(coefficientsFile))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            listOfLeftSideValues = everything.split(",");
        } catch (IOException e){
            e.printStackTrace();
        }

        for (String listOfValue : listOfLeftSideValues) {
            values.add(Double.parseDouble(listOfValue));
        }

        try {
           File file = new File(constantFile);
            Scanner scanner = new Scanner(file);
            String[] listOfRightSideValues = scanner.next().split(",");

            this.rightSideValues = new double[listOfRightSideValues.length];

            for (int i = 0; i < listOfRightSideValues.length; i++){
                this.rightSideValues[i] =  Double.parseDouble(listOfRightSideValues[i]);
            }

        } catch (IOException e){
            e.printStackTrace();
        }

        this.leftSideValues = new double[(int) Math.sqrt(values.size())][(int) Math.sqrt(values.size())];

        for (int i = 0; i < this.leftSideValues.length; i++){
            for (int k = 0; k < this.leftSideValues[i].length; k++) {
                this.leftSideValues[i][k] = values.get(i * this.leftSideValues[i].length + k);
            }
        }

    }

    public double[][] getLeftSideValues() {
        return leftSideValues;
    }

    public double[] getRightSideValues() { return rightSideValues; }

    private boolean rowShuffle(int rowToSwitch){
        double[] row = this.leftSideValues[rowToSwitch];
        double constant = this.rightSideValues[rowToSwitch];
        int i;

       for (i = rowToSwitch; i < this.leftSideValues.length; i++){
          if (this.leftSideValues[i][rowToSwitch] != 0){

              this.leftSideValues[rowToSwitch] = this.leftSideValues[i];
              this.leftSideValues[i] = row;

              this.rightSideValues[rowToSwitch] = this.rightSideValues[i];
              this.rightSideValues[i] = constant;

              break;
          }
       }

       return i == this.leftSideValues.length;
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public ArrayList<Double>  solveMatrixByGaussianElimination(){
        ArrayList<Double> solvedCoefficients = new ArrayList<>();

        int identifier = 0;

        //Row echelon form
        while (identifier < this.leftSideValues.length) {

                boolean newRowFound = false;

                for (int i = identifier + 1; i < this.leftSideValues.length; i++){

                    if (this.leftSideValues[identifier][identifier] == 0){
                        newRowFound = rowShuffle(identifier);
                    }
                    if (!newRowFound) {
                        double constant = this.leftSideValues[i][identifier] / this.leftSideValues[identifier][identifier];
                        for (int k = identifier; k < this.leftSideValues[i].length; k++){

                            this.leftSideValues[i][k] = this.leftSideValues[i][k] - this.leftSideValues[identifier][k] * constant;
                        }
                        this.rightSideValues[i] = (this.rightSideValues[i] - this.rightSideValues[identifier] * constant);
                    }

                }
                identifier += 1;
            }

        //Reverse Row echelon form
        identifier = this.leftSideValues.length - 1;
        while (identifier >= 0){

            for (int i = identifier - 1; i >= 0; i--){
                double constant = this.leftSideValues[i][identifier] / this.leftSideValues[identifier][identifier];

                this.leftSideValues[i][identifier] = this.leftSideValues[i][identifier] - this.leftSideValues[identifier][identifier] * constant;

                this.rightSideValues[i] = this.rightSideValues[i] - this.rightSideValues[identifier] * constant;

            }

            identifier--;
        }

        //Begin solving for coefficients
         for (identifier = 0; identifier < this.leftSideValues.length; identifier++){
             solvedCoefficients.add(round(this.rightSideValues[identifier] / this.leftSideValues[identifier][identifier],3));
         }

        return solvedCoefficients;
    }
}

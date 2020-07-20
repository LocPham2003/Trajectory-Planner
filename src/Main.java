import Matrices.Matrix;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        ArrayList<Double> values = new ArrayList<>();

        String[] listOfValues;

        try (BufferedReader br = new BufferedReader(new FileReader("inputValues.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            listOfValues = everything.split(",");
        }

        for (String listOfValue : listOfValues) {
            values.add(Double.parseDouble(listOfValue));
        }

        Matrix matrix = new Matrix();

         matrix.generateMatrix(values, (int) Math.sqrt(listOfValues.length), (int) Math.sqrt(listOfValues.length));

        System.out.println(Arrays.deepToString(matrix.getSampleMatrix()));

        System.out.println(Arrays.deepToString(matrix.solveMatrixByGaussianElimination()));

    }
}

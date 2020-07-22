package Splines;


import com.company.Constants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class NaturalCubicSpline {
    ArrayList<Point> listOfPoints = new ArrayList<>();

    int numberOfUnknownCoefficients = 0;

    private void getListOfPoints() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(Constants.listOfPointsFile));

            StringBuilder sb = new StringBuilder();
            String line = bufferedReader.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = bufferedReader.readLine();
            }

            String data = sb.toString();
            String[] listOfPoints = data.split("\\r?\\n");

            for (String i : listOfPoints) {
                String[] pointRawContent = i.split(",");

                Point point = new Point();
                point.x = Double.parseDouble(pointRawContent[0]);
                point.y = Double.parseDouble(pointRawContent[1]);

                this.listOfPoints.add(point);
            }

            this.numberOfUnknownCoefficients = (this.listOfPoints.size() - 1) * 4;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void generateMatrixValues(ArrayList<ArrayList<String>> leftSide, ArrayList<String> rightSide) {
        try {
            FileWriter leftSideWriter = new FileWriter(Constants.leftSideValuesFile);
            FileWriter rightSideWriter = new FileWriter(Constants.rightSideValuesFile);

            StringBuilder leftSideData = new StringBuilder();

            for (int i = 0; i < leftSide.size(); i++){
                for (int k = 0; k < leftSide.get(i).size(); k++){
                    leftSideData.append(leftSide.get(i).get(k));
                    if (i != leftSide.size() && k != leftSide.get(i).size()){
                        leftSideData.append(",");
                    }
                }
                leftSideData.append("\n");
            }

            leftSideWriter.write(leftSideData.toString().substring(0, leftSideData.toString().length() - 2));
            leftSideWriter.close();

            StringBuilder rightSideData = new StringBuilder();
            for (String i : rightSide){
                rightSideData.append(i);
                if (rightSide.indexOf(i) != rightSide.size() - 1) {
                    rightSideData.append(",");
                }
            }

            rightSideWriter.write(rightSideData.toString());
            rightSideWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void interpolate() {
        getListOfPoints();
        polynomialsCoefficients();
    }

    private void polynomialsCoefficients() {
        //ax^3 + bx^2 + cx + d = y
        //a1x^3 + b1x^2 + c1x^2 + d1 = y

        ArrayList<ArrayList<String>> listOfCoefficients = new ArrayList<>();

        ArrayList<String> listOfUnknownCoefficients = new ArrayList<>();
        ArrayList<String> listOfRightSideValues = new ArrayList<>();

        for (int i = 0; i < this.numberOfUnknownCoefficients; i++) {
            listOfUnknownCoefficients.add("0");
        }

        int i = 0;
        int pointIndex = 0;

        while (i < this.listOfPoints.size() - 1) {

            for (int k = 0; k < 2; k++) {

                for (int j = 0; j < 4; j++) {
                    switch (j) {
                        case 0 -> listOfUnknownCoefficients.set(i * 4 + j, Double.toString(Math.pow(this.listOfPoints.get(pointIndex).x, 3)));
                        case 1 -> listOfUnknownCoefficients.set(i * 4 + j, Double.toString(Math.pow(this.listOfPoints.get(pointIndex).x, 2)));
                        case 2 -> listOfUnknownCoefficients.set(i * 4 + j, Double.toString(Math.pow(this.listOfPoints.get(pointIndex).x, 1)));
                        case 3 -> listOfUnknownCoefficients.set(i * 4 + j, Double.toString(Math.pow(this.listOfPoints.get(pointIndex).x, 0)));
                    }
                }

                listOfRightSideValues.add(Double.toString(this.listOfPoints.get(pointIndex).y));

                listOfCoefficients.add(listOfUnknownCoefficients);

                generateMatrixValues(listOfCoefficients, listOfRightSideValues);

                if (k == 0) {
                    pointIndex++;
                }

                for (int o = 0; o < listOfUnknownCoefficients.size(); o++) {
                    listOfUnknownCoefficients.set(o, "0");
                }

            }

            i++;

        }

    }

    private void firstDerivative(double x, double y) {

    }

    private void secondDerivative(double x, double y) {

    }

    private void boundaryCondition(double x, double y) {

    }
}

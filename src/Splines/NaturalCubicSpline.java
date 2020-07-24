package Splines;


import com.company.Constants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class NaturalCubicSpline {
    private ArrayList<Point> listOfPoints = new ArrayList<>();
    private ArrayList<Point> listOfConnectingPoints = new ArrayList<>();

    private StringBuilder leftSideData = new StringBuilder();
    private StringBuilder rightSideData = new StringBuilder();

    private int numberOfUnknownCoefficients = 0;

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

    private void generateMatrixValues(ArrayList<String> leftSide, String rightSide) {
        try {
            FileWriter leftSideWriter = new FileWriter(Constants.leftSideValuesFile);
            FileWriter rightSideWriter = new FileWriter(Constants.rightSideValuesFile);

            //System.out.println(leftSide);

            for (String i : leftSide) {
                this.leftSideData.append(i).append(",");
            }

            leftSideData.append("\n");

            leftSideWriter.write(leftSideData.toString().substring(0, leftSideData.toString().length() - 2));
            leftSideWriter.close();

            this.rightSideData.append(rightSide).append(",");

            rightSideWriter.write(rightSideData.toString());
            rightSideWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void interpolate() {
        getListOfPoints();
        searchForConnectingPoints();

        polynomialsCoefficients();
        firstDerivative();
        secondDerivative();
        boundaryCondition();
    }

    private void polynomialsCoefficients() {
        //ax^3 + bx^2 + cx + d = y
        //a1x^3 + b1x^2 + c1x^2 + d1 = y

        ArrayList<String> listOfUnknownCoefficients = new ArrayList<>();

        for (int i = 0; i < this.numberOfUnknownCoefficients; i++) {
            listOfUnknownCoefficients.add("0");
        }

        int i = 0;
        int pointIndex = 0;

        while (i < this.listOfPoints.size() - 1) {

            for (int k = 0; k < 2; k++) {

                for (int j = 0; j < 4; j++) {
                    switch (j) {
                        case 0:
                            listOfUnknownCoefficients.set(i * 4 + j, Double.toString(Math.pow(this.listOfPoints.get(pointIndex).x, 3)));
                            break;
                        case 1:
                            listOfUnknownCoefficients.set(i * 4 + j, Double.toString(Math.pow(this.listOfPoints.get(pointIndex).x, 2)));
                            break;
                        case 2:
                            listOfUnknownCoefficients.set(i * 4 + j, Double.toString(Math.pow(this.listOfPoints.get(pointIndex).x, 1)));
                            break;
                        case 3:
                            listOfUnknownCoefficients.set(i * 4 + j, Double.toString(Math.pow(this.listOfPoints.get(pointIndex).x, 0)));
                            break;
                    }
                }

                generateMatrixValues(listOfUnknownCoefficients, Double.toString(this.listOfPoints.get(pointIndex).y));


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

    private void searchForConnectingPoints() {
        ArrayList<Point> connectingPoints = new ArrayList<>();

        for (int i = 0; i < this.listOfPoints.size(); i++) {
            try {
                if (this.listOfPoints.get(i - 1) != null && this.listOfPoints.get(i + 1) != null) {
                    connectingPoints.add(this.listOfPoints.get(i));
                }

            } catch (IndexOutOfBoundsException ignored) {

            }
        }


        this.listOfConnectingPoints = connectingPoints;
    }

    private void firstDerivative() {
        //Search only for the middle point

        ArrayList<String> FirstDerivativeUnknownCoefficients = new ArrayList<>();

        for (Point i : this.listOfConnectingPoints) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 3; k++) {
                    double isNegative = 1;

                    if (j == 1) {
                        isNegative = -1;
                    }

                    switch (k) {
                        case 0:
                            FirstDerivativeUnknownCoefficients.add(Double.toString(3 * Math.pow(i.x, 2) * isNegative));
                            break;
                        case 1:
                            FirstDerivativeUnknownCoefficients.add(Double.toString(2 * i.x * isNegative));
                            break;
                        case 2:
                            FirstDerivativeUnknownCoefficients.add(Double.toString(isNegative));
                            break;
                    }
                }

                FirstDerivativeUnknownCoefficients.add("0");

            }

            generateMatrixValues(FirstDerivativeUnknownCoefficients, "0");

        }

    }

    private void secondDerivative() {
        ArrayList<String> SecondDerivativeUnknownCoefficients = new ArrayList<>();

        for (Point i : this.listOfConnectingPoints) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    double isNegative = 1;

                    if (j == 1) {
                        isNegative = -1;
                    }

                    switch (k) {
                        case 0:
                            SecondDerivativeUnknownCoefficients.add(Double.toString(6 * i.x * isNegative));
                            break;
                        case 1:
                            SecondDerivativeUnknownCoefficients.add(Double.toString(2 * isNegative));
                            break;
                    }
                }

                SecondDerivativeUnknownCoefficients.add("0");
                SecondDerivativeUnknownCoefficients.add("0");

            }

            generateMatrixValues(SecondDerivativeUnknownCoefficients, "0");

        }
    }

    private void boundaryCondition() {
        //6ax + 2b = y
        //6axn + 2b = yn

        int pointToUse = 0;

        for (int i = 0; i < 2; i++){
            ArrayList<String> boundaryConditionCoefficients = new ArrayList<>();

            if (pointToUse == 0){
                boundaryConditionCoefficients.add(Double.toString(6 * this.listOfPoints.get(0).x));
                boundaryConditionCoefficients.add("2");

                for (int k = 0; k < 5; k++){
                    boundaryConditionCoefficients.add("0");
                }

            } else {
                for (int k = 0; k < 4; k++){
                    boundaryConditionCoefficients.add("0");
                }
                boundaryConditionCoefficients.add(Double.toString(6 * this.listOfPoints.get(this.listOfPoints.size() - 1).x));
                boundaryConditionCoefficients.add("2");
                boundaryConditionCoefficients.add("0");
                boundaryConditionCoefficients.add("0");
            }

            generateMatrixValues(boundaryConditionCoefficients, Double.toString(this.listOfPoints.get(pointToUse).y));

            pointToUse = this.listOfPoints.size() - 1;

        }

    }
}

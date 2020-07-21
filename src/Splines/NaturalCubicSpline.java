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
    private void getListOfPoints(){
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

            for (String i : listOfPoints){
                String[] pointRawContent = i.split(",");

                Point point = new Point();
                point.x = Double.parseDouble(pointRawContent[0]);
                point.y = Double.parseDouble(pointRawContent[1]);

                this.listOfPoints.add(point);
            }

            this.numberOfUnknownCoefficients = (this.listOfPoints.size() - 1) * 4;

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private void generateMatrixValues(String leftSide, String rightSide){
        try {
            FileWriter leftSideWriter = new FileWriter(Constants.leftSideValuesFile);
            FileWriter rightSideWriter = new FileWriter(Constants.rightSideValuesFile);

            leftSideWriter.write(leftSide);
            leftSideWriter.close();

            rightSideWriter.write(rightSide);
            rightSideWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void interpolate(){
        getListOfPoints();


        polynomialsCoefficients();
    }

    private void polynomialsCoefficients(){
        //ax^3 + bx^2 + cx + d = y
        //a1x^3 + b1x^2 + c1x^2 + d1 = y

        ArrayList<String> listOfUnknownCoefficients = new ArrayList<>();

        for (int i = 0; i < this.numberOfUnknownCoefficients; i++){
            listOfUnknownCoefficients.add("0");
        }

        for (int i = 0; i < this.listOfPoints.size(); i++){
            for (int k = 0; k < 4; k++){
                switch (k) {
                    case 0 -> listOfUnknownCoefficients.set(i * 4 + k, Double.toString(Math.pow(this.listOfPoints.get(i).x, 3)));
                    case 1 -> listOfUnknownCoefficients.set(i * 4 + k, Double.toString(Math.pow(this.listOfPoints.get(i).x, 2)));
                    case 2 -> listOfUnknownCoefficients.set(i * 4 + k, Double.toString(Math.pow(this.listOfPoints.get(i).x, 1)));
                    case 3 -> listOfUnknownCoefficients.set(i * 4 + k, Double.toString(Math.pow(this.listOfPoints.get(i).x, 0)));
                }
            }

            generateMatrixValues(listOfUnknownCoefficients.toString(), Double.toString(this.listOfPoints.get(i).y));

            for (int k = 0; k < 4; k++){
                listOfUnknownCoefficients.set(i * 4 + k, "0");
            }
        }



    }

    private void firstDerivative(double x, double y){

    }

    private void secondDerivative(double x, double y){

    }

    private void boundaryCondition(double x, double y){

    }
}

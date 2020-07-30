package Graphing;

import Matrices.Matrix;
import Splines.NaturalCubicSpline;
import Splines.Point;
import Splines.SplineFunctions;
import com.company.Constants;
import com.company.SortByX;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Graph extends JFrame{
   private ArrayList<Point> listOfInterpolatedPoints = new ArrayList<>();
   private ArrayList<SplineFunctions> listOfFunctions = new ArrayList<>();
   private ArrayList<Point> listOfUnsortedPoints = new ArrayList<>();

   private ArrayList<Point> splinePoints = new ArrayList<>();


   public Graph(){
      setTitle("c   u   r   v   y   b   o   i   s");

      mouseEvents();
      keyEvents();

      setSize(700, 700);

      setResizable(true);
      setVisible(true);
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

   }

   public void paint(Graphics g){
      g.clearRect(0, 0, this.getWidth(), this.getHeight());

      g.drawLine(this.getWidth() / 2, (int) (this.getHeight() * 0.01),this.getWidth() / 2,(int) (this.getHeight() * 0.98));
      g.drawLine((int) (this.getWidth() * 0.01),this.getHeight() / 2, (int) (this.getWidth() * 0.98),this.getHeight() / 2);

       g.setColor(Color.red);

       for (Point i : this.listOfInterpolatedPoints){
           g.fillOval((int) i.getX(), (int) i.getY(), 5,5);
           g.drawString("X : " + i.getX() + " Y : " + i.getY(), (int) (i.getX() + 5), (int) (i.getY() + 5));
       }

       g.setColor(Color.blue);

       for (Point i : this.splinePoints){
           g.fillOval((int) (i.getX()), (int) (i.getY()), 3,3);
       }

   }

   private void mouseEvents(){
      addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent me) {
            listOfFunctions.clear();

            Point newPoint = new Point();
            newPoint.setX((double) me.getX());
            newPoint.setY((double) me.getY());

            listOfInterpolatedPoints.add(newPoint);

            listOfUnsortedPoints.add(newPoint);

            repaint();

            generatePoints();
            calculateSpline();
            graphSpline();

         }

      });
   }

   private void keyEvents(){
      addKeyListener(new KeyAdapter() {
         @Override
         public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() == 'u'){
                if (listOfInterpolatedPoints.size() > 1){

                    listOfInterpolatedPoints.remove(listOfUnsortedPoints.get(listOfUnsortedPoints.size() - 1));

                    listOfUnsortedPoints.remove(listOfUnsortedPoints.size() - 1);

                    generatePoints();
                    calculateSpline();
                    graphSpline();

                } else if (listOfInterpolatedPoints.size() == 1){
                    listOfInterpolatedPoints.remove(0);

                    repaint();
                }
            }
         }
      });
   }

    //Get mouse coordinates to calculate spline functions
    private void generatePoints(){
        try{
            FileWriter fileWriter = new FileWriter("Points.txt");

            StringBuilder stringBuilder = new StringBuilder();

            this.listOfInterpolatedPoints.sort(new SortByX());

            for (Point i : this.listOfInterpolatedPoints){
                stringBuilder.append(i.getX()).append(",").append(i.getY()).append("\n");
            }

            //

            fileWriter.write(stringBuilder.toString());
            fileWriter.close();

        } catch (IOException e){
            e.printStackTrace();
        }

    }

   //Generate spline graph
   private void calculateSpline() {
      NaturalCubicSpline naturalCubicSpline = new NaturalCubicSpline();
      Matrix matrix = new Matrix();

      this.listOfFunctions.clear();

      ArrayList<Double> listOfCoefficients;

      if (naturalCubicSpline.interpolate()) {
         matrix.generateMatrix(Constants.leftSideValuesFile, Constants.rightSideValuesFile);
      }

      listOfCoefficients = matrix.solveMatrixByGaussianElimination();

      for (int k = 0; k < naturalCubicSpline.getNumberOfSplinesRequired(); k++) {
         SplineFunctions splineFunction = new SplineFunctions();

         for (int i = 0; i < 4; i++) {

            double coefficient = listOfCoefficients.get(k * 4 + i);

            switch (i) {
               case 0:
                  splineFunction.setA(coefficient);
                  break;
               case 1:
                  splineFunction.setB(coefficient);
                  break;
               case 2:
                  splineFunction.setC(coefficient);
                  break;
               case 3:
                  splineFunction.setD(coefficient);
                  break;

            }

         }

         this.listOfFunctions.add(splineFunction);

      }

   }

   private void graphSpline(){
      int currentPoint = 0;
      this.splinePoints.clear();

      if (this.listOfInterpolatedPoints.size() >= 2){
         while (currentPoint < this.listOfInterpolatedPoints.size() - 1){
            for (double x = (this.listOfInterpolatedPoints.get(currentPoint).getX()); x <= this.listOfInterpolatedPoints.get(currentPoint + 1).getX(); x += 0.05){
               double y = (this.listOfFunctions.get(currentPoint).getA() * Math.pow(x ,3) +
                       this.listOfFunctions.get(currentPoint).getB() * Math.pow(x, 2) +
                       this.listOfFunctions.get(currentPoint).getC() * x +
                       this.listOfFunctions.get(currentPoint).getD());

               Point point = new Point();
               point.setY(y);
               point.setX(x);

               this.splinePoints.add(point);

         }

            currentPoint++;

         }

      }

      repaint();

   }

}


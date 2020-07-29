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
   private int prevWidth = 800;
   private int prevHeight = 800;

   private int pointX, pointY;

   private ArrayList<Point> listOfPoints = new ArrayList<>();
   private ArrayList<SplineFunctions> listOfFunctions = new ArrayList<>();

   private ArrayList<Point> splinePoints = new ArrayList<>();

   private String coordinate;

   private boolean isNewPointAdded = false;
   private boolean isGraphingNewSpline = false;

   public Graph(){
      this.pointX = this.prevWidth / 2 - 2;
      this.pointY = this.prevHeight / 2 - 2;

      this.coordinate = "";

      setTitle("Trajectory");

      mouseEvents();
      keyEvents();

      setSize(this.prevWidth, this.prevHeight);

      setResizable(true);
      setVisible(true);
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

   }

   public void paint(Graphics g){
      g.clearRect(0, 0, this.getWidth(), this.getHeight());

      if (this.getHeight() != this.prevHeight || this.getWidth() != this.prevWidth){
         g.clearRect(this.prevWidth / 2, (int) (this.prevHeight * 0.01),this.prevWidth / 2,(int) (this.prevHeight * 0.98));
         g.clearRect((int) (this.prevWidth * 0.01),this.prevHeight / 2, (int) (this.prevWidth * 0.98),this.prevHeight / 2);
      }

      g.drawLine(this.getWidth() / 2, (int) (this.getHeight() * 0.01),this.getWidth() / 2,(int) (this.getHeight() * 0.98));
      g.drawLine((int) (this.getWidth() * 0.01),this.getHeight() / 2, (int) (this.getWidth() * 0.98),this.getHeight() / 2);

      g.setColor(Color.blue);

      g.fillOval(this.pointX, this.pointY, 3,3);

      if (this.isNewPointAdded){
         for (Point i : this.splinePoints){
         }

         this.isNewPointAdded = false;
      }

      if (this.isGraphingNewSpline){
         for (Point i : this.splinePoints){
            g.fillOval((int) (i.getX()), (int) (i.getY()), 3,3);
         }

         this.isGraphingNewSpline = false;
      }


      g.drawString(this.coordinate, pointX + 5, pointY + 5);

      this.prevWidth = this.getWidth();
      this.prevHeight = this.getHeight();

   }

   private void mouseEvents(){
      addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent me) {
            listOfFunctions.clear();

            isNewPointAdded = true;

            coordinate = "X : " + me.getX() + " Y : " + me.getY();
            pointX = me.getX();
            pointY = me.getY();

            Point newPoint = new Point();
            newPoint.setX((double) pointX);
            newPoint.setY((double) pointY);

            listOfPoints.add(newPoint);

            repaint();

            generatePoints();
            calculateSpline();

         }

      });
   }

   private void keyEvents(){
      addKeyListener(new KeyAdapter() {
         @Override
         public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() == 'u'){
               System.out.println("Hi");
            }
         }
      });
   }

   //Generate spline graph
   private void calculateSpline() {
      NaturalCubicSpline naturalCubicSpline = new NaturalCubicSpline();
      Matrix matrix = new Matrix();

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

     graphSpline();

   }

   private void graphSpline(){
      int currentPoint = 0;
      this.splinePoints.clear();
      this.isGraphingNewSpline = true;

      if (this.listOfPoints.size() >= 2){
         while (currentPoint < this.listOfPoints.size() - 1){
            for (double x = (this.listOfPoints.get(currentPoint).getX()); x <= this.listOfPoints.get(currentPoint + 1).getX(); x += 0.1){
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

   private void generatePoints(){
      try{
         FileWriter fileWriter = new FileWriter("Points.txt");

         StringBuilder stringBuilder = new StringBuilder();

         this.listOfPoints.sort(new SortByX());

         for (Point i : this.listOfPoints){
            stringBuilder.append(i.getX()).append(",").append(i.getY()).append("\n");
         }

         //

         fileWriter.write(stringBuilder.toString());
         fileWriter.close();

      } catch (IOException e){
         e.printStackTrace();
      }

   }
}


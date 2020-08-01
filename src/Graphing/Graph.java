    package Graphing;

    import Matrices.Matrix;
    import Splines.NaturalCubicSpline;
    import Splines.SplineFunctions;
    import com.company.Constants;
    import com.company.SortByX;

    import javax.swing.*;
    import javax.swing.border.Border;
    import java.awt.*;
    import java.awt.event.*;
    import java.io.FileWriter;
    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.Random;

    public class Graph extends JFrame implements ActionListener{
       private ArrayList<Point> listOfInterpolatedPoints = new ArrayList<>();
       private ArrayList<SplineFunctions> listOfFunctions = new ArrayList<>();
       private ArrayList<Point> listOfUnsortedPoints = new ArrayList<>();

       private ArrayList<Point> splinePoints = new ArrayList<>();

       private int prevWidth = 700;
       private int prevHeight = 700;

       private int time = 0;
       private Timer timer = new Timer(1,this);

        public Graph(){
          setTitle("Trajectory");
          setSize(prevWidth, prevHeight);

          setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

          configureButtons();
          mouseEvents();
          keyEvents();

          setResizable(true);
          setVisible(true);

       }

        public void paint(Graphics g){
           if (!this.timer.isRunning()){

           g.clearRect(0,0,this.getWidth(), this.getHeight());

           g.drawLine(this.getWidth() / 2, (int) (this.getHeight() * 0.01),this.getWidth() / 2,(int) (this.getHeight() * 0.98));
           g.drawLine((int) (this.getWidth() * 0.01),this.getHeight() / 2, (int) (this.getWidth() * 0.98),this.getHeight() / 2);

           int widthChange = this.getWidth() - this.prevWidth;
           int heightChange = this.getHeight() - this.prevHeight;

           g.setColor(Color.blue);

           for (Point i : this.splinePoints){
               i.setX(i.getX() + widthChange);
               i.setY(i.getY() + heightChange);
               g.fillOval((int) (i.getX()), (int) (i.getY()), 25,25);
           }

           for (Point i : this.listOfInterpolatedPoints){
               i.draw(g);
           }

           if (this.splinePoints.size() >= 1) {
               g.setColor(Color.cyan);
               g.fillOval((int) (this.splinePoints.get(time).getX()), (int) (this.splinePoints.get(time).getY()), 25,25);
           }

           } else {
               g.clearRect(0,0,this.getWidth(), this.getHeight());

               g.drawLine(this.getWidth() / 2, (int) (this.getHeight() * 0.01),this.getWidth() / 2,(int) (this.getHeight() * 0.98));
               g.drawLine((int) (this.getWidth() * 0.01),this.getHeight() / 2, (int) (this.getWidth() * 0.98),this.getHeight() / 2);

               g.setColor(Color.cyan);
               g.fillOval((int) (this.splinePoints.get(time).getX()), (int) (this.splinePoints.get(time).getY()), 25,25);

           }

           this.prevWidth = this.getWidth();
           this.prevHeight = this.getHeight();

       }

        private void configureButtons() {
            JButton button = new JButton("Add new Point");
            JPanel controlPanel = new JPanel();

            button.setFocusable(false);
            button.setVisible(true);

            controlPanel.add(button);

            add(controlPanel,BorderLayout.SOUTH);

            button.addActionListener(e -> {
                listOfFunctions.clear();

                double pointRadius = 25.0;

                Point newPoint = new Point();
                newPoint.setX((double) getWidth() / 2);
                newPoint.setY((double) getHeight() / 2);
                newPoint.setRadius(pointRadius);

                //check that we dont go offscreen by subtarcting its radius unless its x and y are not bigger than radius

                button.setVisible(true);

                listOfInterpolatedPoints.add(newPoint);//add ball to panel to be drawn

                listOfUnsortedPoints.add(newPoint);

                generatePoints();
                calculateSpline();
                graphSpline();
            });
        }

       private void mouseEvents(){
          addMouseListener(new MouseAdapter() {
             public void mouseClicked(MouseEvent me) {

                 for (Point p : listOfInterpolatedPoints) {//iterate through each points
                     if (p.getBounds().contains(me.getPoint())) {//get the point bounds and check if mouse click was within its bounds
                         if (!p.isSelected()) {//check if ball has been clicked on
                             p.setSelected(true);
                         } else {
                             p.setSelected(false);
                         }
                         repaint(); //so point color change will be shown
                     }
                 }



             }

          });


       }

       private void keyEvents(){
          this.addKeyListener(new KeyAdapter() {
             @Override
             public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 'u'){
                    if (listOfInterpolatedPoints.size() > 1){

                        System.out.println("hI");

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

                if (e.getKeyChar() == 'p'){
                    if (!timer.isRunning()){
                        timer.start();
                    } else {
                        timer.stop();
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

        @Override
        public void actionPerformed(ActionEvent e) {
            if (time == this.splinePoints.size() - 1){
                timer.stop();
                time = 0;
                repaint();
            } else {
                time++;
                repaint();
            }
        }
    }


package Graphing;

import Matrices.Matrix;
import Splines.NaturalCubicSpline;
import Splines.SplineFunctions;
import com.company.Constants;
import com.company.SortByX;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DrawPanel extends JPanel implements ActionListener {
    private ArrayList<Point> listOfInterpolatedPoints = new ArrayList<>();
    private ArrayList<SplineFunctions> listOfFunctions = new ArrayList<>();
    private ArrayList<Point> listOfUnsortedPoints = new ArrayList<>();
    private ArrayList<Point> splinePoints = new ArrayList<>();

    private Point rootPoint = new Point();

    private JFrame mainView = new JFrame();

    private int prevWidth = 1200;
    private int prevHeight = 700;

    private int time = 0;
    private Timer timer = new Timer(Constants.timerDelay, this);


    void configurePanel(JFrame frame) {
        this.mainView = frame;
        mouseEvents();
        keyEvents();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(prevWidth, prevHeight);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!this.timer.isRunning()) {

            g.clearRect(0, 0, this.getWidth(), this.getHeight());

            try {
                final BufferedImage image = ImageIO.read(new File("frcField.png"));
                Image newImage = image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT);
                g.drawImage(newImage,0,0,null);

            } catch (Exception e){
                System.out.println("bro");
            }

            for (Point i : this.splinePoints) {
                i.setX(i.getX());
                i.setY(i.getY());
                g.fillOval((int) (i.getX()), (int) (i.getY()), 25, 25);
            }

            for (Point i : this.listOfInterpolatedPoints) {
                i.setX(i.getX());
                i.setY(i.getY());
                i.draw(g);
            }

            if (this.splinePoints.size() >= 1) {
                g.setColor(Color.cyan);
                g.fillOval((int) (this.splinePoints.get(time).getX()), (int) (this.splinePoints.get(time).getY()), 25, 25);
            }

        } else {
            try {
                final BufferedImage image = ImageIO.read(new File("frcField.png"));
                Image newImage = image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT);
                g.drawImage(newImage,0,0,null);

            } catch (Exception e){
                System.out.println("bro");
            }
            g.setColor(Color.cyan);
            g.fillOval((int) (this.splinePoints.get(time).getX()), (int) (this.splinePoints.get(time).getY()), 25, 25);
        }

    }

    private void resetFocusedObject(){
        this.mainView.setFocusable(true);
        this.mainView.requestFocusInWindow();
        this.mainView.requestFocus();
    }

    private void keyEvents() {

        this.mainView.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {

                if (e.getKeyChar() == 'u') {
                    if (listOfInterpolatedPoints.size() >= 2){
                        undoAction();
                    }
                }
                if (e.getKeyChar() == 'p') {
                    startSimulation();
                }

                if (e.getKeyChar() == 'n'){
                    addNewPoint();
                }

                if (e.getKeyChar() == 'd'){
                    if (listOfInterpolatedPoints.size() >= 2){
                        deletePoint();
                    }
                }

            }
        });
    }

    void deletePoint(){
        resetFocusedObject();

        for (Point p : this.listOfInterpolatedPoints){
            if (p.isSelected()){
                this.listOfInterpolatedPoints.remove(p);
                this.listOfUnsortedPoints.remove(p);
            }
        }

        generatePoints();
        calculateSpline();
        graphSpline();

        repaint();

    }

    void startSimulation() {
        resetFocusedObject();

        if (!timer.isRunning()) {
            timer.start();
        } else {
            timer.stop();
            repaint();
        }
    }

    void undoAction() {
        resetFocusedObject();

        if (listOfInterpolatedPoints.size() > 1) {

            listOfInterpolatedPoints.remove(listOfUnsortedPoints.get(listOfUnsortedPoints.size() - 1));

            listOfUnsortedPoints.remove(listOfUnsortedPoints.size() - 1);

            generatePoints();
            calculateSpline();
            graphSpline();

        } else if (listOfInterpolatedPoints.size() == 1) {
            listOfInterpolatedPoints.remove(0);

            repaint();
        }
    }


    void addNewPoint() {
        resetFocusedObject();

        listOfFunctions.clear();

        double pointRadius = 25.0;

        Point newPoint = new Point();
        newPoint.setX((double) getWidth() / 2);
        newPoint.setY((double) getHeight() / 2);
        newPoint.setRadius(pointRadius);
        newPoint.setSelected(false);

        //check that we dont go offscreen by subtarcting its radius unless its x and y are not bigger than radius

        listOfInterpolatedPoints.add(newPoint);//add ball to panel to be drawn

        listOfUnsortedPoints.add(newPoint);

        generatePoints();
        calculateSpline();
        graphSpline();
    }

    private void mouseEvents() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (Point p : listOfInterpolatedPoints) {//iterate through each points
                    if (p.getBounds().contains(e.getPoint())) {//get the point bounds and check if mouse click was within its bounds
                        p.setSelected(true);
                        rootPoint.setX((double) e.getX());
                        rootPoint.setY((double) e.getY());
                        repaint(); //so point color change will be shown
                    }
                }

            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                for (Point p : listOfInterpolatedPoints) {//iterate through each points
                    if (p.getBounds().contains(e.getPoint())) {//get the point bounds and check if mouse click was within its bounds
                        p.setSelected(false);
                        repaint(); //so point color change will be shown
                    }
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Point p : listOfInterpolatedPoints) {//iterate through each points
                    if (p.getBounds().contains(e.getPoint())) {//get the point bounds and check if mouse click was within its bounds
                        if (p.isSelected()){
                            p.setSelected(false);
                        } else {
                            p.setSelected(true);
                        }
                        repaint(); //so point color change will be shown
                    }
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                double dx = e.getX() - rootPoint.getX();
                double dy = e.getY() - rootPoint.getY();

                for (Point p : listOfInterpolatedPoints) {//iterate through each points
                    if (p.getBounds().contains(e.getPoint())) {//get the point bounds and check if mouse click was within its bounds
                        p.setX(p.getX() + dx);
                        p.setY(p.getY() + dy);
                        repaint();
                    }
                }

                rootPoint.setX(rootPoint.getX() + dx);
                rootPoint.setY(rootPoint.getY() + dy);

                generatePoints();
                calculateSpline();
                graphSpline();
            }

        });
    }

    private void generatePoints() {
        try {
            FileWriter fileWriter = new FileWriter("Points.txt");

            StringBuilder stringBuilder = new StringBuilder();

            this.listOfInterpolatedPoints.sort(new SortByX());

            for (Point i : this.listOfInterpolatedPoints) {
                stringBuilder.append(i.getX()).append(",").append(i.getY()).append("\n");
            }

            fileWriter.write(stringBuilder.toString());
            fileWriter.close();

        } catch (IOException e) {
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

    private void graphSpline() {
        int currentPoint = 0;
        this.splinePoints.clear();

        if (this.listOfInterpolatedPoints.size() >= 2) {
            while (currentPoint < this.listOfInterpolatedPoints.size() - 1) {
                for (double x = (this.listOfInterpolatedPoints.get(currentPoint).getX()); x <= this.listOfInterpolatedPoints.get(currentPoint + 1).getX(); x += 0.1) {
                    double y = (this.listOfFunctions.get(currentPoint).getA() * Math.pow(x, 3) +
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
        if (time == this.splinePoints.size() - 1) {
            timer.stop();
            time = 0;
            repaint();
        } else {
            time++;
            repaint();
        }
    }
}

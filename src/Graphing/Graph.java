    package Graphing;

    import javax.swing.*;
    import java.awt.*;

    public class Graph {
       private final DrawPanel drawPanel = new DrawPanel();

        public Graph(){
            JFrame frame = new JFrame("Trajectory");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            drawPanel.configurePanel(frame);

            JPanel controlPanel = new JPanel();

            JButton createPoint = new JButton("Add point");
            createPoint.addActionListener(e -> drawPanel.addNewPoint());

            JButton undoPoint = new JButton("Undo");
            undoPoint.addActionListener(e -> drawPanel.undoAction());

            JButton startSimulation = new JButton("Start/Pause");
            startSimulation.addActionListener(e -> drawPanel.startSimulation());

            JButton deletePoint = new JButton("Delete");
            deletePoint.addActionListener(e -> drawPanel.deletePoint());

            controlPanel.add(deletePoint);
            controlPanel.add(createPoint);
            controlPanel.add(undoPoint);
            controlPanel.add(startSimulation);

            frame.add(drawPanel);
            frame.add(controlPanel, BorderLayout.SOUTH);

            frame.pack();
            frame.setResizable(false);
            frame.setVisible(true);
            frame.setFocusable(true);

       }

    }


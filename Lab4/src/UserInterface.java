package lab4;

import lab3.Function;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class UserInterface {

    private final static List<Function> equations = new LinkedList<>();

    static {
        /*
        Xo = 0 Yo = 1
        solution: y(x) = 1.5e^(2x) * x ^ 2 - x - 0.5
        */
        Function function1 = new FunctionAdapter() {
            @Override
            public double getValue(double x, double y) {
                return (2 * (x * x + y));
            }

            @Override
            public String toString() {
                return "dy/dx = 2(x ^ 2 + y)";
            }
        };
        Function function2 = new FunctionAdapter() {
            @Override
            public double getValue(double x, double y) {
                return 3 * x * x;
            }

            @Override
            public String toString() {
                return "dy/dx = 3x ^ 2";
            }
        };
        Function function3 = new FunctionAdapter() {
            @Override
            public double getValue(double x, double y) {
                return (2 * y + 1) * 1 / Math.tan(x);
            }

            @Override
            public String toString() {
                return "dy/dx = ctg(x) * (2y + 1)";
            }
        };
        equations.add(function1);
        equations.add(function2);
        equations.add(function3);
    }

    public void draw(int width, int height){
        SwingUtilities.invokeLater(() ->{
            JFrame mainFrame = getMainFrame(width, height);
            mainFrame.setResizable(false);
            mainFrame.setVisible(true);
        });
    }

    private JFrame getMainFrame(int width, int height){
        final String errorTitle = "Ошибка";

        JFrame mainFrame = new JFrame("Lab 4");
        mainFrame.setSize(width, height);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel(null);
        mainFrame.setContentPane(mainPanel);

        int  graphingHeight = height * 2 / 3;
        AtomicReference<JPanel> chartPanel = new AtomicReference<>(new JPanel());
        chartPanel.get().setSize(width, graphingHeight);
        chartPanel.get().setLocation(0, 0);
        mainPanel.add(chartPanel.get());

        JPanel controlPanel = new JPanel(new GridLayout(4, 1));
        mainPanel.add(controlPanel);
        controlPanel.setSize(width, height - graphingHeight);
        controlPanel.setLocation(0, graphingHeight);

        JPanel pickPanel = new JPanel();
        controlPanel.add(pickPanel);

        JLabel equationLabel = new JLabel("Выберите ДУ для решения:");
        pickPanel.add(equationLabel);

        String[] options = new String[equations.size()];
        for(int i = 0; i < options.length; i++)
            options[i] = equations.get(i).toString();
        JComboBox<String> equationBox = new JComboBox<>(options);
        pickPanel.add(equationBox);

        JPanel initPanel = new JPanel();
        controlPanel.add(initPanel);

        JLabel initLabel = new JLabel("Введите начальные условия:");
        initPanel.add(initLabel);

        JPanel inputPanel = new JPanel();
        initPanel.add(inputPanel);

        JLabel xLabel = new JLabel("Xo:");
        inputPanel.add(xLabel);

        JTextField xInit = new JTextField();
        xInit.setColumns(10);
        inputPanel.add(xInit);

        JLabel yLabel = new JLabel("Yo");
        inputPanel.add(yLabel);

        JTextField yInit = new JTextField();
        yInit.setColumns(10);
        inputPanel.add(yInit);

        JPanel accuracyPanel = new JPanel();
        controlPanel.add(accuracyPanel);

        JLabel endLabel = new JLabel("Введите конец отрезка:");
        accuracyPanel.add(endLabel);

        JTextField endField = new JTextField();
        endField.setColumns(5);
        accuracyPanel.add(endField);

        JLabel accuracyLabel = new JLabel("Введите точность:");
        accuracyPanel.add(accuracyLabel);

        JTextField accuracyField = new JTextField();
        accuracyField.setColumns(8);
        accuracyPanel.add(accuracyField);

        JPanel buttonPanel = new JPanel();
        controlPanel.add(buttonPanel);

        JButton solveButton = new JButton("Решить");
        solveButton.addActionListener(l -> {
            Function selectedFunction = equations.get(equationBox.getSelectedIndex());
            DifferentialEquation equation = new DifferentialEquation(selectedFunction);


            try {
                double x0;
                double y0;
                double endPoint;
                double accuracy;
                try{
                    x0 = Double.parseDouble(xInit.getText().replace(',', '.'));
                    y0 = Double.parseDouble(yInit.getText().replace(',', '.'));
                }
                catch (NumberFormatException e){
                    JOptionPane.showMessageDialog(mainFrame, "Задайте начальные условия числами",
                            errorTitle, JOptionPane.ERROR_MESSAGE);
                    throw e;
                }

                try{
                    endPoint = Double.parseDouble(endField.getText().replace(',', '.'));
                }
                catch (NumberFormatException e){
                    JOptionPane.showMessageDialog(mainFrame, "Задайте конец отрезка числом",
                            errorTitle, JOptionPane.ERROR_MESSAGE);
                    throw e;
                }

                try{
                    accuracy = Double.parseDouble(accuracyField.getText().replace(',', '.'));
                }
                catch (NumberFormatException e){
                    JOptionPane.showMessageDialog(mainFrame, "Задайте точность числом",
                            errorTitle, JOptionPane.ERROR_MESSAGE);
                    throw e;
                }

                if(Double.compare(x0, endPoint) == 0){
                    JOptionPane.showMessageDialog(mainFrame, "Конец отрезка не может совпадать с его началом",
                            errorTitle, JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException();
                }

                double[][] solution = equation.solve(x0, y0, endPoint, accuracy);
                Graphing graphing = new Graphing(solution[0], solution[1]);

                mainPanel.remove(chartPanel.get());
                chartPanel.set(graphing.getChart(width, graphingHeight));
                chartPanel.get().setLocation(0, 0);
                chartPanel.get().setSize(width, graphingHeight);
                mainPanel.add(chartPanel.get());
                mainPanel.revalidate();
                mainPanel.repaint();
            }
            catch (SolutionException e){
                JOptionPane.showMessageDialog(mainFrame, e.getMessage(),
                        errorTitle, JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        buttonPanel.add(solveButton);


        return mainFrame;
    }

}

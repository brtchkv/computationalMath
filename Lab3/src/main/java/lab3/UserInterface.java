package lab3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class UserInterface {

    private Function baseFunction;

    private Function interpolateFunction;

    private JPanel graphicPanel;

    private double[] xData;


    public UserInterface(Function baseFunction) {
        this.baseFunction = baseFunction;
    }

    public JFrame getMainFrame(int width, int height) {
        final int firstXValue = 4;
        final int xAmount = 5;
        final int graphicHeight = height * 7 / 10;
        final String errorTitle = "Ошибка";

        JFrame jFrame = new JFrame("Lab 3");
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setSize(width, height);
        JPanel mainPanel = new JPanel(null);
        jFrame.setContentPane(mainPanel);

        graphicPanel = new JPanel();
        graphicPanel.setLocation(0, 0);
        graphicPanel.setSize(width, graphicHeight);
        mainPanel.add(graphicPanel);

        JPanel controlPanel = new JPanel(new GridLayout(5, 1));
        controlPanel.setSize(width, height - graphicHeight);
        controlPanel.setLocation(0, graphicHeight);
        mainPanel.add(controlPanel);

        JPanel selectPanel = new JPanel();
        controlPanel.add(selectPanel);
        JLabel labelAmount = new JLabel("Выберите количество узлов интерполирования");
        selectPanel.add(labelAmount);

        JComboBox<Integer> selectedXAmount = new JComboBox<>();
        for (int i = firstXValue; i < firstXValue + xAmount; i++)
            selectedXAmount.addItem(i);
        selectPanel.add(selectedXAmount);

        AtomicReference<JPanel> argsPanel = new AtomicReference<>(generateButtons(4, jFrame));
        controlPanel.add(argsPanel.get());

        selectedXAmount.addActionListener(e -> {
            controlPanel.remove(argsPanel.get());
            argsPanel.set(generateButtons(selectedXAmount.getSelectedIndex() + firstXValue, jFrame));
            controlPanel.add(argsPanel.get(), 1);
            controlPanel.revalidate();
            controlPanel.repaint();
        });

        JPanel changePanel = new JPanel();
        controlPanel.add(changePanel);

        JLabel changeLabel = new JLabel("Введите узел, в котором нужно поменять значение функции:");
        changePanel.add(changeLabel);

        JTextField changeField = new JTextField(5);
        changePanel.add(changeField);

        JButton mainButton = new JButton("Интерполировать");
        mainButton.addActionListener(e -> {
            for (int i = 0; i < xAmount - 1; i++) {
                if (((JTextField) argsPanel.get().getComponent(i)).getText().equals("")) {
                    JOptionPane.showMessageDialog(jFrame, "Заполните все значения Х",
                            errorTitle, JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            double changeX;
            boolean changeMode = true;
            try {
                changeX = Double.parseDouble(changeField.getText().replace(',', '.'));
            } catch (NumberFormatException e1) {
                changeMode = false;
                changeX = 0;
            }
            if (changeMode) {
                double finalChangeX = changeX;
                if (changeX != 0 && Arrays.stream(xData).noneMatch(n -> Double.compare(n, finalChangeX) == 0)) {
                    JOptionPane.showMessageDialog(jFrame, "Точка, в которой нужно подменить " +
                            "значение функции должна быть узлом интерполяции", errorTitle, JOptionPane.ERROR_MESSAGE);
                } else {
                    mainPanel.remove(graphicPanel);
                    graphicPanel = getGraphicPanel(width, graphicHeight, changeX, changeMode);
                    mainPanel.add(graphicPanel);
                    mainPanel.revalidate();
                    mainPanel.repaint();
                }
            } else {
                mainPanel.remove(graphicPanel);
                graphicPanel = getGraphicPanel(width, graphicHeight, changeX, changeMode);
                mainPanel.add(graphicPanel);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
        controlPanel.add(mainButton);

        JPanel findValuePanel = new JPanel();
        controlPanel.add(findValuePanel);

        JLabel findValueLabel = new JLabel("Введите значение х, в котором нужно найти значение функции");
        findValuePanel.add(findValueLabel);

        JTextField findValueField = new JTextField(3);
        findValuePanel.add(findValueField);

        JLabel valueLabel = new JLabel(String.format("f(%s)=%s", "?", "?"));

        JButton findValueButton = new JButton("Найти");
        findValuePanel.add(valueLabel);
        findValuePanel.add(findValueButton);
        findValueButton.addActionListener(e -> {
            if (interpolateFunction == null)
                JOptionPane.showMessageDialog(jFrame, "Сначала интерполируйте функцию",
                        errorTitle, JOptionPane.ERROR_MESSAGE);
            else {
                try {
                    double value = interpolateFunction.getValue(
                            Double.parseDouble(findValueField.getText().replace(',', '.')));
                    valueLabel.setText(String.format("f(%s)=%.3f", findValueField.getText(), value));
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(jFrame, "Значения Х должно быть числом",
                            errorTitle, JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return jFrame;
    }

    private JPanel generateButtons(int argsAmount, JFrame mainFrame) {
        JPanel argsPanel = new JPanel(new GridLayout(1, argsAmount));
        xData = new double[argsAmount];
        for (int i = 0; i < argsAmount; i++) {
            int index = i;
            JTextField xValue = new JTextField();
            xValue.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    try {
                        if (!xValue.getText().equals("")) {
                            xData[index] = Double.parseDouble(xValue.getText().replace(',', '.'));
                            if(Double.isInfinite(baseFunction.getValue(xData[index]))){
                                xData[index] = 0;
                                throw new IllegalArgumentException();
                            }
                        }
                    } catch (NumberFormatException e1) {
                        JOptionPane.showMessageDialog(mainFrame, "Значения Х должны быть числами",
                                "Ошибка", JOptionPane.WARNING_MESSAGE);
                    }
                    catch (IllegalArgumentException e1){
                        JOptionPane.showMessageDialog(mainFrame, "Значение Х выходит за допустимые пределы",
                                "Ошибка", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
            xValue.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    int keyCode = e.getKeyCode();
                    int caretPosition = xValue.getCaretPosition();
                    if (keyCode == KeyEvent.VK_LEFT && index != 0 && caretPosition == 0)
                        argsPanel.getComponent(index - 1).requestFocus();
                    else if (keyCode == KeyEvent.VK_RIGHT && index != argsAmount - 1
                            && caretPosition == xValue.getText().length())
                        argsPanel.getComponent(index + 1).requestFocus();
                }
            });
            argsPanel.add(xValue);
        }
        return argsPanel;
    }

    private JPanel getGraphicPanel(int width, int height, double changeX, boolean changeMode) {
        Lagrange lagrangePolynomial = new Lagrange();

        Arrays.sort(xData);
        double[] yData;
        if (changeMode) {
             yData = Arrays.stream(xData).
                    map(x -> Double.compare(x, changeX) == 0 ? baseFunction.getValue(x) * Main.CHANGE :
                            baseFunction.getValue(x)).toArray();
        } else {
            yData = Arrays.stream(xData).
                    map(x -> baseFunction.getValue(x)).toArray();
        }

        Function interpolateFunction = lagrangePolynomial.interpolate(xData, yData);
        this.interpolateFunction = interpolateFunction;

        JPanel graphicPanel = new Graphing(baseFunction, interpolateFunction, xData).
                getChart(width, height, changeX, baseFunction.getValue(changeX) * Main.CHANGE);
        graphicPanel.setLocation(0, 0);
        graphicPanel.setSize(width, height);
        return graphicPanel;
    }


    public void draw(int width, int height) {
        SwingUtilities.invokeLater(() -> {
            JFrame jFrame = getMainFrame(width, height);
            jFrame.setVisible(true);
        });
    }


}

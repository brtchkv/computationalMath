package lab4;

import lab3.Function;

import java.util.Arrays;

public class DifferentialEquation {
    private Function function;

    public DifferentialEquation(Function function) {
        this.function = function;
    }

    private void setInitialData(double[] xData, double[] yData, double h){

        for(int i = 1; i < 4; i++){
            double x = xData[i - 1];
            double y = yData[i - 1];

            double k0 = function.getValue(x, y);
            double k1 = function.getValue(x + h / 2, y + h / 2 * k0);
            double k2 = function.getValue(x + h / 2, y + h / 2 * k1);
            double k3 = function.getValue(x + h, y + h * k2);

            yData[i] = y + h / 6 * (k0 + 2 * k1 + 2 * k2 + k3);
        }
    }

    public double[][] solve(double x0, double y0, double endPoint, double accuracy){
        int intervals = 5;
        double[] xData;
        double[] yData;
        double averageError;
        double segmentLength = endPoint - x0;
        do {
            intervals *= 2;
            if(intervals > 200_000)
                throw new SolutionException("Достигнут максимум разбиений");
            double h = segmentLength / intervals;

            xData = new double[intervals + 1];
            xData[0] = x0;
            yData = new double[intervals + 1];
            yData[0] = y0;

            for(int i = 1; i < xData.length; i++)
                xData[i] = xData[i - 1] + h;
            setInitialData(xData, yData, h);

            double[] error = new double[yData.length - 4];
            for(int i = 4; i < yData.length; i++){
                double prediction = yData[i - 4] + (4 * h / 3) * (2 * function.getValue(xData[i - 3], yData[i - 3])
                        - function.getValue(xData[i - 2], yData[i - 2])
                        + 2 * function.getValue(xData[i - 1], yData[i - 1]));

                yData[i] = yData[i - 2] + h / 3 * (function.getValue(xData[i - 2], yData[i - 2])
                        + 4 * function.getValue(xData[i - 1], yData[i - 1])
                        + function.getValue(xData[i], prediction));
                if(!Double.isFinite(yData[i]))
                    throw new SolutionException(String.format("При аргументе, лежащем внутри отрезка от %f до %f, " +
                            "значения функции выходят за пределы допустимых значений",
                            Math.min(x0, endPoint), Math.max(x0, endPoint)));
                error[i - 4] = Math.abs(yData[i] - prediction) / 29;
            }
            averageError = Arrays.stream(error).average().orElse(0);
        } while (averageError >= accuracy);
        return new double[][]{xData, yData};

    }


}

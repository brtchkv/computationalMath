package computationalMath;

import java.util.Arrays;
import java.util.List;

public class RandomNumbers {
    private static final double RANDOM_MIN = -5;
    private static final double RANDOM_MAX = 5;

    public double[][] get(int n) {
        System.out.println("Введите размер случайной матрицы");

//        List<Double> numbers = new DoubleArrayPrompter().prompt().orElse(null);
//        if (numbers == null || numbers.size() == 0)
//            return null;
//
//        int size;
//        if (numbers.size() == 1)
//            size = (int)(double)numbers.get(0);
//        else {
//            double minSize = numbers.get(0);
//            double maxSize = numbers.get(1);
//            double result = minSize + Math.random()*(maxSize - minSize);
//            size = (int)result;
//        }
//
        if (n < 0)
            return null;

        double[][] data = new double[n][n + 1];
        for (int a = 0; a < n; a++) {
            double sum = 0;
            for (int b = 0; b < n + 1; b++) {
                data[a][b] = (int) (RANDOM_MIN + Math.random() * (RANDOM_MAX - RANDOM_MIN));
                sum += Math.abs(data[a][b]);
            }
            data[a][a] = sum - Math.abs(data[a][a]) + 1;
        }

        System.out.println("Матрица: ");
        System.out.println();
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < data.length; row++) {
            for (int column = 0; column < data[row].length; column++) {
                builder.append(toNiceString(data[row][column]));
            }
            builder.append("\n");
        }
        System.out.println(builder.toString());

        return data;
    }

    private String toNiceString(double number) {
        return String.format(number >= 0 ? " %8.2f" : "%9.2f", Math.round(number * 1000) / 1000.0);
    }
}
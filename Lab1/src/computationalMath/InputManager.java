package computationalMath;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.Scanner;

public class InputManager {
    private double epsilon = 0;
    private IterationMatrix iterationMatrix;


    public static void welcome() {
        System.out.println("Метод простых итераций");
    }

    public void readMatrix() {
        Scanner in = new Scanner(System.in);

            try {
                System.out.print("Для начала работы введите" +
                        "\n 0 чтобы ввести матрицу с клавиатуры," +
                        "\n 1 для считывания данных для задания из файла " +
                        "\n q для выхода из программы: ");
                System.out.println();
                System.out.println("> ");
                String token = in.next();
                switch (token) {
                    case "0":
                        iterationMatrix = readMatrix(in);
                        while (epsilon == 0) {
                            System.out.println("Введите точность:");
                            epsilon = Double.parseDouble(in.next());
                        }
                        break;
                    case "1":
                        System.out.print("Введите путь к файлу: ");
                        String fileName = in.next();
                        try {
                            in = new Scanner(new File(fileName));
                            in.close();
                            iterationMatrix = readMatrixFromFile(fileName);
                            epsilon = readEpsilonFromFile(fileName);
                        } catch (Exception e) {
                            System.out.println("Неверный путь к файлу!");
                        }
                        break;
                    case "q":
                        System.exit(0);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                System.out.println("Неверный ввод, попробуйте заново.");
                in.nextLine();
            }
    }

    private static int readSize(Scanner in) {
        while (true) {
            System.out.print("Введите количество неизвестных (от 1 до 20): ");
            try {
                int n = in.nextInt();
                if (n < 1 || n > 20)
                    throw new IllegalArgumentException();
                return n;
            } catch (Exception e) {
                System.out.println("Неверное количество неизвестных!");
                in.nextLine();
            }
        }
    }

    public static IterationMatrix readMatrix(Scanner in) {
        int n = readSize(in);
        double[][] matrix = new double[n][n + 1];
        System.out.println("Введите коэффициенты при неизвестных и свободные члены по порядку в формате");
        System.out.println("a11 a12 a13 ... a1n b1");
        System.out.println("a21 a22 a23 ... a2n b2");
        System.out.println("итд.");
        System.out.println("Ввод: ");
        for (int i = 0; i < n; ++i)
            try {
                for (int j = 0; j < n + 1; ++j)
                    matrix[i][j] = Double.parseDouble(in.next().replaceAll(",", "."));
            } catch (Exception e) {
                System.out.println("Произошла ошибка ввода, пожалуйста, введите последнюю строчку заново.");
                in.nextLine();
                i--;
            }
        return new IterationMatrix(matrix);
    }

    /*
     To be read from file a matrix has to be written according to JSON standard
     with the key named "matrix" with the rows split in different elements of JSONArray.
     e. g.
     {  "matrix": [
        "10 1 1 12",
        "2 10 1 13",
        "2 2 10 14"
        ]}
     */
    public static IterationMatrix readMatrixFromFile(String fileName){
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(fileName))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONObject matrixJSON = (JSONObject) obj;
            JSONArray matrixArray = (JSONArray) matrixJSON.get("matrix");
            int n = matrixArray.size();
            double[][] matrix = new double[n][n+1];
            String rowString;
            for (int i = 0; i < matrixArray.size(); ++i) {
                rowString = (String) matrixArray.get(i);
                String[] splitted = rowString.split(" ");
                for (int j = 0; j < splitted.length; ++j)
                    matrix[i][j] = Double.parseDouble(splitted[j].replaceAll(",", "."));
            }
            return new IterationMatrix(matrix);
        } catch (Exception e){
            System.out.println("Ошибка формата JSON у Матрицы.");
            return null;
        }
    }

    /*
     To be read from file an epsilon has to be written according to JSON standard
     with the key named "epsilon"
     e. g.
     {  "epsilon": "0.01" }
     */
    public static double readEpsilonFromFile(String fileName) {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(fileName)) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONObject epsilonJSON = (JSONObject) obj;
            return Double.parseDouble(((String) epsilonJSON.get("epsilon")).replace(",", "."));
        } catch (Exception e){
            System.out.println("Ошибка формата JSON у Эпсилона.");
            return 0;
        }
    }

    public void calculateAnswer() {
        if (!iterationMatrix.isDiagonallyDominant()) {
            System.out.println("Матрица без диагонального преобладания!");
            System.out.println();
            return;
        }

        iterationMatrix.transformMatrixToXFormed();

        //loop over the Xk values till the needed accuracy is reached
        do {
            iterationMatrix.iterateToTheGivenEpsilon();
        } while (iterationMatrix.getMaxDeviation() > epsilon);

        double[] result = iterationMatrix.getApproximation();

        int iters = iterationMatrix.getIterations();
        System.out.println("Точность: " + epsilon);
        System.out.printf("Число итераций: %s", iters);
        System.out.printf("Ответ:\n", iters);
        for (int i = 0; i < result.length; i++) {
            System.out.printf("x%s: %s\n", i+1, result[i]);
        }

        double[] prevResult = iterationMatrix.getPreviousApproximation();
        System.out.println("Вектор погрешностей:");
        for (int i = 0; i < result.length; i++) {
            System.out.printf("x%s^(%s)-x%s^(%s): %s\n", i+1, iters, i+1, iters-1, Math.abs(result[i]-prevResult[i]));
        }
    }
}

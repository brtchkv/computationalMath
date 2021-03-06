package computationalMath.IO;

import computationalMath.rectangleLogic.DoubleFunctor;
import computationalMath.rectangleLogic.Func;
import computationalMath.rectangleLogic.Integral;
import computationalMath.rectangleLogic.Method;

import java.util.*;

public class IntegralIO {
    static {
        init();
    }

    private static List<Func<Double>> functions;

    public static void init() {
        List<Func<Double>> funcs = new ArrayList<>(5);
        funcs.add(new Func<>(Math::sin, "y = sin(x)"));
        funcs.add(new Func<>((x) -> (Math.sqrt(x * x + 1) - 2),
                "y = sqrt(x^2 + 1) - 2"));
        funcs.add(new Func<>((x) -> (Math.log(x)),
                "y = ln(x)"));
        funcs.add(new Func<>((x) -> (x / Math.abs(x)),
                "y = x / |x|"));
        IntegralIO.init(funcs);
    }

    public static void init(List<Func<Double>> arg) {
        functions = arg;
    }

    public static void readData() {
        int numberFunction, numberMethod;
        double leftBound, rightBound, precision;
        Scanner in = new Scanner(System.in);
        StringBuilder output = new StringBuilder("Для начала работы напечатайте номер функции, для которой будет вычисляться интеграл");
        for (int i = 0; i < functions.size(); ++i)
            output.append("\n ").append(i).append(": ").append(functions.get(i).getAlias());
        output.append("\n q для выхода из программы: ");
        while (true) {
            try {
                System.out.print(output.toString());
                String token = in.next();
                if (token.equals("q"))
                    System.exit(0);
                numberFunction = Integer.parseInt(token);
                if (numberFunction < 0 || numberFunction >= functions.size())
                    throw new IllegalArgumentException();
                break;
            } catch (Exception e) {
                System.out.println("Неверный символ, повторите ввод.");
                in.nextLine();
            }
        }
        while (true) {
            try {
                System.out.print("Введите левую границу: ");
                String token = in.next();
                leftBound = Double.parseDouble(token.replaceAll(",","."));
                break;
            } catch (Exception e) {
                System.out.println("Неверное число с плавающей запятой, повторите ввод.");
                in.nextLine();
            }
        }
        while (true) {
            try {
                System.out.print("Введите правую границу: ");
                String token = in.next();
                rightBound = Double.parseDouble(token.replaceAll(",","."));
                break;
            } catch (Exception e) {
                System.out.println("Неверное число с плавающей запятой, повторите ввод.");
                in.nextLine();
            }
        }
        while (true) {
            try {
                System.out.print("Введите необходимую точность: ");
                String token = in.next();
                precision = Double.parseDouble(token.replaceAll(",","."));
                break;
            } catch (Exception e) {
                System.out.println("Неверное число с плавающей запятой, повторите ввод.");
                in.nextLine();
            }
        }
        while (true) {
            try {
                System.out.print("Введите номер числового метода: " +
                        "\n 0: метод левых прямоугольников" +
                        "\n 1: метод средних прямоугольников" +
                        "\n 2: метод правых прямоугольников" +
                        "\n Ввод: ");
                String token = in.next();
                numberMethod = Integer.parseInt(token);
                if (numberMethod < 0 || numberMethod > 2)
                    throw new IllegalArgumentException();
                break;
            } catch (Exception e) {
                System.out.println("Неверное целое число, повторите ввод.");
                in.nextLine();
            }
        }
        var results = new Integral.Results(0, 0, 0);
        try {
                results = Integral.integrate(leftBound, rightBound,
                        new DoubleFunctor(functions.get(numberFunction).getFunction()),
                        precision, Method.values()[1]);
                System.out.println("Результат: " + results.getResult());
                System.out.println("Количество разбиений: " + results.getNumberDivision());
                System.out.println("Погрешность: " + results.getAccuracy());
                System.out.println();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}

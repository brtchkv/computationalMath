package lab3;
public class Main {

    public final static double CHANGE = 1.5;

    public static void main(String[] args) {
        Function function = new FunctionAdapter() {
            @Override
            public double getValue(double arg) {
                return Math.sin(arg);
            }
        };
        UserInterface userInterface = new UserInterface(function);
        userInterface.draw(700, 550);
    }
}

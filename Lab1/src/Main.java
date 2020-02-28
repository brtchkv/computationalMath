import computationalMath.InputManager;

public class Main {
    public static void main(String[] args) {
        InputManager im = new InputManager();
        im.welcome();
        while (true) {
            im.readMatrix();
            im.calculateAnswer();
        }
    }
}

package lab3;

import lab3.Function;

public abstract class FunctionAdapter implements Function {
    @Override
    public double getValue(double arg) {
        return 0;
    }

    @Override
    public double getValue(double x, double y) {
        return 0;
    }
}

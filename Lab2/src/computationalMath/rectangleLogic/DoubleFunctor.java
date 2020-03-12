package computationalMath.rectangleLogic;

import java.util.function.Function;

public class DoubleFunctor {
    private final Function<Double, Double> function;
    public DoubleFunctor(Function<Double, Double> func) {
        function = func;
    }
    public Double applyXValue(Double arg) {
        return function.apply(arg);
    }
}

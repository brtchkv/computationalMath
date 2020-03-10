package computationalMath.rectangleLogic;

import java.util.function.Function;

public class DoubleFunctor implements FunctorImpl<Double> {
    private final Function<Double, Double> function;
    public DoubleFunctor(Function<Double, Double> func) {
        function = func;
    }
    @Override
    public Double execute(Double arg) {
        return function.apply(arg);
    }
}

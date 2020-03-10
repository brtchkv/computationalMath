package computationalMath.rectangleLogic;

import java.util.function.Function;

public class Func<T> {
    private String alias;
    private Function<T, T> function;

    public Func(Function<T, T> function, String alias) {
        this.function = function;
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public Function<T, T> getFunction() {
        return function;
    }
}

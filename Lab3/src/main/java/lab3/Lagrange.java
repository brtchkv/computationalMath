package lab3;
public class Lagrange {
    
    public Function interpolate(double[] xData, double[] yData) {
        Function function = new FunctionAdapter() {
            @Override
            public double getValue(double arg) {
                double lagrangePol = 0;
                for (int i = 0; i < xData.length; i++) {
                    double basicsPol = 1;
                    for (int j = 0; j < xData.length; j++) {
                        if (j != i) {
                            basicsPol *= (arg - xData[j]) / (xData[i] - xData[j]);
                        }
                    }
                    lagrangePol += basicsPol * yData[i];
                }
                return lagrangePol;
            }

            @Override
            public double getValue(double x, double y) {
                return super.getValue(x, y);
            }
        };
        return function;
    }
}

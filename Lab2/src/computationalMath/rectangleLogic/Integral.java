package computationalMath.rectangleLogic;

public class Integral {
    private static final double epsilon = 1e-9;
    public static class Results {
        private final double result;
        private final long numberDivision;
        private final double accuracy;

        public Results(double result, long numberDivision, double accuracy) {
            this.result = result;
            this.numberDivision = numberDivision;
            this.accuracy = accuracy;
        }

        public double getResult() {
            return result;
        }

        public long getNumberDivision() {
            return numberDivision;
        }

        public double getAccuracy() {
            return accuracy;
        }
    }

    public static Results integrate(double leftBound, double rightBound, DoubleFunctor func, double precision, Method method) throws Exception{
        double prevSum = 0;
        double sum = 0;
        double accuracy = 0;
        long n;
        for (n = 1; n < 10000000; n *= 2) {
            double h = (rightBound - leftBound) / n;
            sum = 0;
            switch (method) {
                case LEFT_RECTANGLE:
                    for (int i = 0; i < n; i++){
                        double testValue = func.applyXValue(leftBound + i * h);
                        if (Double.isNaN(testValue) || Double.isInfinite(testValue)){
                            sum += func.applyXValue(leftBound + i * h + epsilon);
                        } else sum += testValue;
                    }
                    break;
                case MIDDLE_RECTANGLE:
                    for (int i = 1; i <= n; i++) {
                        double testValue = func.applyXValue(leftBound + i * h - h / 2);
                        if (Double.isNaN(testValue) || Double.isInfinite(testValue)){
                            sum += func.applyXValue(leftBound + i * h - h / 2 + epsilon);
                        } else sum += testValue;
                    }
                    break;
                case RIGHT_RECTANGLE:
                    for (int i = 0; i <= n; i++) {
                        double testValue = func.applyXValue(leftBound + i * h);
                        if (Double.isNaN(testValue) || Double.isInfinite(testValue)){
                            sum += func.applyXValue(leftBound + i * h + epsilon);
                        } else sum += testValue;
                    }
                    break;
            }
            sum *= h;
            if (Double.isNaN(sum) || Double.isInfinite(sum)) {
                throw new Exception("Интеграл нельзя посчитать");
            }
            accuracy = Math.abs(sum - prevSum) / 3;
            prevSum = sum;
            if (accuracy <= precision)
                break;
            if (n == 1)
                continue;
            if (n * 2 >= 1000000) {
                throw new Exception("Интеграл нельзя посчитать");
            }
        }
            if (Double.isNaN(sum) || Double.isInfinite(sum)) {
                throw new Exception("Интеграл нельзя посчитать");
            }
        return new Results(sum, n, accuracy);
    }
}

package computationalMath.rectangleLogic;

public class Integral {
    public static class Results {
        private final double result;
        private final long numberDivision;
        private final double accuracy;

        private Results(double result, long numberDivision, double accuracy) {
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

    public static Results integrate(double leftBound, double rightBound, DoubleFunctor func, double precision, Method method) {
        precision /= 10;
        double prevSum = 0;
        double sum = 0;
        double accuracy = 0;
        long n;
        for (n = 1; n > 0; n *= 2) {
            double h = (rightBound - leftBound) / n;
            sum = 0;
            switch (method) {
                case LEFT_RECTANGLE:
                    for (int i = 0; i < n; ++i)
                        sum += func.applyXValue(leftBound + i * h);
                    break;
                case MIDDLE_RECTANGLE:
                    for (int i = 1; i <= n; ++i)
                        sum += func.applyXValue(leftBound + i * h - h / 2);
                    break;
                case RIGHT_RECTANGLE:
                    for (int i = 1; i <= n; ++i)
                        sum += func.applyXValue(leftBound + i * h);
                    break;
            }
            sum *= h;
            accuracy = Math.abs(sum - prevSum) / 3;
            prevSum = sum;
            if (n == 1)
                continue;
            if (accuracy < precision)
                break;
        }
        return new Results(sum, n, accuracy);
    }
}

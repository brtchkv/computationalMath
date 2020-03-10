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
        double prev = 0; //NaN is a sign of first loop
        double accumulator = 0;
        double accuracy = 0;
        long n;
        for (n = 1; n > 0; n *= 2) {
            double h = (rightBound - leftBound) / n;
            accumulator = 0;
            switch (method) {
                case LEFT_RECTANGLE:
                    for (int i = 0; i < n; ++i)
                        accumulator += func.execute(leftBound + i * h);
                    break;
                case MIDDLE_RECTANGLE:
                    for (int i = 1; i <= n; ++i)
                        accumulator += func.execute(leftBound + i * h - h / 2);
                    break;
                case RIGHT_RECTANGLE:
                    for (int i = 1; i <= n; ++i)
                        accumulator += func.execute(leftBound + i * h);
                    break;
            }
            accumulator *= h;
            accuracy = Math.abs(accumulator - prev) / 3;
            prev = accumulator;
            if (n == 1)
                continue;
            if (accuracy < precision)
                break;
        }

        return new Results(accumulator, n, accuracy);
    }
}

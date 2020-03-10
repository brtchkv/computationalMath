package computationalMath;

import java.util.Arrays;

public class IterationMatrix {
    private double[][] matrix;
    private double maxDeviation = 0;
    private int iterations = 1;
    private double[] approximation;
    private double[] previousApproximation;

    public IterationMatrix(double[][] initialMatrix) {
        this.matrix = initialMatrix;
    }

    /*
     * Applies the formula Cij = -Aij/Aii (if i != j) or Cij = 0 (if i == j)
     */
    public void transformMatrixToXFormed() {
        // Loop through every row in the array
        for (int i = 0; i < matrix.length; i++) {
            double Aii = matrix[i][i];
            // Loop through every element in the row
            for (int j = 0; j < matrix[i].length; j++) {
                if (j != i) {
                    if (matrix[i].length - 1 != j)
                        matrix[i][j] = matrix[i][j] / -Aii;
                    else
                        matrix[i][j] = matrix[i][j] / Aii;
                } else
                    matrix[i][j] = 0;
            }
        }
    }
    /*
     * Returns an array of constants terms of the matrix a.k.a Bn.
     */
    public double[] getConstantTermsVector() {
        double[] constants = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            constants[i] = matrix[i][matrix[i].length-1];
        }
        return constants;
    }

    /*
     * Computes the X values (aka approximation) on the basis of the previously computed approximation. Also, sets the maximum deviation.
     */
    public double[] computeXUsingPreviousApproximation(final double[] previousApproximation) {
        double[] answer = new double[matrix.length];
        this.maxDeviation = 0;
        for (int i = 0; i < matrix.length; i++) {
            answer[i] = 0;
            //compute the Xk terms values
            for (int j = 0; j < matrix[i].length-1; j++) {
                answer[i] += matrix[i][j] * previousApproximation[j];
            }
            //compute the final Xk value of the row
            answer[i] += matrix[i][matrix[i].length-1];

            //Search for the absolute deviation criteria
            double deviation = Math.abs(answer[i] - previousApproximation[i]);
            if (deviation > this.maxDeviation)
                this.maxDeviation = deviation;
        }
        return answer;
    }
    
    public void iterateToTheGivenEpsilon() {
        // if it's the first iteration then use the Constant Terms Vector as an approximation
        if (iterations == 1) {
            approximation = computeXUsingPreviousApproximation(getConstantTermsVector());
            previousApproximation = approximation;
        } else {
            // otherwise use the previously computed approximation
            previousApproximation = approximation;
            approximation = computeXUsingPreviousApproximation(approximation);
        }
        iterations++;
    }

    /**
     * Check whether the matrix is Diagonally Dominant, if not make it so.
     */
    public boolean transformToDominant(int r, boolean[] V, int[] R) {
        int n = matrix.length;
        // if moved all of the rows then change initial matrix
        if (r == matrix.length) {
            double[][] T = new double[n][n + 1];
            for (int i = 0; i < R.length; i++) {
                for (int j = 0; j < n + 1; j++)
                    T[i][j] = matrix[R[i]][j];
            }
            matrix = T;
            return true;
        }
        //use recursion to move through all of the rows and search for the dominant elements
        for (int i = 0; i < n; i++) {
            if (V[i]) continue;

            double sum = 0;

            for (int j = 0; j < n; j++)
                sum += Math.abs(matrix[i][j]);

            if (2 * Math.abs(matrix[i][r]) > sum) {
                V[i] = true;
                R[r] = i;
                if (transformToDominant(r + 1, V, R))
                    return true;
                V[i] = false;
            }
        }

        return false;
    }

    /**
     * Check whether the matrix is Diagonally Dominant, if not makes it so.
     */
    public boolean makeDominant() {
        //boolean array for highlighting moved rows
        boolean[] visited = new boolean[matrix.length];
        int[] rows = new int[matrix.length];

        Arrays.fill(visited, false);

        return transformToDominant(0, visited, rows);
    }

    public double getMaxDeviation() {
        return maxDeviation;
    }

    public int getIterations() {
        return iterations;
    }

    public double[] getApproximation() {
        return approximation;
    }

    public double[] getPreviousApproximation() {
        return previousApproximation;
    }
}
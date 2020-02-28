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
     * Check whether the matrix is DiagonallyDominant
     */
    public boolean isDiagonallyDominant() {
        double otherTotal;
        // Loop through every row in the array
        for(int row = 0; row < matrix.length; row++) {
            otherTotal = 0;
            // Loop through every element in the row
            for(int column = 0; column < (matrix[row].length - 1); column++) {
                // If this element is NOT on the diagonal
                if(column != row) {
                    // Add it to the running total
                    otherTotal += Math.abs(matrix[row][column]);
                }
            }
            // If this diagonal element is LESS than the sum of the other ones...
            if(Math.abs(matrix[row][row]) < otherTotal) {
                // then the array isn't diagonally dominant and we can return.
                return false;
            }
        }
        return true;
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

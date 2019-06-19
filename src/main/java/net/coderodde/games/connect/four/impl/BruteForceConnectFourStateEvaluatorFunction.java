package net.coderodde.games.connect.four.impl;

import net.coderodde.games.connect.four.ConnectFourState;
import net.coderodde.games.connect.four.PlayerColor;
import net.coderodde.zerosum.ai.EvaluatorFunction;

/**
 * This class implements the default Connect Four state evaluator. The white 
 * player wants to maximize, the red player wants to minimize.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (May 24, 2019)
 */
public final class BruteForceConnectFourStateEvaluatorFunction
        implements EvaluatorFunction<ConnectFourState> {

    private static final double NEGATIVE_WIN_VALUE = -1e9;
    private static final double POSITIVE_WIN_VALUE = 1e9;
    private static final double POSITIVE_CLOSE_TO_WIN_VALUE = -1e6;
    private static final double NEGATIVE_CLOSE_TO_WIN_VALUE = 1e6;
    private static final double BASE_VALUE = 1e1;

    /**
     * The weight matrix. Maps each position to its weight. We need this in 
     * order to 
     */
    private final double[][] weightMatrix;

    /**
     * The winning length.
     */
    private final int winningLength;

    /**
     * Constructs the default heuristic function for Connect Four game states.
     * 
     * @param width the game board width.
     * @param height the game board height.
     * @param maxWeight the maximum weight in the weight matrix.
     * @param winningPatternLength the winning pattern length.
     */
    public BruteForceConnectFourStateEvaluatorFunction(final int width,
                                             final int height,
                                             final double maxWeight,
                                             final int winningPatternLength) {
        this.weightMatrix = getWeightMatrix(width, height, maxWeight);
        this.winningLength = winningPatternLength;
    }

    /**
     * Evaluates the given input {@code state} and returns the estimate.
     * @param state the state to estimate.
     * @return the estimate.
     */
    @Override
    public double evaluate(ConnectFourState state) {
        // 'minimizingPatternCounts[i]' gives the number of patterns of 
        // length 'i':
        int[] minnimizingPatternCounts = new int[state.getWinningLength() + 1];
        int[] maximizingPatternCounts = new int[minnimizingPatternCounts.length];

        // Do not consider patterns of length one!
        for (int targetLength = 2; 
                targetLength <= winningLength; 
                targetLength++) {
            int count = findMinimizingPatternCount(state, targetLength);

            if (count == 0) {
                // Once here, it is not possible to find patterns of larger 
                // length than targetLength:
                break;
            }

            minnimizingPatternCounts[targetLength] = count;
        }

        for (int targetLength = 2;
                targetLength <= state.getWinningLength();
                targetLength++) {
            int count = findMaximizingPatternCount(state, targetLength);

            if (count == 0) {
                // Once here, it is not possible to find patterns of larger
                // length than targetLength:
                break;
            }

            maximizingPatternCounts[targetLength] = count;
        }

        double score = computeBaseScore(minnimizingPatternCounts, 
                                        maximizingPatternCounts);

        return score;// + getWeights(weightMatrix, state);
    }

    /**
     * Finds the number of red patterns of length {@code targetLength}.
     * @param state the target state.
     * @param targetLength the length of the pattern to find.
     * @return the number of red patterns of length {@code targetLength}.
     */
    private static final int findMinimizingPatternCount(ConnectFourState state,
                                                 int targetLength) {
        return findPatternCount(state, 
                                targetLength, 
                                PlayerColor.MINIMIZING_PLAYER);
    }

    /**
     * Finds the number of white patterns of length {@code targetLength}. 
     * @param state the target state.
     * @param targetLength the length of the pattern to find.
     * @return the number of white patterns of length {@code targetLength}.
     */
    private static final int findMaximizingPatternCount(ConnectFourState state,
                                                   int targetLength) {
        return findPatternCount(state,
                                targetLength, 
                                PlayerColor.MAXIMIZING_PLAYER);
    }

    /**
     * Implements the target pattern counting function for both the player 
     * colors.
     * @param state the state to search.
     * @param targetLength the length of the patterns to count.
     * @param playerColor the target player color.
     * @return the number of patterns of length {@code targetLength} and color
     * {@code playerColor}.
     */
    private static final int findPatternCount(ConnectFourState state,
                                              int targetLength,
                                              PlayerColor playerColor) {
        int count = 0;

        count += findHorizontalPatternCount(state, 
                                            targetLength, 
                                            playerColor);

        count += findVerticalPatternCount(state, 
                                          targetLength, 
                                          playerColor);

        count += findAscendingDiagonalPatternCount(state, 
                                                   targetLength,
                                                   playerColor);

        count += findDescendingDiagonalPatternCount(state, 
                                                    targetLength,
                                                    playerColor);
        return count;
    }

    /**
     * Scans the input state for diagonal <b>descending</b> patterns and 
     * returns the number of such patterns.
     * @param state the target state.
     * @param patternLength the target pattern length.
     * @param playerColor the target player color.
     * @return the number of patterns.
     */
    private static final int 
        findDescendingDiagonalPatternCount(ConnectFourState state,
                                           int patternLength,
                                           PlayerColor playerColor) {
        int patternCount = 0;

        for (int y = 0; y < state.getWinningLength() - 1; y++) {
            inner:
            for (int x = 0;
                    x <= state.getWidth() - state.getWinningLength(); 
                    x++) {
                for (int i = 0; i < patternLength; i++) {
                    if (state.readCell(x + i, y + i) != playerColor) {
                        continue inner;
                    }
                }

                patternCount++;
            }
        }

        return patternCount;
    }

    /**
     * Scans the input state for diagonal <b>ascending</b> patterns and returns
     * the number of such patterns.
     * @param state the target state.
     * @param patternLength the target pattern length.
     * @param playerColor the target player color.
     * @return the number of patterns.
     */
    private static final int 
        findAscendingDiagonalPatternCount(ConnectFourState state,
                                          int patternLength,
                                          PlayerColor playerColor) {
        int patternCount = 0;

        for (int y = state.getHeight() - 1;
                y > state.getHeight() - state.getWinningLength();
                y--) {

            inner:
            for (int x = 0; 
                    x <= state.getWidth() - state.getWinningLength();
                    x++) {
                for (int i = 0; i < patternLength; i++) {
                    if (state.readCell(x + i, y - i) != playerColor) {
                        continue inner;
                    }
                }

                patternCount++;
            }
        }

        return patternCount;
    } 

    /**
     * Scans the input state for diagonal <b>horizontal</b> patterns and returns
     * the number of such patterns.
     * @param state the target state.
     * @param patternLength the target pattern length.
     * @param playerColor the target player color.
     * @return the number of patterns.
     */
    private static final int findHorizontalPatternCount(
            ConnectFourState state,
            int patternLength,
            PlayerColor playerColor) {
        int patternCount = 0;

        for (int y = state.getHeight() - 1; y >= 0; y--) {

            inner:
            for (int x = 0; x <= state.getWidth() - patternLength; x++) {
                if (state.readCell(x, y) == null) {
                    continue inner;
                }

                for (int i = 0; i < patternLength; i++) {
                    if (state.readCell(x + i, y) != playerColor) {
                        continue inner;
                    }
                }

                patternCount++;
            }
        }

        return patternCount;
    }

    /**
     * Scans the input state for diagonal <b>vertical</b> patterns and returns
     * the number of such patterns.
     * @param state the target state.
     * @param patternLength the target pattern length.
     * @param playerColor the target player color.
     * @return the number of patterns.
     */
    private static final int findVerticalPatternCount(ConnectFourState state,
                                                      int patternLength,
                                                      PlayerColor playerColor) {
        int patternCount = 0;

        outer:
        for (int x = 0; x < state.getWidth(); x++) {
            inner:
            for (int y = state.getHeight() - 1;
                    y > state.getHeight() - state.getWinningLength(); 
                    y--) {
                if (state.readCell(x, y) == null) {
                    continue outer;
                }

                for (int i = 0; i < patternLength; i++) {
                    if (state.readCell(x, y - i) != playerColor) {
                        continue inner;
                    }
                }

                patternCount++;
            }
        }

        return patternCount;
    }

    /**
     * Gets the state weight. We use this in order to discourage the positions
     * that are close to borders/far away from the center of the game board.
     * @param weightMatrix the weighting matrix.
     * @param state the state to weight.
     * @return the state weight.
     */
    private static final double getWeights(final double[][] weightMatrix,
                                           final ConnectFourState state) {
        double score = 0.0;

        outer:
        for (int x = 0; x < state.getWidth(); x++) {
            for (int y = state.getHeight() - 1; y >= 0; y--) {
                PlayerColor playerColor = state.readCell(x, y);

                if (playerColor == null) {
                    continue outer;
                }

                if (playerColor == PlayerColor.MINIMIZING_PLAYER) {
                    score -= weightMatrix[y][x];
                } else {
                    score += weightMatrix[y][x];
                }
            }
        }

        return score;
    }

    /**
     * Computes the base scorer that relies on number of patterns. For example,
     * {@code redPatternCounts[i]} will denote the number of patterns of length 
     * [@code i}.
     * @param minimizingPatternCounts the pattern count map for red patterns.
     * @param maximizingPatternCounts the pattern count map for white patterns.
     * @return the base estimate.
     */
    private static final double computeBaseScore(
            int[] minimizingPatternCounts,
            int[] maximizingPatternCounts) {
        final int winningLength = minimizingPatternCounts.length - 1;

        double value = 0.0;

        if (minimizingPatternCounts[winningLength] != 0) {
            value = NEGATIVE_WIN_VALUE;
        }

        if (maximizingPatternCounts[winningLength] != 0) {
            value = POSITIVE_WIN_VALUE;
        }

        for (int length = 2; length < minimizingPatternCounts.length; length++) {
            int minimizingCount = minimizingPatternCounts[length];
            value -= minimizingCount * Math.pow(BASE_VALUE, length);

            int maximizingCount = maximizingPatternCounts[length];
            value += maximizingCount * Math.pow(BASE_VALUE, length);
        }

        return value;
    }

    /**
     * Computes the weight matrix. The closer the entry in the board is to the
     * center of the board, the closer the weight of that position will be to
     * {@code maxWeight}.
     * 
     * @param width the width of the matrix.
     * @param height the height of the matrix.
     * @param maxWeight the maximum weight. The minimum weight will be always
     * 1.0.
     * @return the weight matrix. 
     */
    private static final double[][] getWeightMatrix(final int width,
                                                    final int height,
                                                    final double maxWeight) {
        final double[][] weightMatrix = new double[height][width];

        for (int y = 0; y < weightMatrix.length; y++) {
            for (int x = 0; x < weightMatrix[0].length; x++) {
                int left = x;
                int right = weightMatrix[0].length - x - 1;
                int top = y;
                int bottom = weightMatrix.length - y - 1;
                int horizontalDifference = Math.abs(left - right);
                int verticalDifference = Math.abs(top - bottom);
                weightMatrix[y][x] =
                        1.0 + (maxWeight - 1.0) / 
                              (horizontalDifference + verticalDifference);
            }
        }

        return weightMatrix;
    }
}

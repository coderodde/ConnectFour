package net.coderodde.games.connect.four.impl;

import net.coderodde.games.connect.four.ConnectFourState;
import net.coderodde.games.connect.four.PlayerColor;
import net.coderodde.zerosum.ai.EvaluatorFunction;

/**
 * This evaluation function relies on a weight matrix that reflects how many
 * patterns visit each matrix position.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jun 19, 2019)
 */
public class WeightMatrixConnectFourStateEvaluatorFunction implements EvaluatorFunction<ConnectFourState> {

    private final double[][] matrix;
    
    public WeightMatrixConnectFourStateEvaluatorFunction() {
        this.matrix =  new double[][] {{3, 4,  5,  7,  5, 4, 3}, 
                                       {4, 6,  8, 10,  8, 6, 4},
                                       {5, 8, 11, 13, 11, 8, 5}, 
                                       {5, 8, 11, 13, 11, 8, 5},
                                       {4, 6,  8, 10,  8, 6, 4},
                                       {3, 4,  5,  7,  5, 4, 3}};
    }

    @Override
    public double evaluate(ConnectFourState state) {
        PlayerColor winner = state.checkVictory();
        
        if (winner == PlayerColor.MINIMIZING_PLAYER) {
            return -1e6;
        }
        
        if (winner == PlayerColor.MAXIMIZING_PLAYER) {
            return 1e6;
        }
        
        double sum = 0.0;
        
        for (int y = 0; y < state.getHeight(); y++) {
            for (int x = 0; x < state.getWidth(); x++) {
                if (state.readCell(x, y) == PlayerColor.MAXIMIZING_PLAYER) {
                    sum += matrix[y][x];
                } else if (state.readCell(x, y) ==
                        PlayerColor.MINIMIZING_PLAYER) {
                    sum -= matrix[y][x];
                }
            }
        }
        
        return sum;
    }
}

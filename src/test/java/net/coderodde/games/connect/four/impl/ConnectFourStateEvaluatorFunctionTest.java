package net.coderodde.games.connect.four.impl;

import net.coderodde.games.connect.four.ConnectFourState;
import net.coderodde.games.connect.four.Demo;
import net.coderodde.games.connect.four.PlayerColor;
import net.coderodde.zerosum.ai.EvaluatorFunction;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * This class tests the correctness of the evaluator function.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jun 18, 2019)
 */
public class ConnectFourStateEvaluatorFunctionTest {
    
    private final EvaluatorFunction<ConnectFourState> func = 
            new ConnectFourStateEvaluatorFunction(
                    ConnectFourState.DEFAULT_WIDTH,
                    ConnectFourState.DEFAULT_HEIGHT,
                    Demo.MAX_WEIGHT_MATRIX_ENTRY,
                    ConnectFourState.DEFAULT_WINNING_LENGTH);
    @Test
    public void testWhenStateIsInitial() {
        ConnectFourState state = 
                new ConnectFourState(PlayerColor.MAXIMIZING_PLAYER);
        
        assertEquals(0.0, func.evaluate(state), 0.001);
    }        
    
    @Test
    public void testWhenOnlyTwoMinimizingCells() {
        ConnectFourState state = 
                new ConnectFourState(PlayerColor.MINIMIZING_PLAYER);
        
        state = state.move(3, PlayerColor.MAXIMIZING_PLAYER);
        state = state.move(2, PlayerColor.MAXIMIZING_PLAYER);
        state = state.move(2, PlayerColor.MAXIMIZING_PLAYER);
        state = state.move(1, PlayerColor.MAXIMIZING_PLAYER);
        state = state.move(0, PlayerColor.MAXIMIZING_PLAYER);
        System.out.println(func.evaluate(state));
    }
}

package net.coderodde.games.connect.four.impl;

import net.coderodde.games.connect.four.ConnectFourState;
import static net.coderodde.games.connect.four.Demo.MAX_WEIGHT_MATRIX_ENTRY;
import net.coderodde.games.connect.four.PlayerColor;
import net.coderodde.zerosum.ai.EvaluatorFunction;
import net.coderodde.zerosum.ai.GameEngine;
import net.coderodde.zerosum.ai.impl.AlphaBetaPruningGameEngine;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * This class tests the smart bot.
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jun 19, 2019)
 */
public class SmartBotTest {
    
    private final SmartBot smartBot;
    private final GameEngine<ConnectFourState, PlayerColor> gameEngine;
    
    public SmartBotTest() {
        EvaluatorFunction<ConnectFourState> evaluatorFunction1 = 
                new BruteForceConnectFourStateEvaluatorFunction(
                        ConnectFourState.DEFAULT_WIDTH,
                        ConnectFourState.DEFAULT_HEIGHT,
                        MAX_WEIGHT_MATRIX_ENTRY,
                        ConnectFourState.DEFAULT_WINNING_LENGTH);

        EvaluatorFunction<ConnectFourState> evaluatorFunction2 = 
                new WeightMatrixConnectFourStateEvaluatorFunction();
        
        this.gameEngine = 
                new AlphaBetaPruningGameEngine<>(evaluatorFunction1, 7);
        this.smartBot = new SmartBot(PlayerColor.MINIMIZING_PLAYER, gameEngine);
    }
    
    @Test
    public void testThreeMaximizingOneMinimizing() {
        ConnectFourState state =
                new ConnectFourState(PlayerColor.MAXIMIZING_PLAYER);
        
        state.write(2, 5, PlayerColor.MAXIMIZING_PLAYER);
        state.write(3, 5, PlayerColor.MAXIMIZING_PLAYER);
        
        assertEquals(PlayerColor.MAXIMIZING_PLAYER, state.readCell(2, 5));
        assertEquals(PlayerColor.MAXIMIZING_PLAYER, state.readCell(3, 5));
        
        state = gameEngine.makePly(state,
                                   PlayerColor.MINIMIZING_PLAYER, 
                                   PlayerColor.MAXIMIZING_PLAYER,
                                   PlayerColor.MINIMIZING_PLAYER);
        
        assertNull(state.readCell(0, 5));
        assertNull(state.readCell(5, 5));
        assertNull(state.readCell(6, 5));
        
        PlayerColor playerColor1 = state.readCell(1, 5);
        PlayerColor playerColor2 = state.readCell(4, 5);
        
        assertTrue(
                (playerColor1 == PlayerColor.MINIMIZING_PLAYER 
                    && playerColor2 == null) ||
                (playerColor1 == null 
                    && playerColor2 == PlayerColor.MINIMIZING_PLAYER));
    }
}

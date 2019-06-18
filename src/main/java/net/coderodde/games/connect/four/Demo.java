package net.coderodde.games.connect.four;

import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import net.coderodde.games.connect.four.impl.ConnectFourStateEvaluatorFunction;
import net.coderodde.games.connect.four.impl.Human;
import net.coderodde.games.connect.four.impl.RandomBot;
import net.coderodde.games.connect.four.impl.SmartBot;
import net.coderodde.zerosum.ai.EvaluatorFunction;
import net.coderodde.zerosum.ai.GameEngine;
import net.coderodde.zerosum.ai.impl.AlphaBetaPruningGameEngine;

/**
 * This class implements the Connect Four game in the command line/console.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (May 25, 2019)
 */
public class Demo {

    public static final double MAX_WEIGHT_MATRIX_ENTRY = 10.0;
    
    private static final int DEFAULT_SEARCH_DEPTH = 8;
    
    public static void main(String[] args) {
        Random random = new Random();
        
        EvaluatorFunction<ConnectFourState> evaluatorFunction = 
                new ConnectFourStateEvaluatorFunction(
                        ConnectFourState.DEFAULT_WIDTH,
                        ConnectFourState.DEFAULT_HEIGHT,
                        MAX_WEIGHT_MATRIX_ENTRY,
                        ConnectFourState.DEFAULT_WINNING_LENGTH);
        
        GameEngine<ConnectFourState, PlayerColor> gameEngine = 
                new AlphaBetaPruningGameEngine<>(evaluatorFunction, 
                                                 DEFAULT_SEARCH_DEPTH);
        
        Bot bot1 = new RandomBot(PlayerColor.MINIMIZING_PLAYER, random);
        Bot bot2 = new SmartBot(PlayerColor.MAXIMIZING_PLAYER, gameEngine);
        // 'bot3' is connected to cin:
        Bot bot3 = new Human(PlayerColor.MINIMIZING_PLAYER, "X >>> ", 
                             new Scanner(System.in));
        
        playMatch(bot3, bot2);
    }
    
    /**
     * Plays a single match.
     * 
     * @param bot1 the first bot. This bot makes the first move.
     * @param bot2 the second bot.
     */
    private static final void playMatch(Bot bot1, Bot bot2) {
        checkBotPlayers(bot1, bot2);
        ConnectFourState state = new ConnectFourState(bot1.getPlayerColor());
        System.out.println(state);
        System.out.println();
        
        // bot1 begins the game.
        Bot currentBot = bot1;
        PlayerColor winner = null;
        
        // While there is room in the board:
        while (!state.isFull()) {
            if (currentBot == bot1) {
                state = bot1.computeNextState(state);
                winner = state.checkVictory();
                
                if (winner != null) {
                    break;
                }
                
                currentBot = bot2;
            } else {
                state = bot2.computeNextState(state);
                winner = state.checkVictory();
                
                if (winner != null) {
                    break;
                }
                
                currentBot = bot1;
            }
            
            System.out.println(state);
            System.out.println();
        }
        
        System.out.println(state);
        
        if (winner != null) {
            char winnerChar;
        
            switch (winner) {
                case MAXIMIZING_PLAYER:
                    winnerChar = PlayerColor.MINIMIZING_PLAYER.getChar();
                    break;
                    
                case MINIMIZING_PLAYER:
                    winnerChar = PlayerColor.MAXIMIZING_PLAYER.getChar();
                    break;
                    
                default:
                    throw new IllegalStateException(
                            "Unknown player: " + winner);
            }
            
            System.out.println("RESULT: The " + winnerChar + " won!");
        } else {
            System.out.println("RESULT: It's a draw!");
        }
    }
    
    private static final void checkBotPlayers(Bot bot1, Bot bot2) {
        Objects.requireNonNull(bot1, "The bot1 is null.");
        Objects.requireNonNull(bot1, "The bot2 is null.");
        Objects.requireNonNull(bot1.getPlayerColor(), 
                               "The PlayerColor of bot1 is null.");
        Objects.requireNonNull(bot2.getPlayerColor(),
                               "The PlayerColor of bot2 is null.");
        
        if (bot1.getPlayerColor() == bot2.getPlayerColor()) {
            throw new IllegalArgumentException(
                    "The two input bots have the same player color.");
        }
    }
}

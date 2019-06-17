package net.coderodde.games.connect.four;

import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import net.coderodde.games.connect.four.impl.Human;
import net.coderodde.games.connect.four.impl.RandomBot;

/**
 * This class implements the Connect Four game in the command line/console.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (May 25, 2019)
 */
public class Demo {

    
    public static void main(String[] args) {
        Random random = new Random();
        
        Bot bot1 = new RandomBot(PlayerColor.WHITE_PLAYER, random);
        Bot bot2 = new RandomBot(PlayerColor.RED_PLAYER, random);
        Bot bot3 = new Human(PlayerColor.RED_PLAYER, "X >>> ", new Scanner(System.in));
        playMatch(bot1, bot3);
    }
    
    /**
     * Plays a single match.
     * 
     * @param bot1 the first bot;
     * @param bot2 the second bot;
     */
    private static final void playMatch(Bot bot1, Bot bot2) {
        checkBotPlayers(bot1, bot2);
        ConnectFourState state = new ConnectFourState();
        System.out.println(state);
        System.out.println();
        
        // bot1 begins the game.
        Bot currentBot = bot1;
        
        // While there is room in the board and no one won yet:
        while (!state.isFull() && state.checkVictory() == null) {
            if (currentBot == bot1) {
                state = bot1.computeNextState(state);
                currentBot = bot2;
            } else {
                state = bot2.computeNextState(state);
                currentBot = bot1;
            }
            
            System.out.println(state);
            System.out.println();
        }
        
        PlayerColor winnerPlayerColor = state.checkVictory();
        
        if (winnerPlayerColor == null) {
            if (!state.isFull()) {
                throw new IllegalStateException(
                        "No one won and the board is not full.");
            }
            
            System.out.println("RESULT: It's a draw.");
        } else {
            switch (winnerPlayerColor.getChar()) {
                case 'O':
                    System.out.println("RESULT: The O player won.");
                    break;
                    
                case 'X':
                    System.out.println("RESULT: The X player won.");
                    break;
            }
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

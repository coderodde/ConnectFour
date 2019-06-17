package net.coderodde.games.connect.four.impl;

import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;
import net.coderodde.games.connect.four.Bot;
import net.coderodde.games.connect.four.ConnectFourState;
import net.coderodde.games.connect.four.PlayerColor;

/**
 * This class implements a human bot controllable from the console.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (May 25, 2019)
 */
public final class Human implements Bot {

    /**
     * The color of this human.
     */
    private final PlayerColor myPlayerColor;
    
    /**
     * The command prompt for the user input.
     */
    private final String commandPrompt;
    
    /**
     * The scanner for reading the input.
     */
    private final Scanner scanner;
    
    public Human(PlayerColor me, String commandPrompt, Scanner scanner) {
        this.myPlayerColor = Objects.requireNonNull(me, "The input player is null.");
        this.commandPrompt = 
                Objects.requireNonNull(
                        commandPrompt, 
                        "The input command prompt is null.");
        
        this.scanner = Objects.requireNonNull(scanner, 
                                              "The input scanner is null.");
    }
    
    @Override
    public ConnectFourState computeNextState(ConnectFourState state) {
        loop:
        while (true) {
            try {
                System.out.print(commandPrompt);
                int columnIndex = scanner.nextInt();
                checkColumn(columnIndex, state);
                // Convert human indexing from 1 to computer indexing from 0:
                columnIndex--;
                
                if (state.columnIsFull(columnIndex)) {
                    System.out.println(
                            "Column " + (columnIndex + 1) + " is full.");
                    continue loop;
                }
                
                return state.move(columnIndex, myPlayerColor);
            } catch (InputMismatchException ex) {
                throw new IllegalStateException("Unrecognized column number: " +
                                                ex.getMessage());
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    private void checkColumn(int columnIndex, ConnectFourState state) {
        if (columnIndex < 1) {
            throw new IllegalArgumentException(
                    "Too small column index: " + columnIndex + ". Must be at " + 
                    "least 1.");
        }
        
        if (columnIndex > state.getWidth()) {
            throw new IllegalArgumentException(
                    "Too large column index: " + columnIndex + ". Must be at " +
                    "mosst " + state.getWidth() + ".");
        }
    }

    @Override
    public PlayerColor getPlayerColor() {
        return myPlayerColor;
    }
}

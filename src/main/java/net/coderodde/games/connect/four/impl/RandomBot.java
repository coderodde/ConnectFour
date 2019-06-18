
package net.coderodde.games.connect.four.impl;

import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;
import net.coderodde.games.connect.four.Bot;
import net.coderodde.games.connect.four.ConnectFourState;
import net.coderodde.games.connect.four.PlayerColor;

/**
 * This class implements a bot that chooses the columns randomly.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (May 24, 2019)
 */
public final class RandomBot implements Bot {

    private final PlayerColor myPlayerColor;
    private final Random random;
    
    public RandomBot(PlayerColor myPlayerColor, Random random) {
        this.myPlayerColor = 
                Objects.requireNonNull(myPlayerColor,
                                       "The given player is null.");
        this.random = Objects.requireNonNull(random,
                                             "The input Random is null.");
    }
    
    @Override
    public ConnectFourState computeNextState(ConnectFourState state) {
        int[] columnCoordinates = 
                IntStream.range(0, state.getWidth())
                         .toArray();
        for (int i = 0; i < columnCoordinates.length * 2; i++) {
            int index1 = random.nextInt(columnCoordinates.length);
            int index2 = random.nextInt(columnCoordinates.length);
            int column = columnCoordinates[index1];
            columnCoordinates[index1] = columnCoordinates[index2];
            columnCoordinates[index2] = column;
        }
        
        for (int columnCoordinate : columnCoordinates) {
            if (!state.columnIsFull(columnCoordinate)) {
                return state.move(columnCoordinate, myPlayerColor);
            }
        }
        
        throw new IllegalStateException("We should not get here. Ever.");
    }

    @Override
    public PlayerColor getPlayerColor() {
        return myPlayerColor;
    }
}

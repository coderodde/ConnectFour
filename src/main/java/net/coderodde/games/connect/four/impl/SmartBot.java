package net.coderodde.games.connect.four.impl;

import java.util.Objects;
import net.coderodde.games.connect.four.Bot;
import net.coderodde.games.connect.four.ConnectFourState;
import net.coderodde.games.connect.four.PlayerColor;
import net.coderodde.zerosum.ai.GameEngine;

/**
 * This class implements the smart bot relying on Alpha-beta pruning.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (May 24, 2019)
 */
public final class SmartBot implements Bot {
    
    private final PlayerColor myPlayerColor;
    private final GameEngine<ConnectFourState, PlayerColor> engine;
    
    public SmartBot(PlayerColor me, 
                    GameEngine<ConnectFourState, PlayerColor> engine) {
        this.myPlayerColor =
                Objects.requireNonNull(me, "The input player is null.");
        
        this.engine = Objects.requireNonNull(engine,
                                             "The input engine is null.");
    }

    @Override
    public ConnectFourState computeNextState(ConnectFourState state) {
        long startTime = System.currentTimeMillis();
        ConnectFourState nextState = 
                engine.makePly(state, 
                               PlayerColor.MINIMIZING_PLAYER, 
                               PlayerColor.MAXIMIZING_PLAYER, 
                               myPlayerColor);
        long endTime = System.currentTimeMillis();
        System.out.println("SmartBot in " + (endTime - startTime) + " ms.");
        return nextState;
    }

    @Override
    public PlayerColor getPlayerColor() {
        return myPlayerColor;
    }
}

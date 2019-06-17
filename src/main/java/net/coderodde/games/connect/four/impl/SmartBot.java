package net.coderodde.games.connect.four.impl;

import java.util.Objects;
import net.coderodde.games.connect.four.Bot;
import net.coderodde.games.connect.four.ConnectFourState;
import net.coderodde.games.connect.four.PlayerColor;
import net.coderodde.games.connect.four.HeuristicFunction;

/**
 * This class implements the smart bot relying on Alpha-beta pruning.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (May 24, 2019)
 */
public final class SmartBot implements Bot {
    
    private final PlayerColor myPlayerColor;
    private final HeuristicFunction evaluator;
    
    public SmartBot(PlayerColor me, HeuristicFunction evaluator) {
        this.myPlayerColor =
                Objects.requireNonNull(me, "The input player is null.");
        
        this.evaluator = Objects.requireNonNull(evaluator,
                                                "The input evaluator is null.");
    }

    @Override
    public ConnectFourState computeNextState(ConnectFourState state) {
        return null;
    }

    @Override
    public PlayerColor getPlayerColor() {
        return myPlayerColor;
    }
}

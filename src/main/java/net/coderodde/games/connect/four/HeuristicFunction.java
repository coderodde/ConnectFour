package net.coderodde.games.connect.four;

/**
 * This interface specifies the API for methods for evaluating the game states.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (May 24, 2019)
 */
public interface HeuristicFunction {
    
    /**
     * Evaluates the input state and returns the estimate.
     * @param state the state to estimate.
     * @return the state quality estimate.
     */
    public double evaluate(ConnectFourState state);
}

package net.coderodde.games.connect.four;

/**
 * This enumeration lists all the players.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (May 24, 2019)
 */
public enum PlayerColor {
    
    /**
     * Maximizing player.
     */
    MAXIMIZING_PLAYER('X'),
    
    /**
     * Minimizing player.
     */
    MINIMIZING_PLAYER('O');
    
    private final char playerColorChar;
    
    private PlayerColor(final char playerColorChar) {
        this.playerColorChar = playerColorChar;
    }
    
    public char getChar() {
        return this.playerColorChar;
    }
}

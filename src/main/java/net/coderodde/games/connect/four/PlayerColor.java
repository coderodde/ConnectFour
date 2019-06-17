package net.coderodde.games.connect.four;

/**
 * This enumeration lists all the players.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (May 24, 2019)
 */
public enum PlayerColor {
    
    WHITE_PLAYER('O'),
    RED_PLAYER('X');
    
    private final char playerColorChar;
    
    private PlayerColor(final char playerColorChar) {
        this.playerColorChar = playerColorChar;
    }
    
    public char getChar() {
        return this.playerColorChar;
    }
}

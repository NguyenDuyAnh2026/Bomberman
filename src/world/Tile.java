package world;

public class Tile {
    private final char symbol;
    public Tile(char symbol) {
        this.symbol=symbol;
    }
    public char symbol() {
        return symbol;
    }
    public boolean walkable() {
        return symbol=='.'||symbol=='G';
    }
}

package world;

public final class TileFactory {
    private TileFactory() {
    }
    public static Tile create(char c) {
        return switch(c) {
            case '#'->new Wall();
            case 'B'->new Brick();
            case 'G'->new Portal();
            default->new Ground();
        }
        ;
    }
}

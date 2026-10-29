public class TileFactory {
    public static Tile createTile(char symbol) {
        switch (symbol) {
            case '#':
                return new Wall();

            case 'B':
                return new Brick();

            default:
                return new Ground();
        }
    }
}

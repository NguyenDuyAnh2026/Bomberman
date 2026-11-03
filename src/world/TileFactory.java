<<<<<<< HEAD
=======
package world; // Bắt buộc phải có dòng này để cùng package với Tile, GameMap,...

>>>>>>> fd4eec8 (Initial commit: Them du an Bomberman)
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

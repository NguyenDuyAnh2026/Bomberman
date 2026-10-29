import java.awt.Graphics2D;

public class GameMap {
    public static final int TILE_SIZE = 40;
    public static final int ROWS = 13;
    public static final int COLS = 15;

    private final Tile[][] tiles = new Tile[ROWS][COLS];

    private final String[] mapData = {
            "###############",
            "#P..B....B....#",
            "#.#.#.#.#.#.#.#",
            "#B.B.B.B.B.B.B#",
            "#.#...#...#.#.#",
            "#...B...B.....#",
            "###.#.#.#.#####",
            "#...B...B.....#",
            "#.#.#.#.#.#.#.#",
            "#B.B.B.B.B.B.B#",
            "#.#...#...#.#.#",
            "#............G#",
            "###############"
    };

    public GameMap() {
        loadMap();
    }

    private void loadMap() {
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLS; x++) {
                char symbol = mapData[y].charAt(x);
                tiles[y][x] = TileFactory.createTile(symbol);
            }
        }
    }

    public void render(Graphics2D g) {
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLS; x++) {
                int px = x * TILE_SIZE;
                int py = y * TILE_SIZE;

                // Ve nen truoc
                ImageLoader.load("floor.png");
                g.drawImage(
                        ImageLoader.load("floor.png"),
                        px, py, TILE_SIZE, TILE_SIZE, null
                );

                // Ve tuong hoac gach len tren nen
                tiles[y][x].render(g, px, py, TILE_SIZE);
            }
        }
    }

    public boolean isWalkable(int x, int y) {
        if (x < 0 || x >= COLS || y < 0 || y >= ROWS) {
            return false;
        }

        return tiles[y][x].isWalkable();
    }

    public boolean blocksFlame(int x, int y) {
        if (x < 0 || x >= COLS || y < 0 || y >= ROWS) {
            return true;
        }

        return tiles[y][x].blocksFlame();
    }

    public void destroyBrick(int x, int y) {
        if (x < 0 || x >= COLS || y < 0 || y >= ROWS) {
            return;
        }

        if (tiles[y][x] instanceof Brick) {
            tiles[y][x].destroy();
        }
    }

    public int getWidth() {
        return COLS * TILE_SIZE;
    }

    public int getHeight() {
        return ROWS * TILE_SIZE;
    }
}

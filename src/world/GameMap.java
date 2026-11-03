<<<<<<< HEAD
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
=======
package world;

import entities.Bomber;
import entities.Item;
import utils.ImageLoader;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameMap {
    public static final int TILE_SIZE = 40;
    private int rows = 0;
    private int cols = 0;
    private Tile[][] tiles;

    // --- BỔ SUNG: Danh sách quản lý Item và Random ---
    private List<Item> items = new ArrayList<>();
    private Random random = new Random();

    public GameMap(String fileName) {
        // Tự động quét các vị trí phổ biến của file map để không bao giờ bị FileNotFound
        String[] possiblePaths = {
                "src/resources/map/" + fileName,
                "resources/map/" + fileName,
                "res/map/" + fileName,
                fileName
        };

        File mapFile = null;
        for (String path : possiblePaths) {
            File f = new File(path);
            if (f.exists()) {
                mapFile = f;
                break;
            }
        }

        if (mapFile != null) {
            loadMapFromFile(mapFile);
        } else {
            System.err.println("Không tìm thấy file map: " + fileName + " ở bất kỳ đường dẫn nào!");
        }
    }

    private void loadMapFromFile(File file) {
        List<String> lines = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(file);
             InputStreamReader streamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(streamReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi đọc file map từ ổ đĩa: " + e.getMessage());
            e.printStackTrace();
        }

        rows = lines.size();
        if (rows > 0) {
            cols = lines.get(0).length();
        }

        tiles = new Tile[rows][cols];

        for (int y = 0; y < rows; y++) {
            String rowLine = lines.get(y);
            for (int x = 0; x < cols; x++) {
                char symbol = rowLine.charAt(x);
>>>>>>> fd4eec8 (Initial commit: Them du an Bomberman)
                tiles[y][x] = TileFactory.createTile(symbol);
            }
        }
    }

    public void render(Graphics2D g) {
<<<<<<< HEAD
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
=======
        if (tiles == null) return;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                int px = x * TILE_SIZE;
                int py = y * TILE_SIZE;

                g.drawImage(
                        ImageLoader.load("images/floor.png"),
                        px, py, TILE_SIZE, TILE_SIZE, null
                );

                if (tiles[y][x] != null) {
                    tiles[y][x].render(g, px, py, TILE_SIZE);
                }
>>>>>>> fd4eec8 (Initial commit: Them du an Bomberman)
            }
        }
    }

<<<<<<< HEAD
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
=======
    // --- BỔ SUNG: Hàm render danh sách Item rơi ra trên bản đồ ---
    public void renderItems(Graphics2D g) {
        for (Item item : items) {
            item.render(g);
        }
    }

    // --- BỔ SUNG: Hàm kiểm tra Bomber nhặt Item ---
    public void checkItems(Bomber bomber) {
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item.checkCollision(bomber) || item.isCollected()) {
                iterator.remove();
            }
        }
    }

    public boolean isWalkable(int col, int row) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return false;
        }
        return tiles[row][col].isWalkable();
    }

    public boolean blocksFlame(int col, int row) {
        if (col < 0 || col >= cols || row < 0 || row >= rows) {
            return true;
        }
        return tiles[row][col].blocksFlame();
    }

    public boolean isBrick(int col, int row) {
        if (col < 0 || col >= cols || row < 0 || row >= rows) {
            return false;
        }
        return tiles[row][col] instanceof Brick;
    }

    public void destroyBrick(int col, int row) {
        if (col < 0 || col >= cols || row < 0 || row >= rows) {
            return;
        }

        if (tiles[row][col] instanceof Brick) {
            tiles[row][col] = new Ground(col, row);

            // --- BỔ SUNG: Gạch vỡ ngẫu nhiên sinh ra Item (Speed, Flame, Bomb) ---
            if (random.nextBoolean()) {
                Item.ItemType type = Item.ItemType.SPEED_UP;
                int chance = random.nextInt(3);
                if (chance == 1) {
                    type = Item.ItemType.FLAME_UP;
                } else if (chance == 2) {
                    type = Item.ItemType.BOMB_UP;
                }

                items.add(new Item(col, row, type));
            }
>>>>>>> fd4eec8 (Initial commit: Them du an Bomberman)
        }
    }

    public int getWidth() {
<<<<<<< HEAD
        return COLS * TILE_SIZE;
    }

    public int getHeight() {
        return ROWS * TILE_SIZE;
=======
        return cols * TILE_SIZE;
    }

    public int getHeight() {
        return rows * TILE_SIZE;
>>>>>>> fd4eec8 (Initial commit: Them du an Bomberman)
    }
}

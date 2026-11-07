package world;
import javafx.scene.image.Image;
public class GameMap {

    private final char[][] tiles;

    private final int tileSize = 50;

    private int portalC = -1;
    private int portalR = -1;
    private final Image[][] wallImages;
    // Constructor dùng map TXT
    public GameMap(char[][] data, int season) {

        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Map không được rỗng");
        }

        int cols = data[0].length;

        tiles = new char[data.length][cols];
        wallImages = new Image[data.length][cols];

        for (int r = 0; r < data.length; r++) {
            for (int c = 0; c < cols; c++) {

                char tile = data[r][c];

                switch (tile) {
                    case 'P':
                    case 'E':
                    case 'C':
                        tiles[r][c] = '.';
                        break;

                    case 'O':
                        tiles[r][c] = 'O';
                        portalC = c;
                        portalR = r;
                        break;

                    default:
                        tiles[r][c] = tile;
                        break;
                }

                // Random ảnh tường một lần khi tạo map
                if (tile == '#') {
                    wallImages[r][c] = Wall.randomImage(season);
                }
            }
        }
    }


    public char get(int c, int r) {

        if (r < 0 || r >= tiles.length
                || c < 0 || c >= tiles[0].length) {
            return '#';
        }

        return tiles[r][c];
    }

    public void set(int c, int r, char v) {

        if (r >= 0 && r < tiles.length
                && c >= 0 && c < tiles[0].length) {

            tiles[r][c] = v;
        }
    }

    public boolean isWalkable(int c, int r) {

        char t = get(c, r);

        return t == '.'
                || t == 'G'
                || t == 'O';
    }

    public Image getWallImage(int c, int r) {
        return wallImages[r][c];
    }
    public int rows() {
        return tiles.length;
    }

    public int cols() {
        return tiles[0].length;
    }

    public int tileSize() {
        return tileSize;
    }

    public int pixelWidth() {
        return cols() * tileSize;
    }

    public int pixelHeight() {
        return rows() * tileSize;
    }

    public int portalCol() {
        return portalC;
    }

    public int portalRow() {
        return portalR;
    }
}

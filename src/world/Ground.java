<<<<<<< HEAD

public class Ground extends Tile {
    public Ground() {
        super("floor.png");
=======
package world;

import utils.ImageLoader;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Ground extends Tile {
    private BufferedImage groundImage;

    // 1. Constructor không tham số (để TileFactory gọi new Ground())
    public Ground() {
        super("images/floor.png");
        this.groundImage = ImageLoader.load("images/floor.png");
    }

    // 2. Constructor nhận tọa độ (để dùng khi bom nổ gọi new Ground(col, row))
    public Ground(int col, int row) {
        super("images/floor.png");
        this.groundImage = ImageLoader.load("images/floor.png");
>>>>>>> fd4eec8 (Initial commit: Them du an Bomberman)
    }

    @Override
    public boolean isWalkable() {
        return true;
    }

    @Override
    public boolean blocksFlame() {
        return false;
    }
<<<<<<< HEAD
=======

    @Override
    public void render(Graphics2D g, int x, int y, int tileSize) {
        if (groundImage != null) {
            g.drawImage(groundImage, x, y, tileSize, tileSize, null);
        }
    }
>>>>>>> fd4eec8 (Initial commit: Them du an Bomberman)
}

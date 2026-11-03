<<<<<<< HEAD

public class Brick extends Tile {
    private boolean destroyed = false;

    public Brick() {
        super("brick.png");
=======
package world;

import utils.ImageLoader;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class Brick extends Tile {
    private BufferedImage brickImage;

    // 1. Constructor không tham số (để TileFactory gọi new Brick() không bị lỗi)
    public Brick() {
        super("images/brick.png");
        this.brickImage = ImageLoader.load("images/brick.png");
    }

    // 2. Constructor nhận tọa độ (để dùng khi cần thiết)
    public Brick(int col, int row) {
        super("images/brick.png");
        this.brickImage = ImageLoader.load("images/brick.png");
>>>>>>> fd4eec8 (Initial commit: Them du an Bomberman)
    }

    @Override
    public boolean isWalkable() {
<<<<<<< HEAD
        return destroyed;
=======
        return false;
>>>>>>> fd4eec8 (Initial commit: Them du an Bomberman)
    }

    @Override
    public boolean blocksFlame() {
<<<<<<< HEAD
        return !destroyed;
    }

    @Override
    public void destroy() {
        destroyed = true;
    }

    @Override
    public void render(
            java.awt.Graphics2D g,
            int x, int y, int size) {

        if (!destroyed) {
            super.render(g, x, y, size);
=======
        return true;
    }

    @Override
    public void render(Graphics2D g, int x, int y, int tileSize) {
        if (brickImage != null) {
            g.drawImage(brickImage, x, y, tileSize, tileSize, null);
        } else {
            g.setColor(Color.GRAY);
            g.fillRect(x, y, tileSize, tileSize);
>>>>>>> fd4eec8 (Initial commit: Them du an Bomberman)
        }
    }
}

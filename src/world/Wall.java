<<<<<<< HEAD

public class Wall extends Tile {
    public Wall() {
        super("wall.png");
=======
package world;

import utils.ImageLoader;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class Wall extends Tile {
    private BufferedImage wallImage;

    public Wall() {
        super("images/wall.png");
        this.wallImage = ImageLoader.load("images/wall.png");
    }

    public Wall(int col, int row) {
        super("images/wall.png");
        this.wallImage = ImageLoader.load("images/wall.png");
>>>>>>> fd4eec8 (Initial commit: Them du an Bomberman)
    }

    @Override
    public boolean isWalkable() {
<<<<<<< HEAD
        return false;
=======
        return false; // Tường chặn đường đi
>>>>>>> fd4eec8 (Initial commit: Them du an Bomberman)
    }

    @Override
    public boolean blocksFlame() {
<<<<<<< HEAD
        return true;
=======
        return true; // Tường chặn lửa
    }

    @Override
    public void render(Graphics2D g, int x, int y, int tileSize) {
        if (wallImage != null) {
            g.drawImage(wallImage, x, y, tileSize, tileSize, null);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(x, y, tileSize, tileSize);
        }
>>>>>>> fd4eec8 (Initial commit: Them du an Bomberman)
    }
}

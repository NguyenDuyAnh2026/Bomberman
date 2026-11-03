<<<<<<< HEAD

public class Portal extends Tile {
    public Portal() {
        super("portal.png");
=======
package world;

import utils.ImageLoader;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class Portal extends Tile {
    private BufferedImage portalImage;

    public Portal() {
        super("images/portal.png");
        this.portalImage = ImageLoader.load("images/portal.png");
    }

    public Portal(int col, int row) {
        super("images/portal.png");
        this.portalImage = ImageLoader.load("images/portal.png");
>>>>>>> fd4eec8 (Initial commit: Them du an Bomberman)
    }

    @Override
    public boolean isWalkable() {
<<<<<<< HEAD
        return true;
=======
        return true; // Cho phép đi vào cổng (qua màn)
>>>>>>> fd4eec8 (Initial commit: Them du an Bomberman)
    }

    @Override
    public boolean blocksFlame() {
<<<<<<< HEAD
        return false;
=======
        return false; // Lửa không bị chặn bởi cổng
    }

    @Override
    public void render(Graphics2D g, int x, int y, int tileSize) {
        if (portalImage != null) {
            g.drawImage(portalImage, x, y, tileSize, tileSize, null);
        } else {
            g.setColor(Color.MAGENTA);
            g.fillRect(x, y, tileSize, tileSize);
        }
>>>>>>> fd4eec8 (Initial commit: Them du an Bomberman)
    }
}


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class Tile {
    protected final BufferedImage image;

    protected Tile(String imageName) {
        image = ImageLoader.load(imageName);
    }

    public abstract boolean isWalkable();

    public abstract boolean blocksFlame();

    public void render(Graphics2D g, int x, int y, int size) {
        g.drawImage(image, x, y, size, size, null);
    }

    public void destroy() {
        // Mac dinh khong the pha
    }
}

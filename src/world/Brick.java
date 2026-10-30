
public class Brick extends Tile {
    private boolean destroyed = false;

    public Brick() {
        super("brick.png");
    }

    @Override
    public boolean isWalkable() {
        return destroyed;
    }

    @Override
    public boolean blocksFlame() {
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
        }
    }
}

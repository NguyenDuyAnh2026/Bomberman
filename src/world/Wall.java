
public class Wall extends Tile {
    public Wall() {
        super("wall.png");
    }

    @Override
    public boolean isWalkable() {
        return false;
    }

    @Override
    public boolean blocksFlame() {
        return true;
    }
}

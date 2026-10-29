
public class Ground extends Tile {
    public Ground() {
        super("floor.png");
    }

    @Override
    public boolean isWalkable() {
        return true;
    }

    @Override
    public boolean blocksFlame() {
        return false;
    }
}

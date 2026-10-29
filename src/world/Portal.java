
public class Portal extends Tile {
    public Portal() {
        super("portal.png");
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

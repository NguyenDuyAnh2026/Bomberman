package world;

import javafx.scene.image.Image;

public class Brick extends Tile {
    public static final Image IMAGE = new Image(
            Brick.class.getResourceAsStream("/images/brick.png")
    );
    public Brick() {
        super('B');
    }
}

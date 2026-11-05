package world;

import javafx.scene.image.Image;

public class Portal extends Tile {
    public static final Image CLOSED_IMAGE = new Image(
            Portal.class.getResourceAsStream("/images/portal_closed.png")
    );

    public static final Image OPEN_IMAGE = new Image(
            Portal.class.getResourceAsStream("/images/portal_open.png")
    );
    public Portal() {
        super('G');
    }
}

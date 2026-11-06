package world;

import javafx.scene.image.Image;
import java.util.Random;

public class Wall extends Tile {

    public static final Image[] IMAGES = {
            new Image(Wall.class.getResourceAsStream("/images/wall1.png")),
            new Image(Wall.class.getResourceAsStream("/images/wall2.png")),
            new Image(Wall.class.getResourceAsStream("/images/wall3.png"))
    };

    private static final Random random = new Random();

    public Wall() {
        super('#');
    }

    public static Image randomImage() {
        int n = random.nextInt(10) + 1;

        if (n <= 2) {
            return IMAGES[0]; // 20%
        } else if (n <= 5) {
            return IMAGES[1]; // 20%
        } else {
            return IMAGES[2]; // 60%
        }
    }
}

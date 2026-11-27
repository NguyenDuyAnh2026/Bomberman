
package world;

import javafx.scene.image.Image;
import java.util.Random;

public class Wall extends Tile {

    // Spring
    private static final Image[] SPRING_IMAGES = {
            loadImage("/images/wall1.png"),
            loadImage("/images/wall2.png"),
            loadImage("/images/wall3.png")
    };

    // Summer
    private static final Image[] SUMMER_IMAGES = {
            loadImage("/images/summer01.png"),
            loadImage("/images/summer02.png"),
            loadImage("/images/summer03.png")
    };

    // Autumn
    private static final Image[] AUTUMN_IMAGES = {
            loadImage("/images/autumn01.png"),
            loadImage("/images/autumn02.png"),
            loadImage("/images/autumn03.png")
    };

    // Winter
    private static final Image[] WINTER_IMAGES = {
            loadImage("/images/winter01.png"),
            loadImage("/images/winter02.png"),
            loadImage("/images/winter03.png")
    };

    private static final Random random = new Random();

    public Wall() {
        super('#');
    }

    private static Image loadImage(String path) {
        return new Image(Wall.class.getResourceAsStream(path));
    }

    public static Image randomImage(int season) {

        Image[] images;

        switch (season) {
            case 2:
                images = SUMMER_IMAGES;
                break;

            case 3:
                images = AUTUMN_IMAGES;
                break;

            case 4:
                images = WINTER_IMAGES;
                break;

            case 1:
            default:
                images = SPRING_IMAGES;
                break;
        }

        // Giữ phân bố texture như code cũ:
        // 20% ảnh 1, 30% ảnh 2, 50% ảnh 3
        int n = random.nextInt(10) + 1;

        if (n <= 2) {
            return images[0];
        } else if (n <= 5) {
            return images[1];
        } else {
            return images[2];
        }
    }
}

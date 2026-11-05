package entities;

import javafx.scene.image.Image;

public class Bomber extends MovingEntity {
    public int lives = 3, maxBombs = 1, flameRange = 2;
    public double moveSpeed = 0.13;

    public double x, y;
    public double startX, startY;
    public double moveProgress = 1.0;

    public static final Image IMAGE = new Image(
            Bomber.class.getResourceAsStream("/images/bomber.png")
    );

    public Bomber(int col, int row) {
        super(col, row);

        x = col;
        y = row;

        startX = col;
        startY = row;
    }
}

package entities;

import javafx.scene.image.Image;

public class ChasingEnemy extends Enemy {

    public static final Image IMAGE = new Image(
            ChasingEnemy.class.getResourceAsStream(
                    "/images/chasing_enemy.png"
            )
    );

    public ChasingEnemy(int col, int row) {
        super(col, row);

        moveSpeed = 1.8; // tốc độ đuổi
        timer = 0;
    }
}

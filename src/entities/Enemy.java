package entities;

import javafx.scene.image.Image;


public class Enemy extends MovingEntity {
    public double x, y;
    public double moveSpeed = 1.5;
    public int targetCol, targetRow;
    public boolean moving = false;
    public double timer=.3;
    public static final Image IMAGE =
            new Image(Enemy.class.getResourceAsStream("/images/enemy.png"));
    public Enemy(int col,int row) {
        super(col,row);
        x = col;
        y = row;
        targetCol = col;
        targetRow = row;
    }
}

package entities;

import javafx.scene.image.Image;

public class Bomb extends GameObject {
    public double timer=1.7;
    public final int range;
    public static final Image IMAGE = new Image(
            Bomb.class.getResourceAsStream("/images/bomb.png")
    );
    public Bomb(int col,int row,int range) {
        super(col,row);
        this.range=range;
    }
}

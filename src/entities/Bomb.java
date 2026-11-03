package entities;

public class Bomb extends GameObject {
    public double timer=1.7;
    public final int range;
    public Bomb(int col,int row,int range) {
        super(col,row);
        this.range=range;
    }
}

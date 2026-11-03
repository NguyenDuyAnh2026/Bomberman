package entities;

public abstract class GameObject {
    public int col,row;
    protected GameObject(int col,int row) {
        this.col=col;
        this.row=row;
    }
}

package utils;

public final class CollisionManager {
    private CollisionManager() {
    }
    public static boolean sameCell(int ax,int ay,int bx,int by) {
        return ax==bx&&ay==by;
    }
}

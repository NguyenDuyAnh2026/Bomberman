package entities;

import world.GameMap;
import java.awt.Graphics2D;
import java.awt.Color;

public class Bomber {
    private int x, y; // Tọa độ pixel là int
    private int speed = 2; // Tốc độ di chuyển
    private int width = 32;  // Hitbox nhỏ hơn TILE_SIZE (40) để dễ chui khe
    private int height = 32;

    public Bomber(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    // Kiểm tra va chạm với map dựa trên hitbox thu nhỏ
    public boolean canMoveTo(int nextX, int nextY, GameMap map) {
        int margin = 6; // Độ lỏng để lách khe
        int left = (nextX + margin) / GameMap.TILE_SIZE;
        int right = (nextX + width - margin) / GameMap.TILE_SIZE;
        int top = (int) (nextY + margin + (height / 2)) / GameMap.TILE_SIZE;
        int bottom = (nextY + height - margin) / GameMap.TILE_SIZE;

        boolean topLeft = map.isWalkable(left, top);
        boolean topRight = map.isWalkable(right, top);
        boolean bottomLeft = map.isWalkable(left, bottom);
        boolean bottomRight = map.isWalkable(right, bottom);

        return topLeft && topRight && bottomLeft && bottomRight;
    }

    public void update(GameMap map, boolean up, boolean down, boolean left, boolean right) {
        int nextX = x;
        int nextY = y;

        if (up) nextY -= speed;
        if (down) nextY += speed;
        if (left) nextX -= speed;
        if (right) nextX += speed;

        // Trượt tường mượt mà khi chui khe
        if (canMoveTo(nextX, y, map)) {
            x = nextX;
        }
        if (canMoveTo(x, nextY, map)) {
            y = nextY;
        }
    }

    public void render(Graphics2D g) {
        int drawOffset = (GameMap.TILE_SIZE - width) / 2;

        // Vẽ khối màu đỏ tượng trưng cho nhân vật (không sợ mất ảnh hay tàng hình)
        g.setColor(Color.RED);
        g.fillRect(x + drawOffset, y + drawOffset, width, height);

        // Viền đen quanh khối đỏ cho sắc nét
        g.setColor(Color.BLACK);
        g.drawRect(x + drawOffset, y + drawOffset, width, height);
    }

    public int getCol() {
        return (x + width / 2) / GameMap.TILE_SIZE;
    }

    public int getRow() {
        return (y + height / 2) / GameMap.TILE_SIZE;
    }
    // Tăng tốc độ di chuyển của Bomber khi nhặt item
    public void increaseSpeed(int amount) {
        this.speed += amount;
    }
}

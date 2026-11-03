package entities;

import world.GameMap;
import java.awt.Graphics2D;
import java.awt.Color;

public class Item {
    public enum ItemType {
        SPEED_UP, FLAME_UP, BOMB_UP
    }

    private int col, row;
    private ItemType type;
    private boolean collected = false;

    public Item(int col, int row, ItemType type) {
        this.col = col;
        this.row = row;
        this.type = type;
    }

    public void render(Graphics2D g) {
        if (collected) return;
        int px = col * GameMap.TILE_SIZE;
        int py = row * GameMap.TILE_SIZE;

        // Vẽ tạm Item hình tròn màu vàng/xanh để dễ nhận diện
        g.setColor(Color.YELLOW);
        g.fillOval(px + 8, py + 8, GameMap.TILE_SIZE - 16, GameMap.TILE_SIZE - 16);
        g.setColor(Color.BLACK);
        g.drawOval(px + 8, py + 8, GameMap.TILE_SIZE - 16, GameMap.TILE_SIZE - 16);
    }

    // Kiểm tra Bomber có nhặt được item không
    public boolean checkCollision(Bomber bomber) {
        if (collected) return false;

        int bCol = bomber.getCol();
        int bRow = bomber.getRow();

        if (this.col == bCol && this.row == bRow) {
            collected = true;
            applyEffect(bomber);
            return true;
        }
        return false;
    }

    private void applyEffect(Bomber bomber) {
        switch (type) {
            case SPEED_UP:
                bomber.increaseSpeed(1);
                System.out.println("Nhặt được Item: Tăng tốc độ!");
                break;
            case FLAME_UP:
                // bomber.increaseFlame(); // Thêm hàm này vào Bomber nếu muốn
                System.out.println("Nhặt được Item: Tăng tầm nổ!");
                break;
            case BOMB_UP:
                // bomber.increaseMaxBombs();
                System.out.println("Nhặt được Item: Tăng số lượng bom!");
                break;
        }
    }

    public boolean isCollected() {
        return collected;
    }
}

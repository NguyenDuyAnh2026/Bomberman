package entities;

import utils.ImageLoader;
import world.GameMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.Random;
import java.util.List;

public class Enemy {
    private int x, y;
    private int speed = 2;
    private int dx = 0, dy = 0;
    private BufferedImage enemyImage;
    private Random random = new Random();
    private int moveTimer = 0;

    public Enemy(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.enemyImage = ImageLoader.load("images/enemy.png");
        chooseRandomDirection();
    }

    // Nhận thêm danh sách activeBombs vào hàm update
    public void update(GameMap map, Bomber bomber, List<Bomb> activeBombs) {
        int distanceX = Math.abs(this.x - bomber.getX());
        int distanceY = Math.abs(this.y - bomber.getY());
        int detectionRange = GameMap.TILE_SIZE * 5;

        if (distanceX + distanceY < detectionRange) {
            if (random.nextInt(10) < 8) {
                dx = 0;
                dy = 0;
                if (bomber.getX() < this.x) dx = -1;
                else if (bomber.getX() > this.x) dx = 1;

                if (bomber.getY() < this.y) dy = -1;
                else if (bomber.getY() > this.y) dy = 1;
            } else {
                updateRandomWander();
            }
        } else {
            updateRandomWander();
        }

        // Tách biệt trục X và Y để trượt tường mượt mà
        boolean moved = false;

        if (dx != 0) {
            int newX = x + dx * speed;
            if (canMove(newX, y, map, activeBombs)) {
                x = newX;
                moved = true;
            }
        }

        if (dy != 0) {
            int newY = y + dy * speed;
            if (canMove(x, newY, map, activeBombs)) {
                y = newY;
                moved = true;
            }
        }

        // Nếu bị kẹt hoặc đụng bom, lập tức đổi hướng mới ngay lập tức
        if (!moved) {
            chooseRandomDirection();
            moveTimer = 0;
        }
    }

    private void updateRandomWander() {
        moveTimer++;
        if (moveTimer > 45 || (dx == 0 && dy == 0)) {
            chooseRandomDirection();
            moveTimer = 0;
        }
    }

    private void chooseRandomDirection() {
        int choice = random.nextInt(4);
        dx = 0;
        dy = 0;
        switch (choice) {
            case 0: dy = -1; break; // Lên
            case 1: dy = 1; break;  // Xuống
            case 2: dx = -1; break; // Trái
            case 3: dx = 1; break;  // Phải
        }
    }

    private boolean canMove(int targetX, int targetY, GameMap map, List<Bomb> activeBombs) {
        int tileSize = GameMap.TILE_SIZE;
        int margin = 4;

        int leftTile = (targetX + margin) / tileSize;
        int rightTile = (targetX + tileSize - margin - 1) / tileSize;
        int topTile = (targetY + margin) / tileSize;
        int bottomTile = (targetY + tileSize - margin - 1) / tileSize;

        // 1. Kiểm tra va chạm bản đồ (tường, gạch)
        boolean walkable = map.isWalkable(leftTile, topTile) &&
                map.isWalkable(rightTile, topTile) &&
                map.isWalkable(leftTile, bottomTile) &&
                map.isWalkable(rightTile, bottomTile);

        if (!walkable) {
            return false;
        }

        // 2. Kiểm tra va chạm với bom: Quái hoàn toàn không được đi vào ô có bom
        if (activeBombs != null) {
            int targetCenterCol = (targetX + tileSize / 2) / tileSize;
            int targetCenterRow = (targetY + tileSize / 2) / tileSize;

            for (Bomb bomb : activeBombs) {
                if (bomb.getCol() == targetCenterCol && bomb.getRow() == targetCenterRow) {
                    return false; // Chặn đứng quái, không cho đi vào ô chứa bom
                }
            }
        }

        return true;
    }

    public void render(Graphics2D g) {
        if (enemyImage != null) {
            g.drawImage(enemyImage, x, y, GameMap.TILE_SIZE, GameMap.TILE_SIZE, null);
        } else {
            g.setColor(Color.ORANGE);
            g.fillRect(x + 4, y + 4, GameMap.TILE_SIZE - 8, GameMap.TILE_SIZE - 8);
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
}

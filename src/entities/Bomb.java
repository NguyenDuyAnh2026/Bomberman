package entities;

import utils.ImageLoader;
import world.GameMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.List;

public class Bomb {
    private int col, row;
    private int countdown = 180; // Thời gian đếm ngược trước khi nổ (180 frames ~ 3 giây)
    private boolean exploded = false;
    private int explosionDuration = 30; // Thời gian hiệu ứng lửa tồn tại sau khi nổ (30 frames)

    private int flameLength; // Tầm nổ của bom nhận từ Bomber

    private BufferedImage bombImage;
    private BufferedImage flameImage;

    // Constructor 1: Nhận 2 tham số (mặc định tầm nổ = 2)
    public Bomb(int col, int row) {
        this.col = col;
        this.row = row;
        this.flameLength = 2;
        this.bombImage = ImageLoader.load("images/bomb.png");
        this.flameImage = ImageLoader.load("images/flame.png");
    }

    // Constructor 2: Nhận 3 tham số (tọa độ + tầm nổ chuẩn từ Bomber)
    public Bomb(int col, int row, int flameLength) {
        this.col = col;
        this.row = row;
        this.flameLength = flameLength;
        this.bombImage = ImageLoader.load("images/bomb.png");
        this.flameImage = ImageLoader.load("images/flame.png");
    }

    public void update(GameMap map, List<Enemy> enemies, Bomber bomber) {
        if (!exploded) {
            countdown--;
            if (countdown <= 0) {
                explode(map, enemies, bomber);
            }
        } else {
            explosionDuration--;
        }
    }

    private void explode(GameMap map, List<Enemy> enemies, Bomber bomber) {
        exploded = true;

        // 1. Kiểm tra sát thương tại ô tâm nổ
        checkFlameHit(col, row, map, enemies, bomber);

        // 2. Lan lửa 4 hướng: Lên, Xuống, Trái, Phải
        int[][] directions = {
                {0, -1}, // Lên
                {0, 1},  // Xuống
                {-1, 0}, // Trái
                {1, 0}   // Phải
        };

        for (int[] dir : directions) {
            int dCol = dir[0];
            int dRow = dir[1];

            for (int i = 1; i <= flameLength; i++) {
                int targetCol = col + (i * dCol);
                int targetRow = row + (i * dRow);

                // Nếu gặp tường cố định (chặn lửa và không phải gạch) thì dừng tia lửa hướng này
                if (map.blocksFlame(targetCol, targetRow) && !map.isBrick(targetCol, targetRow)) {
                    break;
                }

                // Nếu gặp gạch (Brick), tiến hành phá hủy gạch và dừng tia lửa hướng đó lại ngay
                if (map.isBrick(targetCol, targetRow)) {
                    map.destroyBrick(targetCol, targetRow);
                    break;
                }

                // Kiểm tra sát thương quái vật / bomber tại ô lửa lan tới
                checkFlameHit(targetCol, targetRow, map, enemies, bomber);

                // Nếu gặp vật cản không đi được khác thì dừng
                if (!map.isWalkable(targetCol, targetRow)) {
                    break;
                }
            }
        }
    }

    // Kiểm tra ngọn lửa tại ô (c, r) có tiêu diệt enemy, bomber hay phá gạch không
    private boolean checkFlameHit(int c, int r, GameMap map, List<Enemy> enemies, Bomber bomber) {
        if (map.isBrick(c, r)) {
            map.destroyBrick(c, r);
            return true;
        }

        int tileSize = GameMap.TILE_SIZE;
        int flameCenterX = c * tileSize + tileSize / 2;
        int flameCenterY = r * tileSize + tileSize / 2;

        // Tiêu diệt quái vật nếu đứng trúng ô lửa
        if (enemies != null) {
            enemies.removeIf(enemy -> {
                int enemyCenterX = enemy.getX() + tileSize / 2;
                int enemyCenterY = enemy.getY() + tileSize / 2;
                return Math.abs(enemyCenterX - flameCenterX) < tileSize / 2 &&
                        Math.abs(enemyCenterY - flameCenterY) < tileSize / 2;
            });
        }

        // Kiểm tra sát thương Bomber nếu đứng trúng ô lửa
        if (bomber != null) {
            int bomberCenterX = bomber.getX() + tileSize / 2;
            int bomberCenterY = bomber.getY() + tileSize / 2;
            if (Math.abs(bomberCenterX - flameCenterX) < tileSize / 2 &&
                    Math.abs(bomberCenterY - flameCenterY) < tileSize / 2) {
                // Xử lý Bomber mất mạng ở đây nếu cần
            }
        }

        return false;
    }

    public void render(Graphics2D g) {
        int tileSize = GameMap.TILE_SIZE;

        if (!exploded) {
            if (bombImage != null) {
                g.drawImage(bombImage, col * tileSize, row * tileSize, tileSize, tileSize, null);
            } else {
                g.setColor(Color.RED);
                g.fillRect(col * tileSize + 8, row * tileSize + 8, tileSize - 16, tileSize - 16);
            }
        } else if (explosionDuration > 0) {
            drawFlameCross(g, tileSize);
        }
    }

    private void drawFlameCross(Graphics2D g, int tileSize) {
        drawSingleFlame(g, col, row, tileSize);

        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        for (int[] dir : directions) {
            int dCol = dir[0];
            int dRow = dir[1];

            for (int i = 1; i <= flameLength; i++) {
                int targetCol = col + (i * dCol);
                int targetRow = row + (i * dRow);

                // Nếu gặp tường/gạch thì dừng vẽ hiệu ứng lửa ở hướng đó
                if (mapBlocksOrBrick(targetCol, targetRow)) {
                    break;
                }

                drawSingleFlame(g, targetCol, targetRow, tileSize);
            }
        }
    }

    // Hàm phụ trợ cho việc render hiệu ứng lửa không xuyên tường/gạch
    private boolean mapBlocksOrBrick(int c, int r) {
        // Có thể bổ sung kiểm tra nhanh nếu cần, hoặc dùng trực tiếp GameMap nếu truyền vào
        return false;
    }

    private void drawSingleFlame(Graphics2D g, int c, int r, int tileSize) {
        if (flameImage != null) {
            g.drawImage(flameImage, c * tileSize, r * tileSize, tileSize, tileSize, null);
        } else {
            g.setColor(Color.ORANGE);
            g.fillRect(c * tileSize + 4, r * tileSize + 4, tileSize - 8, tileSize - 8);
        }
    }

    public boolean isExploded() {
        return exploded && explosionDuration <= 0;
    }

    public int getCol() { return col; }
    public int getRow() { return row; }
}

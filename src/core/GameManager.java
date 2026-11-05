
package core;

import utils.SoundManager;
import entities.Bomber;
import entities.Bomb;
import entities.Enemy;
import utils.InputHandler;
import world.GameMap;
import javafx.scene.image.Image;
import java.util.*;

public class GameManager {
    private GameMap map;
    private Bomber player;

    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Bomb> bombs = new ArrayList<>();
    private final List<Flame> flames = new ArrayList<>();
    private final List<Pickup> pickups = new ArrayList<>();

    private GameState state = GameState.PLAYING;

    private double bombCooldown = 0;
    private boolean spaceWasDown = false;

    private final Random random = new Random();

    public GameManager() {
        restart();
    }

    public void restart() {
        map = new GameMap();
        player = new Bomber(1, 1);

        enemies.clear();
        bombs.clear();
        flames.clear();
        pickups.clear();

        state = GameState.PLAYING;
        bombCooldown = 0;
        spaceWasDown = false;

        int[][] spots = {
                {11, 1},
                {13, 5},
                {7, 11},
                {13, 11}
        };

        for (int[] s : spots) {
            if (map.isWalkable(s[0], s[1])) {
                enemies.add(new Enemy(s[0], s[1]));
            }
        }
    }

    public void update(double dt, InputHandler input) {
        if (state != GameState.PLAYING) return;

        bombCooldown -= dt;

        // ===== DI CHUYỂN MƯỢT =====

        if (player.moveProgress < 1.0) {
            player.moveProgress += dt / player.moveSpeed;

            if (player.moveProgress > 1.0) {
                player.moveProgress = 1.0;
            }

            player.x = player.startX
                    + (player.col - player.startX)
                    * player.moveProgress;

            player.y = player.startY
                    + (player.row - player.startY)
                    * player.moveProgress;
        }

        // Khi đã đến ô đích
        if (player.moveProgress >= 1.0) {
            player.x = player.col;
            player.y = player.row;

            int dx = 0;
            int dy = 0;

            if (input.up()) {
                dy = -1;
            } else if (input.down()) {
                dy = 1;
            } else if (input.left()) {
                dx = -1;
            } else if (input.right()) {
                dx = 1;
            }

            if (dx != 0 || dy != 0) {
                int nx = player.col + dx;
                int ny = player.row + dy;

                if (map.isWalkable(nx, ny) && !bombAt(nx, ny)) {

                    player.startX = player.x;
                    player.startY = player.y;

                    player.col = nx;
                    player.row = ny;

                    player.moveProgress = 0.0;

                    collectPickup(nx, ny);
                }
            }
        }

        // ===== ĐẶT BOM =====

        boolean space = input.space();

        if (space
                && !spaceWasDown
                && bombCooldown <= 0
                && bombs.size() < player.maxBombs
                && !bombAt(player.col, player.row)) {

            bombs.add(new Bomb(
                    player.col,
                    player.row,
                    player.flameRange
            ));

            bombCooldown = 0.25;
        }

        spaceWasDown = space;

        // ===== BOM NỔ =====

        Iterator<Bomb> it = bombs.iterator();

        while (it.hasNext()) {
            Bomb b = it.next();

            b.timer -= dt;

            if (b.timer <= 0) {
                explode(b);
                it.remove();
            }
        }

        // ===== LỬA =====

        Iterator<Flame> fi = flames.iterator();

        while (fi.hasNext()) {
            Flame f = fi.next();

            f.timer -= dt;

            if (f.timer <= 0) {
                fi.remove();
            }
        }

        // ===== QUÁI DI CHUYỂN =====

        for (Enemy e : enemies) {
            e.timer -= dt;

            // Nếu Enemy đang di chuyển tới ô mục tiêu
            if (e.moving) {
                double dx = e.targetCol - e.x;
                double dy = e.targetRow - e.y;

                double distance = Math.sqrt(dx * dx + dy * dy);
                double step = e.moveSpeed * dt;

                if (distance <= step) {
                    e.x = e.targetCol;
                    e.y = e.targetRow;

                    e.col = e.targetCol;
                    e.row = e.targetRow;

                    e.moving = false;
                } else {
                    e.x += dx / distance * step;
                    e.y += dy / distance * step;
                }
            }

            // Chỉ chọn hướng mới khi Enemy đã tới ô đích
            if (!e.moving && e.timer <= 0) {
                int[][] dirs = {
                        {1, 0},
                        {-1, 0},
                        {0, 1},
                        {0, -1}
                };

                int[] d = dirs[random.nextInt(4)];

                int nx = e.col + d[0];
                int ny = e.row + d[1];

                if (map.isWalkable(nx, ny) && !bombAt(nx, ny)) {
                    e.targetCol = nx;
                    e.targetRow = ny;
                    e.moving = true;
                }

                e.timer = 0.45 + random.nextDouble() * 0.4;
            }
        }

        // ===== LỬA TRÚNG NHÂN VẬT =====

        for (Flame f : flames) {
            if (player.col == f.col && player.row == f.row) {
                player.lives--;

                resetPlayerPosition();

                if (player.lives <= 0) {
                    state = GameState.LOST;
                }
            }

            enemies.removeIf(e ->
                    e.col == f.col && e.row == f.row
            );
        }

        // ===== QUÁI CHẠM NHÂN VẬT =====

        for (Enemy enemy : enemies) {
            if (enemy.col == player.col
                    && enemy.row == player.row) {

                player.lives--;

                resetPlayerPosition();

                if (player.lives <= 0) {
                    state = GameState.LOST;
                }

                break;
            }
        }

        // ===== KIỂM TRA THẮNG =====

        if (enemies.isEmpty()
                && player.col == map.portalCol()
                && player.row == map.portalRow()) {

            state = GameState.WON;
        }
    }

    private void resetPlayerPosition() {
        player.col = 1;
        player.row = 1;

        player.x = 1;
        player.y = 1;

        player.startX = 1;
        player.startY = 1;

        player.moveProgress = 1.0;
    }

    private boolean bombAt(int c, int r) {
        return bombs.stream().anyMatch(
                b -> b.col == c && b.row == r
        );
    }

    private void explode(Bomb b) {
        SoundManager.playBomb();

        addFlame(b.col, b.row);

        // Phần logic nổ bom còn lại giữ nguyên
        int[][] dirs = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };

        for (int[] d : dirs) {
            for (int i = 1; i <= b.range; i++) {
                int x = b.col + d[0] * i;
                int y = b.row + d[1] * i;

                char tile = map.get(x, y);

                if (tile == '#') {
                    break;
                }

                if (tile == 'B') {
                    map.set(x, y, '.');

                    if (random.nextDouble() < 0.35) {
                        String[] types = {
                                "BOMB",
                                "FLAME",
                                "SPEED",
                                "LIFE"
                        };

                        pickups.add(new Pickup(
                                x,
                                y,
                                types[random.nextInt(types.length)]
                        ));
                    }

                    addFlame(x, y);
                    break;
                }

                addFlame(x, y);
            }
        }
    }

    private void addFlame(int c, int r) {
        flames.add(new Flame(c, r));
    }

    private void collectPickup(int c, int r) {
        Iterator<Pickup> it = pickups.iterator();

        while (it.hasNext()) {
            Pickup p = it.next();

            if (p.col == c && p.row == r) {
                SoundManager.playPickup();

                switch (p.type) {
                    case "BOMB":
                        player.maxBombs++;
                        break;

                    case "FLAME":
                        player.flameRange++;
                        break;

                    case "SPEED":
                        player.moveSpeed = Math.max(
                                0.07,
                                player.moveSpeed - 0.015
                        );
                        break;

                    case "LIFE":
                        player.lives++;
                        break;
                }

                it.remove();
            }
        }
    }

    public GameMap getMap() {
        return map;
    }

    public Bomber getPlayer() {
        return player;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public List<Flame> getFlames() {
        return flames;
    }

    public List<Pickup> getPickups() {
        return pickups;
    }

    public GameState getState() {
        return state;
    }

    public void togglePause() {
        if (state == GameState.PLAYING) {
            state = GameState.PAUSED;
        } else if (state == GameState.PAUSED) {
            state = GameState.PLAYING;
        }
    }

    public static class Flame {
        public static final Image IMAGE = new Image(
                Flame.class.getResourceAsStream("/images/flame.png")
        );
        public final int col, row;
        public double timer = 0.55;

        public Flame(int c, int r) {
            col = c;
            row = r;
        }
    }

    public static class Pickup {
        public final int col, row;
        public final String type;

        public Pickup(int c, int r, String t) {
            col = c;
            row = r;
            type = t;
        }
    }
}

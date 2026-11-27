
package core;

import utils.SoundManager;
import entities.Bomber;
import entities.Bomb;
import entities.ChasingEnemy;
import entities.Enemy;
import utils.InputHandler;
import world.GameMap;
import javafx.scene.image.Image;
import world.MapLoader;

import java.util.*;
import pathfinding.PathFinder;

public class GameManager {
    private GameMap map;
    private Bomber player;
    private int currentLevel = 1;
    private int playerStartCol = 1;
    private int playerStartRow = 1;
    private final int MAX_LEVEL = 4;

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
        currentLevel = 1;
        loadLevel("resources/map/map1.txt");
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

        // ===== CẬP NHẬT ANIMATION NARUTO =====

        boolean moving = player.moveProgress < 1.0;

        Bomber.Direction direction = Bomber.Direction.DOWN;

        if (moving) {
            double dx = player.col - player.startX;
            double dy = player.row - player.startY;

            if (dx > 0) {
                direction = Bomber.Direction.RIGHT;
            } else if (dx < 0) {
                direction = Bomber.Direction.LEFT;
            } else if (dy > 0) {
                direction = Bomber.Direction.DOWN;
            } else if (dy < 0) {
                direction = Bomber.Direction.UP;
            }
        }

        player.updateBomberAnimation(dt, moving, direction);

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

        List<Bomb> expiredBombs = new ArrayList<>();

        Iterator<Bomb> it = bombs.iterator();

        while (it.hasNext()) {
            Bomb b = it.next();

            b.timer -= dt;

            if (b.timer <= 0) {
                expiredBombs.add(b);
                it.remove();
            }
        }

        // Kích nổ các bom hết thời gian và xử lý chain reaction
        if (!expiredBombs.isEmpty()) {
            detonateBombs(expiredBombs);
        }

        // ===== LỬA =====
        Iterator<Flame> fi = flames.iterator();

        while (fi.hasNext()) {
            Flame f = fi.next();

            // Chờ lửa xuất hiện
            if (f.delay > 0) {
                f.delay -= dt;
            }

            // Khi lửa đã xuất hiện
            else {
                f.timer -= dt;

                if (f.timer <= 0) {
                    fi.remove();
                }
            }
        }
        // ===== QUÁI DI CHUYỂN =====

        for (Enemy e : enemies) {
            e.timer -= dt;

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

            if (!e.moving && e.timer <= 0) {

                if (e instanceof ChasingEnemy) {

                    int playerCol = (int) Math.round(player.x);
                    int playerRow = (int) Math.round(player.y);

                    List<int[]> path = PathFinder.findPath(
                            map,
                            e.col, e.row,
                            playerCol, playerRow
                    );

                    if (!path.isEmpty()) {
                        int[] nextStep = path.get(0);

                        int nx = nextStep[0];
                        int ny = nextStep[1];

                        if (map.isWalkable(nx, ny) && !bombAt(nx, ny)) {
                            e.targetCol = nx;
                            e.targetRow = ny;
                            e.moving = true;
                        }
                    }

                    e.timer = 0.2;
                } else {
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
        }

        // ===== KIỂM TRA LỬA TRÚNG NHÂN VẬT =====

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

        // ===== KIỂM TRA CHUYỂN MÀN =====

        if (enemies.isEmpty()
                && player.col == map.portalCol()
                && player.row == map.portalRow()) {

            nextLevel();
        }
    }

    private void resetPlayerPosition() {
        player.col = playerStartCol;
        player.row = playerStartRow;

        player.x = playerStartCol;
        player.y = playerStartRow;

        player.startX = playerStartCol;
        player.startY = playerStartRow;

        player.moveProgress = 1.0;
    }

    private boolean bombAt(int c, int r) {
        return bombs.stream().anyMatch(
                b -> b.col == c && b.row == r
        );
    }

// ===== XỬ LÝ NỔ BOM VÀ CHAIN REACTION =====

    private void detonateBombs(List<Bomb> initialBombs) {

        Queue<Bomb> queue = new ArrayDeque<>();
        Set<Bomb> scheduledBombs = new HashSet<>();

        // Đưa những bom ban đầu vào hàng đợi
        for (Bomb b : initialBombs) {
            if (scheduledBombs.add(b)) {
                queue.add(b);
            }
        }

        // Xử lý lần lượt các bom trong hàng đợi
        while (!queue.isEmpty()) {

            Bomb b = queue.poll();

            explode(b, queue, scheduledBombs);
        }

        // Xóa tất cả bom đã phát nổ khỏi danh sách
        bombs.removeAll(scheduledBombs);
    }


    // Tìm bom tại một tọa độ
    private Bomb findBombAt(int col, int row) {

        for (Bomb b : bombs) {
            if (b.col == col && b.row == row) {
                return b;
            }
        }

        return null;
    }


    // Xử lý vùng nổ của một quả bom
    private void explode(
            Bomb b,
            Queue<Bomb> queue,
            Set<Bomb> scheduledBombs
    ) {

        SoundManager.playBomb();

        // Tâm vụ nổ
        addFlame(
                b.col, b.row, 0,
                Flame.Type.CENTER,
                Flame.Direction.NONE
        );

        int[][] dirs = {
                {1, 0},    // Phải
                {-1, 0},   // Trái
                {0, 1},    // Xuống
                {0, -1}    // Lên
        };

        Flame.Direction[] directions = {
                Flame.Direction.RIGHT,
                Flame.Direction.LEFT,
                Flame.Direction.DOWN,
                Flame.Direction.UP
        };

        for (int dirIndex = 0; dirIndex < dirs.length; dirIndex++) {

            int[] d = dirs[dirIndex];
            Flame.Direction direction = directions[dirIndex];

            for (int i = 1; i <= b.range; i++) {

                int x = b.col + d[0] * i;
                int y = b.row + d[1] * i;

                char tile = map.get(x, y);

                // Gặp tường cứng: dừng nhánh lửa
                if (tile == '#') {
                    break;
                }

                // Kiểm tra xem đây có phải đầu nhánh lửa không
                int nextX = x + d[0];
                int nextY = y + d[1];

                char nextTile = map.get(nextX, nextY);

                boolean isEnd =
                        i == b.range
                                || nextTile == '#'
                                || nextTile == 'B';

                Flame.Type type = isEnd
                        ? Flame.Type.END
                        : Flame.Type.BODY;

                // Tạo lửa, delay tăng theo khoảng cách
                addFlame(
                        x, y, i * 0.025,
                        type,
                        direction
                );

                // Kích nổ bom khác nếu bị lửa chạm
                Bomb otherBomb = findBombAt(x, y);

                if (otherBomb != null
                        && scheduledBombs.add(otherBomb)) {

                    queue.add(otherBomb);
                }

                // Gặp Brick: phá gạch và dừng nhánh
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

                    break;
                }
            }
        }
    }

    private void addFlame(int c, int r, double delay,
                          Flame.Type type, Flame.Direction direction) {

        flames.add(new Flame(c, r, delay, type, direction));
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

        // ===== FLAME TYPE =====
        public enum Type {
            CENTER, BODY, END
        }

        // ===== FLAME DIRECTION =====
        public enum Direction {
            NONE, LEFT, RIGHT, UP, DOWN
        }

        // ===== EXPLOSION ANIMATION FRAMES =====
        public static final Image[] CENTER_FRAMES =
                loadFrames("/images/explosion/center/");

        public static final Image[] HORIZONTAL_FRAMES =
                loadFrames("/images/explosion/horizontal/");

        public static final Image[] VERTICAL_FRAMES =
                loadFrames("/images/explosion/vertical/");

        public static final Image[] END_FRAMES =
                loadFrames("/images/explosion/end/");

        // ===== LOAD 4 FRAMES =====
        private static Image[] loadFrames(String folder) {

            Image[] frames = new Image[4];

            for (int i = 0; i < 4; i++) {

                String path = folder + "frame" + (i + 1) + ".png";

                frames[i] = new Image(
                        Flame.class.getResourceAsStream(path)
                );
            }

            return frames;
        }

        // ===== FLAME POSITION & STATE =====
        public final int col, row;

        public final Type type;
        public final Direction direction;

        public double delay;
        public double timer = 0.45;
        public double animationTimer = 0;

        // ===== CONSTRUCTOR =====
        public Flame(int col, int row, double delay,
                     Type type, Direction direction) {

            this.col = col;
            this.row = row;
            this.delay = delay;
            this.type = type;
            this.direction = direction;
        }

        // ===== CHECK FLAME ACTIVE =====
        public boolean isActive() {
            return delay <= 0;
        }

        // ===== GET CURRENT ANIMATION FRAME =====
        public Image getCurrentFrame() {

            int frameIndex = (int) (animationTimer / 0.08) % 4;

            switch (type) {

                case CENTER:
                    return CENTER_FRAMES[frameIndex];

                case BODY:
                    if (direction == Direction.LEFT
                            || direction == Direction.RIGHT) {

                        return HORIZONTAL_FRAMES[frameIndex];
                    }

                    return VERTICAL_FRAMES[frameIndex];

                case END:
                    return END_FRAMES[frameIndex];

                default:
                    return CENTER_FRAMES[0];
            }
        }
    }
    // ===== LOAD LEVEL =====

    private void loadLevel(String filePath) {
        char[][] data = MapLoader.loadMap(filePath);

        map = new GameMap(data, currentLevel);

        enemies.clear();
        bombs.clear();
        flames.clear();
        pickups.clear();

        player = null;

        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[row].length; col++) {

                char tile = data[row][col];

                switch (tile) {
                    case 'P':
                        player = new Bomber(col, row);

                        playerStartCol = col;
                        playerStartRow = row;
                        break;

                    case 'E':
                        enemies.add(new Enemy(col, row));
                        break;

                    case 'C':
                        enemies.add(new ChasingEnemy(col, row));
                        break;
                }
            }
        }

        if (player == null) {
            throw new RuntimeException(
                    "Map không có vị trí Player: " + filePath
            );
        }

        bombCooldown = 0;
        spaceWasDown = false;

        state = GameState.PLAYING;
    }

    public void nextLevel() {
        if (currentLevel >= MAX_LEVEL) {
            state = GameState.WON;
            return;
        }

        currentLevel++;

        loadLevel(
                "resources/map/map" + currentLevel + ".txt"
        );
    }

    public static class Pickup {

        public static final Image BOMB_IMAGE = new Image(
                Pickup.class.getResourceAsStream("/images/bombup.png")
        );

        public static final Image FLAME_IMAGE = new Image(
                Pickup.class.getResourceAsStream("/images/flameup.png")
        );

        public static final Image SPEED_IMAGE = new Image(
                Pickup.class.getResourceAsStream("/images/speedup.png")
        );

        public static final Image LIFE_IMAGE = new Image(
                Pickup.class.getResourceAsStream("/images/lifeup.png")
        );

        public final int col, row;
        public final String type;

        public Pickup(int c, int r, String t) {
            col = c;
            row = r;
            type = t;
        }

        public Image getImage() {
            switch (type) {
                case "BOMB":
                    return BOMB_IMAGE;
                case "FLAME":
                    return FLAME_IMAGE;
                case "SPEED":
                    return SPEED_IMAGE;
                case "LIFE":
                    return LIFE_IMAGE;
                default:
                    return null;
            }
        }
    }
}

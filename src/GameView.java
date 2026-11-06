import core.GameManager;
import core.GameState;
import entities.ChasingEnemy;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import world.GameMap;
import entities.Bomb;
import entities.Enemy;
import entities.Bomber;
import world.Portal;
import world.Wall;
import world.Brick;

public class GameView extends Canvas {

    private final GameManager game;
    private final GraphicsContext g;

    // ================= ICON HUD =================
    // Có thể thay đổi đường dẫn ảnh tại đây

    private final Image heartIcon =
            loadIcon("/images/heart_icon.png");

    private final Image bombIcon =
            loadIcon("/images/bom_icon2.png");

    private final Image flameIcon =
            loadIcon("/images/flame_icon2.png");

    private final Image enemyIcon =
            loadIcon("/images/quai.png");


    // Load ảnh an toàn, tránh crash nếu ảnh không tồn tại
    private Image loadIcon(String path) {
        var stream = getClass().getResourceAsStream(path);

        if (stream == null) {
            System.out.println("HUD icon not found: " + path);
            return null;
        }

        return new Image(stream);
    }


    public GameView(GameManager game) {
        super(
                game.getMap().pixelWidth(),
                game.getMap().pixelHeight() + 42
        );

        this.game = game;
        g = getGraphicsContext2D();

        render();
    }


    public void render() {

        GameMap map = game.getMap();
        int t = map.tileSize();

        // ================= BACKGROUND =================

        g.setFill(Color.web("#101827"));
        g.fillRect(0, 0, getWidth(), getHeight());


        // ================= VẼ MAP =================

        for (int r = 0; r < map.rows(); r++) {

            for (int c = 0; c < map.cols(); c++) {

                double x = c * t;
                double y = r * t;

                char tile = map.get(c, r);

                // Vẽ nền
                g.setFill((r + c) % 2 == 0
                        ? Color.web("#334155")
                        : Color.web("#293548"));

                g.fillRect(x, y, t, t);

// Vẽ Wall với texture ngẫu nhiên đã lưu trong GameMap
                if (tile == '#') {
                    g.drawImage(
                            map.getWallImage(c, r),
                            x,
                            y,
                            t,
                            t
                    );
                }

// Vẽ Brick
                if (tile == 'B') {
                    g.drawImage(
                            Brick.IMAGE,
                            x,
                            y,
                            t,
                            t
                    );
                }

// Vẽ Portal
                if (tile == 'O') {

                    if (game.getEnemies().isEmpty()) {
                        g.drawImage(Portal.OPEN_IMAGE, x, y, t, t);
                    } else {
                        g.drawImage(Portal.CLOSED_IMAGE, x, y, t, t);
                    }
                }
            }
        }


        // ================= VẼ PICKUP =================

        for (GameManager.Pickup p : game.getPickups()) {
            g.drawImage(
                    p.getImage(),
                    p.col * t,
                    p.row * t,
                    t,
                    t
            );
        }
        // Vẽ bom
        for (Bomb b : game.getBombs()) {

            g.drawImage(
                    Bomb.IMAGE,
                    b.col * t,
                    b.row * t,
                    t,
                    t
            );
        }


        // ================= VẼ FLAMES =================

        for (GameManager.Flame f : game.getFlames()) {

            g.drawImage(
                    GameManager.Flame.IMAGE,
                    f.col * t,
                    f.row * t,
                    t,
                    t
            );
        }


        // ================= VẼ ENEMY =================

        for (Enemy e : game.getEnemies()) {

            if (e instanceof ChasingEnemy) {

                g.drawImage(
                        ChasingEnemy.IMAGE,
                        e.x * t,
                        e.y * t,
                        t,
                        t
                );

            } else {

                g.drawImage(
                        Enemy.IMAGE,
                        e.x * t,
                        e.y * t,
                        t,
                        t
                );
            }
        }


        // ================= VẼ PLAYER =================

        var p = game.getPlayer();

        double px = p.x * t;
        double py = p.y * t;

        g.drawImage(
                Bomber.IMAGE,
                px,
                py,
                t,
                t
        );


        // ================= HUD =================

        double hudY = map.pixelHeight();

        // HUD background
        g.setFill(Color.web("#0F172A"));
        g.fillRect(
                0,
                hudY,
                getWidth(),
                42
        );


        // Font chính
        g.setFont(Font.font("Arial", 17));
        g.setFill(Color.WHITE);


        // ---------- LIVES ----------

        if (heartIcon != null) {
            g.drawImage(
                    heartIcon,
                    12,
                    hudY + 8,
                    25,
                    25
            );
        } else {
            g.fillText("♥", 12, hudY + 27);
        }

        g.fillText(
                "× " + game.getPlayer().lives,
                42,
                hudY + 27
        );


        // ---------- BOMBS ----------

        if (bombIcon != null) {
            g.drawImage(
                    bombIcon,
                    105,
                    hudY + 8,
                    25,
                    25
            );
        } else {
            g.fillText("B", 105, hudY + 27);
        }

        g.fillText(
                "× " + game.getPlayer().maxBombs,
                135,
                hudY + 27
        );


        // ---------- FLAME RANGE ----------

        if (flameIcon != null) {
            g.drawImage(
                    flameIcon,
                    205,
                    hudY + 8,
                    25,
                    25
            );
        } else {
            g.fillText("F", 205, hudY + 27);
        }

        g.fillText(
                "× " + game.getPlayer().flameRange,
                235,
                hudY + 27
        );


        // ---------- ENEMIES ----------

        if (enemyIcon != null) {
            g.drawImage(
                    enemyIcon,
                    305,
                    hudY + 8,
                    25,
                    25
            );
        } else {
            g.fillText("E", 305, hudY + 27);
        }

        g.fillText(
                "× " + game.getEnemies().size(),
                335,
                hudY + 27
        );


        // ---------- CONTROLS ----------

        g.setFill(Color.web("#CBD5E1"));
        g.setFont(Font.font("Arial", 14));

        g.fillText(
                "[P] Pause   [R] Restart",
                420,
                hudY + 27
        );


        // ================= PAUSE SCREEN =================

        if (game.getState() == GameState.PAUSED) {

            g.setFill(Color.rgb(0, 0, 0, 0.6));

            g.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );

            g.setFill(Color.WHITE);
            g.setFont(Font.font(44));

            g.fillText(
                    "PAUSED",
                    map.pixelWidth() / 2 - 90,
                    map.pixelHeight() / 2
            );
        }
    }
}
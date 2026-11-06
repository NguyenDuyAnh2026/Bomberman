
import core.GameManager;
import core.GameState;
import entities.ChasingEnemy;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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

    public GameView(GameManager game) {
        super(game.getMap().pixelWidth(), game.getMap().pixelHeight() + 42);
        this.game = game;
        g = getGraphicsContext2D();
        render();
    }

    public void render() {
        GameMap map = game.getMap();
        int t = map.tileSize();

        g.setFill(Color.web("#101827"));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Vẽ bản đồ
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

        // Vẽ vật phẩm
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

        // Flames
        for (GameManager.Flame f : game.getFlames()) {
            g.drawImage(
                    GameManager.Flame.IMAGE,
                    f.col * t,
                    f.row * t,
                    t,
                    t
            );
        }
        // Vẽ enemy
        for (Enemy e : game.getEnemies()) {

            if (e instanceof ChasingEnemy) {
                // Enemy đuổi người chơi
                g.drawImage(
                        ChasingEnemy.IMAGE,
                        e.x * t,
                        e.y * t,
                        t,
                        t
                );
            } else {
                // Enemy thường
                g.drawImage(
                        Enemy.IMAGE,
                        e.x * t,
                        e.y * t,
                        t,
                        t
                );
            }
        }

        // Vẽ nhân vật bằng tọa độ double để di chuyển mượt
        // Vẽ Bomber
        var p = game.getPlayer();

        double px = p.x * t;
        double py = p.y * t;

        g.drawImage(Bomber.IMAGE, px, py, t, t);

        // Vẽ HUD
        g.setFill(Color.web("#0F172A"));
        g.fillRect(0, map.pixelHeight(), getWidth(), 42);

        g.setFill(Color.WHITE);
        g.setFont(Font.font("Arial", 17));
        g.fillText(
                "♥ " + game.getPlayer().lives
                        + "    BOMBS: " + game.getPlayer().maxBombs
                        + "    FLAME: " + game.getPlayer().flameRange
                        + "    ENEMIES: " + game.getEnemies().size()
                        + "    [P] Pause  [R] Restart",
                12, map.pixelHeight() + 27
        );

        // Màn hình Pause
        if (game.getState() == GameState.PAUSED) {
            g.setFill(Color.rgb(0, 0, 0, 0.6));
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setFill(Color.WHITE);
            g.setFont(Font.font(44));
            g.fillText("PAUSED", map.pixelWidth() / 2 - 90, map.pixelHeight() / 2);
        }
    }
}

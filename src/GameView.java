
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

                g.setFill((r + c) % 2 == 0
                        ? Color.web("#334155")
                        : Color.web("#293548"));
                g.fillRect(x, y, t, t);

                if (tile == '#') {
                    g.setFill(Color.web("#64748B"));
                    g.fillRoundRect(x + 2, y + 2, t - 4, t - 4, 5, 5);

                    g.setFill(Color.web("#94A3B8"));
                    g.fillRect(x + 4, y + 4, t - 8, 5);
                }

                if (tile == 'B') {
                    g.setFill(Color.web("#B45309"));
                    g.fillRoundRect(x + 3, y + 3, t - 6, t - 6, 4, 4);

                    g.setStroke(Color.web("#FBBF24"));
                    g.strokeLine(x + 5, y + t / 2, x + t - 5, y + t / 2);
                    g.strokeLine(x + t / 2, y + 5, x + t / 2, y + t - 5);
                }

                if (tile == 'G') {
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
            g.setFill(
                    p.type.equals("LIFE") ? Color.PINK :
                            p.type.equals("FLAME") ? Color.ORANGE :
                                    p.type.equals("SPEED") ? Color.CYAN :
                                            Color.GOLD
            );

            g.fillOval(p.col * t + 10, p.row * t + 10, t - 20, t - 20);

            g.setFill(Color.BLACK);
            g.setFont(Font.font(12));
            g.fillText(p.type.substring(0, 1), p.col * t + 17, p.row * t + 25);
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

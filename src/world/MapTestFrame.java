package world;

import entities.Bomber;
import entities.Enemy;
import entities.Bomb;
import utils.InputHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapTestFrame extends JFrame {

    private GameMap gameMap;
    private Bomber bomber;
    private List<Enemy> enemies;
    private List<Bomb> activeBombs;
    private InputHandler inputHandler;
    private Timer gameLoopTimer;
    private boolean spaceReleased = true;

    public MapTestFrame() {
        setTitle("Bomberman - Test Map, Character & Enemies");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Đọc map thông qua luồng từ thư mục resources/map/map1.txt
        gameMap = new GameMap("map1.txt");

        // Bomber xuất hiện ở ô (1, 1) - truyền tọa độ pixel (col * TILE_SIZE)
        bomber = new Bomber(1 * GameMap.TILE_SIZE, 1 * GameMap.TILE_SIZE);

        // Khởi tạo danh sách quái vật
        enemies = new ArrayList<>();
        enemies.add(new Enemy(6 * GameMap.TILE_SIZE, 11 * GameMap.TILE_SIZE));
        enemies.add(new Enemy(11 * GameMap.TILE_SIZE, 11 * GameMap.TILE_SIZE));

        activeBombs = new ArrayList<>();
        inputHandler = new InputHandler();

        JPanel canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // 1. Vẽ bản đồ (tường, gạch, nền)
                gameMap.render(g2d);

                // --- BỔ SUNG: Vẽ các item rơi ra từ gạch vỡ ---
                gameMap.renderItems(g2d);

                // 2. Vẽ tất cả bom đang chờ nổ
                for (Bomb bomb : activeBombs) {
                    bomb.render(g2d);
                }

                // 3. Vẽ quái vật
                for (Enemy enemy : enemies) {
                    enemy.render(g2d);
                }

                // 4. Vẽ người chơi
                bomber.render(g2d);
            }
        };

        canvas.setPreferredSize(new Dimension(gameMap.getWidth(), gameMap.getHeight()));
        canvas.setOpaque(true);

        // Gắn KeyListener trực tiếp vào canvas và yêu cầu nhận focus để bắt sự kiện phím bấm
        canvas.setFocusable(true);
        canvas.requestFocusInWindow();
        canvas.addKeyListener(inputHandler);

        add(canvas);

        pack();
        setLocationRelativeTo(null);

        gameLoopTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGame();
                canvas.repaint();
            }
        });
        gameLoopTimer.start();
    }

    private void updateGame() {
        // Cập nhật trạng thái di chuyển của Bomber thông qua InputHandler
        boolean up = inputHandler.isUp();
        boolean down = inputHandler.isDown();
        boolean left = inputHandler.isLeft();
        boolean right = inputHandler.isRight();

        bomber.update(gameMap, up, down, left, right);

        // Xử lý đặt bom bằng phím Space
        if (inputHandler.isSpacePressed()) {
            if (spaceReleased) {
                int bombCol = (bomber.getX() + GameMap.TILE_SIZE / 2) / GameMap.TILE_SIZE;
                int bombRow = (bomber.getY() + GameMap.TILE_SIZE / 2) / GameMap.TILE_SIZE;

                int flameLength = 1;
                activeBombs.add(new Bomb(bombCol, bombRow, flameLength));
                spaceReleased = false;
            }
        } else {
            spaceReleased = true;
        }

        // Cập nhật trạng thái đếm ngược của bom, nổ lan, phá gạch và tiêu diệt quái
        Iterator<Bomb> iterator = activeBombs.iterator();
        while (iterator.hasNext()) {
            Bomb bomb = iterator.next();
            bomb.update(gameMap, enemies, bomber);
            if (bomb.isExploded()) {
                iterator.remove();
            }
        }

        // Cập nhật quái vật
        for (Enemy enemy : enemies) {
            enemy.update(gameMap, bomber, activeBombs);
        }

        // --- BỔ SUNG: Kiểm tra xem Bomber có đi vào vị trí item để nhặt không ---
        gameMap.checkItems(bomber);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MapTestFrame frame = new MapTestFrame();
            frame.setVisible(true);
        });
    }
}

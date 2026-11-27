
package entities;

import javafx.scene.image.Image;

public class Bomber extends MovingEntity {

    public int lives = 3;
    public int maxBombs = 1;
    public int flameRange = 2;

    public double moveSpeed = 0.1;

    public double x, y;
    public double startX, startY;
    public double moveProgress = 1.0;

    // Các hướng di chuyển
    public enum Direction {
        DOWN, UP, LEFT, RIGHT
    }

    private Direction currentDirection = Direction.DOWN;

    // Tải ảnh
    private static Image loadImage(String path) {
        return new Image(
                Bomber.class.getResourceAsStream(path)
        );
    }

    // Ảnh Naruto
    public static final Image IDLE_IMAGE =
            loadImage("/images/naruto/idle.png");

    // Giữ IMAGE để tương thích với code cũ nếu còn sử dụng
    public static final Image IMAGE = IDLE_IMAGE;

    // Animation theo từng hướng
    private final Animation idleAnimation;

    private final Animation walkDown;
    private final Animation walkUp;
    private final Animation walkLeft;
    private final Animation walkRight;

    public Bomber(int col, int row) {
        super(col, row);

        x = col;
        y = row;

        startX = col;
        startY = row;

        idleAnimation = new Animation(
                new Image[]{IDLE_IMAGE},
                0.2,
                true
        );

        walkDown = new Animation(
                new Image[]{
                        loadImage("/images/naruto/walk_down_1.png"),
                        loadImage("/images/naruto/walk_down_2.png")
                },
                0.15,
                true
        );

        walkUp = new Animation(
                new Image[]{
                        loadImage("/images/naruto/walk_up_1.png"),
                        loadImage("/images/naruto/walk_up_2.png")
                },
                0.15,
                true
        );

        walkLeft = new Animation(
                new Image[]{
                        loadImage("/images/naruto/walk_left_1.png"),
                        loadImage("/images/naruto/walk_left_2.png")
                },
                0.15,
                true
        );

        walkRight = new Animation(
                new Image[]{
                        loadImage("/images/naruto/walk_right_1.png"),
                        loadImage("/images/naruto/walk_right_2.png")
                },
                0.15,
                true
        );

        setAnimation(idleAnimation);
    }

    public void updateBomberAnimation(
            double deltaTime,
            boolean moving,
            Direction direction
    ) {
        if (!moving) {
            setAnimation(idleAnimation);
        } else {
            currentDirection = direction;

            switch (currentDirection) {
                case DOWN:
                    setAnimation(walkDown);
                    break;

                case UP:
                    setAnimation(walkUp);
                    break;

                case LEFT:
                    setAnimation(walkLeft);
                    break;

                case RIGHT:
                    setAnimation(walkRight);
                    break;
            }
        }

        updateAnimation(deltaTime);
    }
}

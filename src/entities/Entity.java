
package entities;

import javafx.scene.image.Image;

public abstract class Entity extends GameObject {

    protected Animation currentAnimation;

    protected Entity(int col, int row) {
        super(col, row);
    }

    // Gán animation hiện tại
    public void setAnimation(Animation animation) {
        if (currentAnimation != animation) {
            currentAnimation = animation;

            if (currentAnimation != null) {
                currentAnimation.reset();
            }
        }
    }

    // Cập nhật animation
    public void updateAnimation(double deltaTime) {
        if (currentAnimation != null) {
            currentAnimation.update(deltaTime);
        }
    }

    // Lấy ảnh của frame hiện tại
    public Image getCurrentImage() {
        if (currentAnimation == null) {
            return null;
        }

        return currentAnimation.getCurrentFrame();
    }
}

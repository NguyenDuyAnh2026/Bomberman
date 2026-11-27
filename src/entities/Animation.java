
package entities;

import javafx.scene.image.Image;

public class Animation {

    private final Image[] frames;
    private final double frameDuration;
    private final boolean loop;

    private int currentFrame = 0;
    private double timer = 0;
    private boolean finished = false;

    public Animation(Image[] frames, double frameDuration, boolean loop) {
        if (frames == null || frames.length == 0) {
            throw new IllegalArgumentException("Animation phải có ít nhất 1 frame");
        }

        if (frameDuration <= 0) {
            throw new IllegalArgumentException("frameDuration phải lớn hơn 0");
        }

        this.frames = frames;
        this.frameDuration = frameDuration;
        this.loop = loop;
    }

    public void update(double deltaTime) {
        if (finished) return;

        timer += deltaTime;

        while (timer >= frameDuration && !finished) {
            timer -= frameDuration;
            currentFrame++;

            if (currentFrame >= frames.length) {
                if (loop) {
                    currentFrame = 0;
                } else {
                    currentFrame = frames.length - 1;
                    finished = true;
                }
            }
        }
    }

    public Image getCurrentFrame() {
        return frames[currentFrame];
    }

    public boolean isFinished() {
        return finished;
    }

    public void reset() {
        currentFrame = 0;
        timer = 0;
        finished = false;
    }
}

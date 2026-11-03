package utils;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import java.util.HashSet;
import java.util.Set;
public class InputHandler {
    private final Set<KeyCode> keys=new HashSet<>();
    public void attach(Scene s) {
        s.setOnKeyPressed(e->keyPressed(e.getCode()));
        s.setOnKeyReleased(e->keyReleased(e.getCode()));
    }
    public void keyPressed(KeyCode k) {
        keys.add(k);
    }
    public void keyReleased(KeyCode k) {
        keys.remove(k);
    }
    public boolean up() {
        return keys.contains(KeyCode.UP)||keys.contains(KeyCode.W);
    }
    public boolean down() {
        return keys.contains(KeyCode.DOWN)||keys.contains(KeyCode.S);
    }
    public boolean left() {
        return keys.contains(KeyCode.LEFT)||keys.contains(KeyCode.A);
    }
    public boolean right() {
        return keys.contains(KeyCode.RIGHT)||keys.contains(KeyCode.D);
    }
    public boolean space() {
        return keys.contains(KeyCode.SPACE);
    }
}

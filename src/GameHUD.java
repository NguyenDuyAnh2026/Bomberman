import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
/** Reusable HUD bar for score/status display. */
public class GameHUD extends HBox {
    private final Label status = new Label();
    public GameHUD() {
        setPadding(new Insets(8,12,8,12));
        setSpacing(12);
        setStyle("-fx-background-color:#0F172A;");
        status.setTextFill(Color.WHITE);
        status.setStyle("-fx-font-size:16px;-fx-font-weight:bold;");
        getChildren().add(status);
    }
    public void update(int lives,int bombs,int flame,int enemies) {
        status.setText("LIVES: "+lives+"   BOMBS: "+bombs+"   FLAME: "+flame+"   ENEMIES: "+enemies);
    }
}

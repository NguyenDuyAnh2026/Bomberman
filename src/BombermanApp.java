import core.GameManager;
import core.GameState;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
public class BombermanApp extends Application {
    private Stage stage;
    @Override public void start(Stage stage) {
        this.stage = stage;
        showMenu();
        stage.setTitle("Bomberman");
        stage.setMinWidth(760);
        stage.setMinHeight(580);
        stage.show();
    }
    private void showMenu() {
        StackPane root = new StackPane();
        var imageUrl = getClass().getResource("/images/menu_bg_fixed.png");
        if (imageUrl != null) {
            ImageView bg = new ImageView(new Image(imageUrl.toExternalForm()));
            bg.setPreserveRatio(false);
            bg.fitWidthProperty().bind(root.widthProperty());
            bg.fitHeightProperty().bind(root.heightProperty());
            root.getChildren().add(bg);
        } else root.setStyle("-fx-background-color: #111827;");
        Rectangle overlay = new Rectangle();
        overlay.widthProperty().bind(root.widthProperty());
        overlay.heightProperty().bind(root.heightProperty());
        overlay.setFill(Color.rgb(0,0,0,.55));
        root.getChildren().add(overlay);
        Label title = new Label("BOMBERMAN");
        title.setStyle("-fx-font-size: 64px;-fx-font-family:'Arial Black';-fx-text-fill:#FFB547;-fx-effect:dropshadow(gaussian,#00CFFF,18,.3,0,0);");
        Label subtitle = new Label("THE BATTLE BEGINS");
        subtitle.setStyle("-fx-font-size:18px;-fx-text-fill:white;-fx-font-weight:bold;");
        Button start = button("▶  START", "#E92D50");
        Button guide = button("HOW TO PLAY", "#2563EB");
        Button quit = button("✕  QUIT", "#374151");
        start.setOnAction(e -> startGame());
        guide.setOnAction(e -> showGuide());
        quit.setOnAction(e -> Platform.exit());
        VBox menu = new VBox(15,title,subtitle,start,guide,quit);
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(20));
        root.getChildren().add(menu);
        stage.setScene(new Scene(root,960,686));
    }
    private Button button(String text, String color) {
        Button b = new Button(text);
        b.setPrefSize(280,54);
        b.setStyle("-fx-background-color:"+color+";-fx-text-fill:white;-fx-font-size:18px;-fx-font-weight:bold;-fx-background-radius:10;-fx-cursor:hand;");
        b.setOnMouseEntered(e -> b.setScaleX(1.04));
        b.setOnMouseExited(e -> b.setScaleX(1));
        return b;
    }
    private void showGuide() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("How to Play");
        a.setHeaderText("Bomberman controls");
        a.setContentText("Move: WASD or arrow keys\nPlace bomb: SPACE\nPause: P\nRestart: R\n\nDestroy bricks, avoid explosions, defeat enemies, then reach the portal.");
        a.showAndWait();
    }
    private void startGame() {
        GameManager manager = new GameManager();
        GameController controller = new GameController(manager, this::showMenu);
        stage.setScene(new Scene(controller.getRoot()));
        controller.start();
    }
    public static void main(String[] args) {
        launch(args);
    }
}

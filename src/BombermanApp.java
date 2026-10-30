
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

    @Override
    public void start(Stage stage) {

        // 1. Doc anh nen tu resources/images
        var imageUrl = getClass().getResource("/images/menu_bg_fixed.png");

        if (imageUrl == null) {
            System.err.println(
                    "Khong tim thay anh nen: resources/images/menu_bg_fixed.png"
            );
            return;
        }

        Image backgroundImage = new Image(imageUrl.toExternalForm());
        ImageView background = new ImageView(backgroundImage);
        background.setPreserveRatio(false);
        background.setSmooth(true);

        // 2. Khung giao dien chinh
        StackPane root = new StackPane();
        background.fitWidthProperty().bind(root.widthProperty());
        background.fitHeightProperty().bind(root.heightProperty());

        // 3. Lop phu toi, giup chu va nut noi bat hon
        Rectangle darkOverlay = new Rectangle();
        darkOverlay.widthProperty().bind(root.widthProperty());
        darkOverlay.heightProperty().bind(root.heightProperty());
        darkOverlay.setMouseTransparent(true);
        darkOverlay.setFill(Color.web("#101018", 0.48));
        darkOverlay.setStyle(
                "-fx-fill: linear-gradient(to bottom, " +
                        "rgba(8, 10, 22, 0.68) 0%, " +
                        "rgba(8, 10, 22, 0.18) 48%, " +
                        "rgba(8, 10, 22, 0.72) 100%);"
        );

        // 4. Tieu de game
        Label title = new Label("BOMBERMAN");
        title.setStyle(
                "-fx-font-family: 'Arial Black';" +
                        "-fx-font-size: 80px;" +
                        "-fx-font-weight: 900;" +
                        "-fx-text-fill: #FFB547;" +
                        "-fx-effect: dropshadow(gaussian, #00CFFF, 22, 0.35, 0, 0);" +
                        " -fx-padding: 0 0 0 0;"
        );

        Label subtitle = new Label("THE BATTLE BEGINS");
        subtitle.setStyle(
                "-fx-font-family: 'Arial';" +
                        "-fx-font-size: 23px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #FFFFFF;" +
                        "-fx-letter-spacing: 4px;" +
                        "-fx-effect: dropshadow(gaussian, black, 5, 0.4, 0, 2);"
        );

        // Duong trang tri neon
        Rectangle neonLine = new Rectangle(115, 3, Color.web("#FFB547"));
        neonLine.setArcWidth(3);
        neonLine.setArcHeight(3);
        neonLine.setEffect(
                new javafx.scene.effect.Glow(0.65)
        );

        // 5. Cac nut chinh
        Button btnStart = createButton("▶   START");
        Button btnGuide = createButton("   HOW TO PLAY");
        Button btnQuit = createButton("✕   QUIT");

        btnStart.setOnAction(e ->
                System.out.println("Da bam Start - cho tich hop gameplay")
        );

        btnGuide.setOnAction(e -> showGuide());

        btnQuit.setOnAction(e -> Platform.exit());

        VBox menu = new VBox(
                15,
                title,
                subtitle,
                neonLine,
                btnStart,
                btnGuide,
                btnQuit
        );

        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(20));

        // Tao khoang cach giua phan tieu de va cac nut
        VBox.setMargin(btnStart, new Insets(15, 0, 0, 0));

        // 6. Thong tin nho o cuoi man hinh
        Label footer = new Label("PLAYER 1     •     BOMBERMAN PROJECT");
        footer.setStyle(
                "-fx-font-size: 11px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255,255,255,0.78);" +
                        "-fx-letter-spacing: 2px;"
        );

        StackPane.setAlignment(footer, Pos.BOTTOM_CENTER);
        StackPane.setMargin(footer, new Insets(0, 0, 18, 0));

        // 7. Xep cac lop theo thu tu
        root.getChildren().addAll(
                background,
                darkOverlay,
                menu,
                footer
        );

        stage.setTitle("Bomberman");
        stage.setMinWidth(760);
        stage.setMinHeight(580);
        stage.setScene(new Scene(root, 960, 686));
        stage.show();
    }

    // Tao nut dung chung mot phong cach
    private Button createButton(String text) {
        Button button = new Button(text);

        String normalStyle =
                "-fx-background-color: #E92D50;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Arial';" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-border-color: rgba(255,255,255,0.55);" +
                        "-fx-border-width: 4px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-cursor: hand;";

        String hoverStyle =
                "-fx-background-color: #FF496B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Arial';" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-border-color: #00E5FF;" +
                        "-fx-border-width: 5px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, #FF315B, 14, 0.3, 0, 0);";

        button.setStyle(normalStyle);
        button.setPrefSize(310, 58);
        button.setMaxWidth(310);

        button.setOnMouseEntered(e -> {
            button.setStyle(hoverStyle);
            button.setScaleX(1.04);
            button.setScaleY(1.04);
        });

        button.setOnMouseExited(e -> {
            button.setStyle(normalStyle);
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });

        return button;
    }

    // Hop thoai huong dan choi
    private void showGuide() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("How to Play");
        alert.setHeaderText("HUONG DAN CHOI BOMBERMAN");
        alert.setContentText(
                "Di chuyen: cac phim mui ten\n" +
                        "Dat bom: phim Space\n" +
                        "Pha gach, tieu diet ke dich va tim cong qua man."
        );
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

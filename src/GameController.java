import core.GameManager;
import core.GameState;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.StackPane;
import javafx.scene.input.KeyCode;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import utils.InputHandler;
public class GameController {
    private final GameManager manager;
    private final Runnable backToMenu;
    private final GameView view;
    private final StackPane root = new StackPane();
    private final InputHandler input = new InputHandler();
    private AnimationTimer loop;
    private long last = 0;
    private VBox endOverlay;
    public GameController(GameManager manager, Runnable backToMenu) {
        this.manager=manager;
        this.backToMenu=backToMenu;
        this.view=new GameView(manager);
        root.getChildren().add(view);
        root.setOnKeyPressed(e -> {
            input.keyPressed(e.getCode());
            if (e.getCode()==KeyCode.P) manager.togglePause();
            if (e.getCode()==KeyCode.R) manager.restart();
        }
        );
        root.setOnKeyReleased(e -> input.keyReleased(e.getCode()));
        root.setFocusTraversable(true);
    }
    public StackPane getRoot() {
        return root;
    }
    public void start() {
        loop=new AnimationTimer() {
            public void handle(long now) {
                if(last==0) last=now;
                double dt=Math.min(.05,(now-last)/1e9);
                last=now;
                manager.update(dt,input);
                view.render();
                if(manager.getState()==GameState.WON||manager.getState()==GameState.LOST) showEnd();
            }
        }
        ;
        loop.start();
        root.requestFocus();
    }
    private void showEnd() {
        if(endOverlay!=null)return;
        if(loop!=null)loop.stop();
        Rectangle shade=new Rectangle(manager.getMap().pixelWidth(),manager.getMap().pixelHeight()+42,Color.rgb(0,0,0,.72));
        Label label=new Label(manager.getState()==GameState.WON?"YOU WIN!":"GAME OVER");
        label.setStyle("-fx-font-size:48px;-fx-font-weight:bold;-fx-text-fill:"+(manager.getState()==GameState.WON?"#4ade80":"#fb7185")+";");
        Button restart=new Button("PLAY AGAIN");
        restart.setOnAction(e-> {
            manager.restart(); root.getChildren().remove(endOverlay);root.getChildren().remove(shade);endOverlay=null;last=0;start();
        }
        );
        Button menu=new Button("MAIN MENU");
        menu.setOnAction(e->backToMenu.run());
        endOverlay=new VBox(18,label,restart,menu);
        endOverlay.setAlignment(Pos.CENTER);
        root.getChildren().addAll(shade,endOverlay);
    }
}

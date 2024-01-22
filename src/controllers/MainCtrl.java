package controllers;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Scene game;

    private Parent parent;

    private static double x;
    private static double y;

    public void initialize(Stage primaryStage, Pair<GameCtrl, Parent> game) {
        this.parent = game.getValue();
        this.game = new Scene(game.getValue());
        game.getKey().setParent(game.getValue());
        game.getKey().setScene(this.game);


        this.game.setOnMousePressed(event -> {
            x = primaryStage.getX() - event.getScreenX();
            y = primaryStage.getY() - event.getScreenY();
        });
        this.game.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() + x);
            primaryStage.setY(event.getScreenY() + y);
        });

        primaryStage.setTitle("20-MaoTek-48");
        primaryStage.setScene(this.game);
        primaryStage.show();
    }

}

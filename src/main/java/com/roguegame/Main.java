package com.roguegame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Label label = new Label("JavaFX is working!");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Rogue Game Starter");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

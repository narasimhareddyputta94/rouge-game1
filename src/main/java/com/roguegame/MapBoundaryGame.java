package com.roguegame;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MapBoundaryGame extends Application {

    private final int tileSize = 40;
    private final int rows = 10;
    private final int cols = 15;
    private int playerX = 1;
    private int playerY = 1;

    private final int[][] map = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,1,0,0,0,0,0,0,0,1},
            {1,0,1,1,1,0,1,0,1,1,1,1,1,0,1},
            {1,0,1,0,0,0,0,0,1,0,0,0,1,0,1},
            {1,0,1,0,1,1,1,1,1,0,1,0,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,1,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,1,1,1,1,1,1,1,1,1,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(cols * tileSize, rows * tileSize);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawMap(gc);

        Label keyLabel = new Label("Last key pressed: ");
        keyLabel.setFont(Font.font("Consolas", 16));
        keyLabel.setTextFill(Color.YELLOW);

        Label helpLabel = new Label("Use W/A/S/D or Arrow Keys to move");
        helpLabel.setFont(Font.font("Consolas", 16));
        helpLabel.setTextFill(Color.LIGHTGRAY);

        Pane overlay = new Pane(keyLabel, helpLabel);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        overlay.setPrefHeight(50);
        overlay.setTranslateY(rows * tileSize - 50);
        overlay.setMinWidth(cols * tileSize);

        keyLabel.setLayoutX(20);
        keyLabel.setLayoutY(5);
        helpLabel.setLayoutX(20);
        helpLabel.setLayoutY(25);

        FadeTransition fade = new FadeTransition(Duration.seconds(2), helpLabel);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setCycleCount(1);
        fade.play();

        Pane root = new Pane(canvas, overlay);
        Scene scene = new Scene(root);

        scene.setOnKeyPressed(event -> {
            String key = event.getCode().toString();
            keyLabel.setText("Last key pressed: " + key);

            int newX = playerX;
            int newY = playerY;

            switch (event.getCode()) {
                case UP, W -> newY--;
                case DOWN, S -> newY++;
                case LEFT, A -> newX--;
                case RIGHT, D -> newX++;
            }

            if (map[newY][newX] == 0) {
                playerX = newX;
                playerY = newY;
            }

            drawMap(gc);
        });

        primaryStage.setTitle("JavaFX Rogue Game – Polished Movement");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void drawMap(GraphicsContext gc) {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                gc.setFill(map[y][x] == 1 ? Color.DARKGRAY : Color.LIGHTGRAY);
                gc.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }

        gc.setFill(Color.BLUE);
        gc.fillOval(playerX * tileSize + 5, playerY * tileSize + 5, tileSize - 10, tileSize - 10);
    }
}

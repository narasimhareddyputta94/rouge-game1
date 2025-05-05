package com.roguegame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MapBoundaryGame extends Application {

    private final int tileSize = 40;
    private final int rows = 10;
    private final int cols = 15;
    private int playerX = 1;
    private int playerY = 1;

    // 0 = walkable, 1 = wall
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

        Scene scene = new Scene(new Pane(canvas));
        scene.setOnKeyPressed(this::handleMovement);

        primaryStage.setTitle("JavaFX Map Boundaries");
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

    private void handleMovement(KeyEvent event) {
        int newX = playerX;
        int newY = playerY;

        switch (event.getCode()) {
            case UP -> newY--;
            case DOWN -> newY++;
            case LEFT -> newX--;
            case RIGHT -> newX++;
        }

        if (map[newY][newX] == 0) {
            playerX = newX;
            playerY = newY;
        }

        Canvas canvas = (Canvas) ((Pane) ((Scene) event.getSource()).getRoot()).getChildren().get(0);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawMap(gc);
    }
}

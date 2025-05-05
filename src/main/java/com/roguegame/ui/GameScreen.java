package com.roguegame.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.Pane;

public class GameScreen extends Pane {
    private static final int TILE_SIZE = 20;
    private static final int COLS = 80;
    private static final int ROWS = 24;

    private final Canvas canvas;
    private int playerX = 5;
    private int playerY = 5;

    public GameScreen() {
        canvas = new Canvas(COLS * TILE_SIZE, ROWS * TILE_SIZE);
        getChildren().add(canvas);
        draw();

        setFocusTraversable(true);
        setOnKeyPressed(this::handleKeyPress);
    }

    private void handleKeyPress(KeyEvent e) {
        switch (e.getCode()) {
            case UP, W -> playerY = Math.max(0, playerY - 1);
            case DOWN, S -> playerY = Math.min(ROWS - 1, playerY + 1);
            case LEFT, A -> playerX = Math.max(0, playerX - 1);
            case RIGHT, D -> playerX = Math.min(COLS - 1, playerX + 1);
        }
        draw();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.GREEN);
        gc.setFont(new Font("Consolas", TILE_SIZE));
        gc.fillText("@", playerX * TILE_SIZE, (playerY + 1) * TILE_SIZE);
    }
}

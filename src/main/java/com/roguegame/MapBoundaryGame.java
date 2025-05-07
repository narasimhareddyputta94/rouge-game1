package com.roguegame;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class MapBoundaryGame extends Application {

    private final int tileSize = 40;
    private final int rows = 10;
    private final int cols = 15;
    private int playerX = 1;
    private int playerY = 1;
    private boolean inCombat = false;
    private Monster activeMonster = null;
    private List<Monster> monsters = new ArrayList<>();

    private int health = 100;
    private final int[][] map = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,1,0,0,0,0,0,0,0,1},
            {1,0,1,1,1,0,1,0,1,1,1,1,1,0,1},
            {1,0,1,0,0,0,0,0,1,0,0,0,1,0,1},
            {1,0,1,0,1,1,1,1,1,0,1,0,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,1,0,0,0,1},
            {1,1,1,1,0,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,1,1,1,1,1,1,1,1,1,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(cols * tileSize, (rows + 2) * tileSize);
        canvas.setTranslateY(tileSize);

        monsters.add(new Monster("Goblin", 2, 5, 30));
        monsters.add(new Monster("Orc", 9, 4, 50));
        monsters.add(new Monster("Slime", 9, 1, 20));

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
        overlay.setTranslateY((rows + 1) * tileSize);
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

        ProgressBar healthBar = new ProgressBar(1.0);
        healthBar.setPrefWidth(cols * tileSize - 40);
        healthBar.setStyle("""
            -fx-accent: linear-gradient(to right, limegreen, green);
            -fx-background-radius: 10;
            -fx-border-radius: 10;
            -fx-border-color: white;
            -fx-border-width: 1;
        """);

        Label hpLabel = new Label("❤ Health: 100 / 100");
        hpLabel.setFont(Font.font("Consolas", 20));
        hpLabel.setTextFill(Color.web("#00FF00"));
        hpLabel.setStyle("-fx-effect: dropshadow(gaussian, black, 2, 0, 1, 1);");

        VBox healthDisplay = new VBox(hpLabel, healthBar);
        healthDisplay.setLayoutX(20);
        healthDisplay.setLayoutY(10);
        healthDisplay.setSpacing(5);
        healthDisplay.setAlignment(Pos.CENTER_LEFT);

        Pane root = new Pane(canvas, overlay, healthDisplay);
        Scene scene = new Scene(root);

        scene.setOnKeyPressed((KeyEvent event) -> {
            String key = event.getCode().toString();
            keyLabel.setText("Last key pressed: " + key);

            if (key.equals("H")) {
                health = Math.max(0, health - 10);
                hpLabel.setText("❤ Health: " + health + " / 100");

                double progress = health / 100.0;
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.seconds(0.2),
                                new KeyValue(healthBar.progressProperty(), progress))
                );
                timeline.play();

                hpLabel.setTextFill(health <= 30 ? Color.ORANGERED : Color.LIME);
            }

            if (key.equals("R") && health == 0) {
                root.getChildren().removeIf(n -> "game-over".equals(n.getId()));
                playerX = 1;
                playerY = 1;
                health = 100;
                hpLabel.setText("❤ Health: 100 / 100");
                hpLabel.setTextFill(Color.LIME);
                healthBar.setProgress(1.0);
                inCombat = false;
                activeMonster = null;

                for (Monster m : monsters) {
                    m.reset();
                }

                drawMap(gc);
                return;
            }


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

            if (!inCombat) {
                for (Monster m : monsters) {
                    if (m.isAlive() && m.getX() == playerX && m.getY() == playerY) {
                        inCombat = true;
                        activeMonster = m;
                        root.getChildren().removeIf(n -> n.getId() != null && n.getId().equals("dialogue-box"));
                        showDialogue(root, "You encountered a " + m.getName() + "! HP: " + m.getHealth() + ". Press SPACE to attack.");
                        break;
                    }
                }
            }

            if (inCombat && key.equals("SPACE") && activeMonster != null) {
                int damage = 5 + (int)(Math.random() * 11);
                activeMonster.damage(damage);

                root.getChildren().removeIf(n -> n.getId() != null && n.getId().equals("dialogue-box"));

                if (!activeMonster.isAlive()) {
                    showDialogue(root, "You defeated the " + activeMonster.getName() + "!");
                    inCombat = false;
                    activeMonster = null;
                } else {
                    showDialogue(root, "You hit the " + activeMonster.getName() + " for " + damage + " damage! Remaining HP: " + activeMonster.getHealth());

                    int counterDamage = 5 + (int)(Math.random() * 11);
                    health = Math.max(0, health - counterDamage);
                    hpLabel.setText("❤ Health: " + health + " / 100");

                    double progress = health / 100.0;
                    Timeline timeline = new Timeline(
                            new KeyFrame(Duration.seconds(0.3),
                                    new KeyValue(healthBar.progressProperty(), progress))
                    );
                    timeline.play();

                    hpLabel.setTextFill(health <= 30 ? Color.ORANGERED : Color.LIME);

                    FadeTransition redFlash = new FadeTransition(Duration.millis(100), canvas);
                    redFlash.setFromValue(1.0);
                    redFlash.setToValue(0.3);
                    redFlash.setAutoReverse(true);
                    redFlash.setCycleCount(2);
                    redFlash.play();

                    if (health == 0) {
                        showGameOver(root);
                        inCombat = false;
                        activeMonster = null;
                    }
                }

                drawMap(gc);

                FadeTransition flash = new FadeTransition(Duration.millis(100), canvas);
                flash.setFromValue(1.0);
                flash.setToValue(0.6);
                flash.setAutoReverse(true);
                flash.setCycleCount(2);
                flash.play();
            }
        });

        primaryStage.setTitle("JavaFX Rogue Game – Monster Combat + Flash Effect");
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

        for (Monster monster : monsters) {
            if (monster.isAlive()) {
                gc.setFill(Color.CRIMSON);
                gc.fillOval(monster.getX() * tileSize + 5, monster.getY() * tileSize + 5, tileSize - 10, tileSize - 10);
            }
        }

        double px = playerX * tileSize + 5;
        double py = playerY * tileSize + 5;
        double size = tileSize - 10;

        gc.setFill(Color.BLUE);
        gc.fillOval(px, py, size, size);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeOval(px, py, size, size);
    }

    private void showDialogue(Pane root, String message) {
        Label dialogue = new Label(message);
        dialogue.setFont(Font.font("Consolas", 18));
        dialogue.setTextFill(Color.WHITE);
        dialogue.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-padding: 10;");
        dialogue.setLayoutX(40);
        dialogue.setLayoutY((rows + 1) * tileSize - 80);
        dialogue.setId("dialogue-box");
        root.getChildren().add(dialogue);
    }

    private void showGameOver(Pane root) {
        Label gameOver = new Label("\u2620 GAME OVER \u2620");
        gameOver.setFont(Font.font("Consolas", 36));
        gameOver.setTextFill(Color.RED);
        gameOver.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-padding: 20;");
        gameOver.setLayoutX(100);
        gameOver.setLayoutY(200);

        Label restart = new Label("Press R to restart");
        restart.setFont(Font.font("Consolas", 20));
        restart.setTextFill(Color.LIGHTGRAY);
        restart.setLayoutX(130);
        restart.setLayoutY(260);

        gameOver.setId("game-over");
        restart.setId("game-over");

        root.getChildren().addAll(gameOver, restart);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

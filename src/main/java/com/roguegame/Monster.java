package com.roguegame;

public class Monster {
    private String name;
    private int x, y;
    private int health;
    private boolean alive;

    public Monster(String name, int x, int y, int health) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.health = health;
        this.alive = true;
    }

    public String getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getHealth() { return health; }
    public boolean isAlive() { return alive; }

    public void damage(int amount) {
        health -= amount;
        if (health <= 0) {
            alive = false;
        }
    }
}

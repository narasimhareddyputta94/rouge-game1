package com.roguegame;

public class Monster {
    private final String name;
    private final int x, y;
    private final int originalHealth;
    private int health;
    private boolean alive;

    public Monster(String name, int x, int y, int health) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.health = health;
        this.originalHealth = health;
        this.alive = true;
    }

    public String getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getHealth() { return health; }
    public boolean isAlive() { return alive; }

    public void damage(int amount) {
        if (!alive) return;
        health = Math.max(0, health - amount);
        if (health == 0) {
            alive = false;
        }
    }


    public void reset() {
        this.health = originalHealth;
        this.alive = true;
    }
}
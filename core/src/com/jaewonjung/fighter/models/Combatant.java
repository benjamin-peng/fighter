package com.jaewonjung.fighter.models;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.jaewonjung.fighter.Fighter;

import java.util.ArrayList;

public abstract class Combatant {

    protected Sprite sprite;
    public Rectangle hitbox;
    public String name;
    public float health;
    public Fighter game;
    protected HealthBar healthBar;
    protected float time;
    protected boolean onPlatform;
    protected float[] velocity;

    public void respawn() {

    }
    public void takeDamage(float damage) {
        health = Math.max(0, health - damage);
    }
    public void render(SpriteBatch batch) {

    }
    public void dispose() {

    }
    public void update(ArrayList<Rectangle> platforms) {

    }
}

package com.jaewonjung.fighter.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.jaewonjung.fighter.Fighter;

import java.util.ArrayList;

public class Dummy {
    private final Sprite dummySprite;
    public Rectangle hitbox;
    public float health;
    public float velocityY;
    public float gravity = (float) - 2000;
    public boolean onPlatform;
    private HealthBar healthBar;
    private Fighter game;

    public Dummy(Fighter game) {
        this.dummySprite = new Sprite(new Texture("dummy.png"));
        this.hitbox = new Rectangle((float) Math.random() * (game.dimensions[0] - dummySprite.getWidth()), (float) Math.random() * game.dimensions[1], dummySprite.getWidth(), dummySprite.getHeight());
        this.health = 100f;
        this.velocityY = 0;
        this.onPlatform = false;
        this.healthBar = new HealthBar(game, "Dummy", health, 1);
        this.game = game;
    }

    public void respawn() {
        health = 100;
        hitbox.setX((float) Math.random() * game.dimensions[0]);
        hitbox.setY((float) Math.random() * game.dimensions[1]);
        dummySprite.setX(hitbox.getX());
        dummySprite.setY(hitbox.getY());
        dummySprite.setAlpha(1.0f);
    }

    public void update(ArrayList<Rectangle> platforms) {
        for (Rectangle platform: platforms) {
            if (hitbox.overlaps(platform)) {
                if (!onPlatform && velocityY < 0 && hitbox.y > platform.y) {
                    velocityY = 0;
                    hitbox.y = platform.y + platform.height - 1;
                    onPlatform = true;
                }
            } else {
                onPlatform = false;
            }
        }
        if (!onPlatform) {
            velocityY += gravity * Gdx.graphics.getDeltaTime();
            hitbox.y += velocityY * Gdx.graphics.getDeltaTime();
        }

        if (hitbox.y < 0) {
            hitbox.y = 0;
            velocityY = 0;
        }

        if (hitbox.x < 0) {
            hitbox.x = 0;
        }
        if (hitbox.x > game.dimensions[0]) {
            hitbox.x = game.dimensions[0];
        }
        dummySprite.setX(hitbox.getX());
        dummySprite.setY(hitbox.getY());
        healthBar.updateHealth(health);
        if (health <= 0) {
            dummySprite.setAlpha(Math.max(0, dummySprite.getColor().a - 0.05f));
            if (dummySprite.getColor().a == 0) {
                respawn();
            }
        }
    }

    public void takeDamage(float damage) {
        health = Math.max(0, health - damage);
    }

    public void render(SpriteBatch batch) {
        healthBar.render(batch);
        dummySprite.draw(batch);
    }


    public void dispose() {

    }
}

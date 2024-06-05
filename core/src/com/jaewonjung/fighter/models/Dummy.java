package com.jaewonjung.fighter.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.jaewonjung.fighter.Fighter;
import com.jaewonjung.fighter.screens.CombatScreen;

import java.util.ArrayList;

public class Dummy extends Combatant {

    public Dummy(Fighter game) {
        this.sprite = new Sprite(new Texture("dummy.png"));
        this.hitbox = new Rectangle((float) Math.random() * (game.dimensions[0] - sprite.getWidth()), (float) Math.random() * game.dimensions[1], sprite.getWidth(), sprite.getHeight());
        this.velocity = new float[] {0,0};
        this.onPlatform = false;
        this.name = "Dummy";
        this.health = 100;
        this.healthBar = new HealthBar(game, name, health, 1);
        this.game = game;
    }

    public void respawn() {
        health = 100;
        hitbox.setX((float) Math.random() * game.dimensions[0]);
        hitbox.setY((float) Math.random() * game.dimensions[1]);
        sprite.setX(hitbox.getX());
        sprite.setY(hitbox.getY());
        sprite.setAlpha(1.0f);
    }

    public void update(ArrayList<Rectangle> platforms) {
        for (Rectangle platform: platforms) {
            if (hitbox.overlaps(platform)) {
                if (!onPlatform && velocity[1] < 0 && hitbox.y > platform.y) {
                    velocity[1] = 0;
                    hitbox.y = platform.y + platform.height - 1;
                    onPlatform = true;
                }
            } else {
                onPlatform = false;
            }
        }
        if (!onPlatform) {
            velocity[1] += CombatScreen.gravity * Gdx.graphics.getDeltaTime();
            hitbox.y += velocity[1] * Gdx.graphics.getDeltaTime();
        }

        if (hitbox.y < 0) {
            hitbox.y = 0;
            velocity[1] = 0;
        }

        if (hitbox.x < 0) {
            hitbox.x = 0;
        }
        if (hitbox.x > game.dimensions[0]) {
            hitbox.x = game.dimensions[0];
        }
        sprite.setX(hitbox.getX());
        sprite.setY(hitbox.getY());
        healthBar.updateHealth(health);
        if (health <= 0) {
            sprite.setAlpha(Math.max(0, sprite.getColor().a - 0.05f));
            if (sprite.getColor().a == 0) {
                respawn();
            }
        }
    }

    public void render(SpriteBatch batch) {
        healthBar.render(batch);
        sprite.draw(batch);
    }


    public void dispose() {

    }
}

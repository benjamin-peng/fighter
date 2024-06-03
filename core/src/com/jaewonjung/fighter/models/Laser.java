package com.jaewonjung.fighter.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Laser {

    private final Texture laserImage;
    public final Rectangle hitbox;
    private int direction;

    public Laser (float x, float y, int direction) {
        this.laserImage = new Texture("laser.png");
        this.hitbox = new Rectangle(x, y, laserImage.getWidth(), laserImage.getHeight());
        this.direction = direction;
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(laserImage, hitbox.getX(), hitbox.getY());
        batch.end();
    }

    public void update() {
        hitbox.setX(hitbox.getX() + direction * 700 * Gdx.graphics.getDeltaTime());
    }

    public float getX() {
        return hitbox.getX();
    }
}

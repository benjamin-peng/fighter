package com.jaewonjung.fighter.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Laser {

    public static float damage;
    private final Texture laserImage;
    public final Rectangle hitbox;
    private int direction;
    private String id;

    public Laser (float x, float y, int direction, String id) {
        this.laserImage = new Texture("laser.png");
        this.hitbox = new Rectangle(x, y, laserImage.getWidth(), laserImage.getHeight());
        this.direction = direction;
        this.id = id;
        damage = 50;
    }

    public void render(SpriteBatch batch) {
        batch.draw(laserImage, hitbox.getX(), hitbox.getY());
    }

    public void update() {
        hitbox.setX(hitbox.getX() + direction * 700 * Gdx.graphics.getDeltaTime());
    }

    public float getX() {
        return hitbox.getX();
    }

    public boolean overlaps(Rectangle r) {
        return hitbox.overlaps(r);
    }

    public String getId() {
        return id;
    }
}

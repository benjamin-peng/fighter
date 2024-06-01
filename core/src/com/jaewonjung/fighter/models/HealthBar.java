package com.jaewonjung.fighter.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.jaewonjung.fighter.Fighter;

public class HealthBar {
    private final Rectangle outerBox;
    private ShapeRenderer shapeRenderer;
    private GlyphLayout glyphLayout;
    private Fighter game;
    private float totalHealth;
    private float currentHealth;
    private String name;

    public HealthBar(Fighter game, String name, float totalHealth) {
        this.outerBox = new Rectangle(30, game.dimensions[1]-70, 100, 20);
        this.shapeRenderer = new ShapeRenderer();
        this.glyphLayout = new GlyphLayout();
        this.game = game;
        this.totalHealth = totalHealth;
        this.currentHealth = totalHealth;
        this.name = name;
    }

    public void updateHealth(float newHealth) {
        currentHealth = newHealth;
    }

    public void render(SpriteBatch batch) {
        batch.end();
        Gdx.gl.glLineWidth(5);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(outerBox.getX(), outerBox.getY(), outerBox.getWidth(), outerBox.getHeight());
        shapeRenderer.end();

        float ratio = ((float) currentHealth) / ((float) totalHealth);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(outerBox.getX()+1, outerBox.getY()+1, (outerBox.getWidth()-2)*ratio, outerBox.getHeight()-2);
        shapeRenderer.end();
        batch.begin();

        game.font.setColor(Color.BLACK);
        game.font.getData().setScale(1, 1);
        String healthText = (int)Math.ceil(currentHealth) + " / " + (int)(totalHealth);
        glyphLayout.setText(game.font, name);
        game.font.draw(game.batch, glyphLayout, outerBox.getX(), outerBox.getY() + outerBox.getHeight() + 30);
        game.font.getData().setScale(0.7f, 0.7f);
        glyphLayout.setText(game.font, healthText);
        game.font.draw(game.batch, glyphLayout, outerBox.getX(), outerBox.getY() + outerBox.getHeight() + 10);

    }

}

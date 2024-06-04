package com.jaewonjung.fighter.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.jaewonjung.fighter.Fighter;
import com.jaewonjung.fighter.models.*;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TrainingScreen extends FighterScreen {

    private Player player;
    private Dummy dummy;
    final private ArrayList<Rectangle> platforms = new ArrayList<>();
    private ShapeRenderer shape;
    private OrthographicCamera camera;
    private List<Laser> lasers;

    public TrainingScreen (final Fighter game) {
        super(game);
    }

    public void handleLasers() {
        Iterator<Laser> it = lasers.iterator();
        while (it.hasNext()) {
            Laser l = it.next();
            l.render(game.batch);
            l.update();
            if (l.getX() > game.dimensions[0] || l.getX() < 0) {
                it.remove();
            }
            else if (l.overlaps(dummy.hitbox)) {
                dummy.takeDamage(Laser.damage);
                it.remove();
            }
        }
    }

    @Override
    public void show () {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.dimensions[0], game.dimensions[1]);
        game.batch = new SpriteBatch();
        shape = new ShapeRenderer();
        lasers = new ArrayList<Laser>();
        Rectangle platform1 = new Rectangle();
        Rectangle platform2 = new Rectangle();
        platform1.set(100, 50, 200, 20);
        platform2.set(400, 200, 200, 20);
        platforms.add(platform1);
        platforms.add(platform2);
        player = new Player(game, lasers);
        dummy = new Dummy(game);
        PlayerInputProcessor ip = new PlayerInputProcessor(player);
        Gdx.input.setInputProcessor(ip);
    }

    @Override
    public void render (float delta) {
        time += delta;
        ScreenUtils.clear(255, 255, 255, 0);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.render(game.batch);
        dummy.render(game.batch);
        handleLasers();
        game.batch.end();
        player.update(platforms);
        dummy.update(platforms);
        for (Rectangle platform : platforms) {
            this.shape.begin(ShapeRenderer.ShapeType.Filled);
            this.shape.setColor(Color.BLUE);
            this.shape.rect(platform.x, platform.y, platform.width, platform.height);
            this.shape.end();
        }
        if (player.hitbox.overlaps(dummy.hitbox)) {
            player.takeDamage(0.01f);
        }
    }

    @Override
    public void dispose () {
        player.dispose();
        dummy.dispose();
    }
}

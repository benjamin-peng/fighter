package com.jaewonjung.fighter.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.jaewonjung.fighter.Fighter;
import com.jaewonjung.fighter.models.Dummy;
import com.jaewonjung.fighter.models.Player;
import com.jaewonjung.fighter.models.PlayerInputProcessor;
import com.jaewonjung.fighter.models.PlayerStatus;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

public class TrainingScreen extends FighterScreen {

    private Player player;
    private Dummy dummy;
    final private ArrayList<Rectangle> platforms = new ArrayList<>();
    private ShapeRenderer shape;
    private OrthographicCamera camera;

    public TrainingScreen (final Fighter game) {
        super(game);
    }

    @Override
    public void show () {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.dimensions[0], game.dimensions[1]);
        game.batch = new SpriteBatch();
        shape = new ShapeRenderer();
        Rectangle platform1 = new Rectangle();
        Rectangle platform2 = new Rectangle();
        platform1.set(100, 50, 200, 20);
        platform2.set(400, 200, 200, 20);
        platforms.add(platform1);
        platforms.add(platform2);
        player = new Player(game);
        dummy = new Dummy();
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
        game.batch.end();
        player.update(platforms);
        dummy.update(platforms);
        for (Rectangle platform : platforms) {
            this.shape.begin(ShapeRenderer.ShapeType.Filled);
            this.shape.setColor(Color.BLUE);
            this.shape.rect(platform.x, platform.y, platform.width, platform.height);
            this.shape.end();
        }
    }

    @Override
    public void dispose () {
        player.dispose();
        dummy.dispose();
    }
}

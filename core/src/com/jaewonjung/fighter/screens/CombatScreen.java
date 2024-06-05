package com.jaewonjung.fighter.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.jaewonjung.fighter.Fighter;
import com.jaewonjung.fighter.models.Combatant;
import com.jaewonjung.fighter.models.Laser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class CombatScreen extends FighterScreen {

    final protected ArrayList<Rectangle> platforms;
    final protected ArrayList<Combatant> combatants;
    final protected ShapeRenderer shape;
    final protected OrthographicCamera camera;
    final protected List<Laser> lasers;
    public static float gravity;

    public CombatScreen (final Fighter game) {
        super(game);
        platforms = new ArrayList<>();
        platforms.add(new Rectangle((game.dimensions[0]-300)/5f, 150, 300, 20));
        platforms.add(new Rectangle((game.dimensions[0]-300)*4/5f, 150, 300, 20));
        platforms.add(new Rectangle((game.dimensions[0]-300)/2f, 300, 300, 20));
        combatants = new ArrayList<>();
        lasers = new ArrayList<>();
        camera = new OrthographicCamera();
        game.batch = new SpriteBatch();
        shape = new ShapeRenderer();
        gravity = -2000f;
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
            else {
                for (Combatant combatant : combatants) {
                    if (combatant.hitbox.overlaps(l.hitbox) && !combatant.name.equals(l.getId())) {
                        combatant.takeDamage(Laser.damage);
                        it.remove();
                    }
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        time += delta;
        ScreenUtils.clear(255, 255, 255, 0);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        for (Combatant combatant : combatants) {
            combatant.render(game.batch);
        }
        handleLasers();
        game.batch.end();
        for (Combatant combatant : combatants) {
            combatant.update(platforms);
        }
        for (Rectangle platform : platforms) {
            this.shape.begin(ShapeRenderer.ShapeType.Filled);
            this.shape.setColor(Color.BLUE);
            this.shape.rect(platform.x, platform.y, platform.width, platform.height);
            this.shape.end();
        }
    }

    @Override
    public void dispose () {
        for (Combatant combatant : combatants) {
            combatant.dispose();
        }
    }

}

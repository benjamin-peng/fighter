package com.jaewonjung.fighter.screens;

import com.badlogic.gdx.Gdx;
import com.jaewonjung.fighter.Fighter;
import com.jaewonjung.fighter.models.*;

public class TrainingScreen extends CombatScreen {

    private Player player;
    private Dummy dummy;

    public TrainingScreen (final Fighter game) {
        super(game);
    }

    @Override
    public void show () {
        camera.setToOrtho(false, game.dimensions[0], game.dimensions[1]);
        player = new Player(game, lasers);
        dummy = new Dummy(game);
        combatants.add(player);
        combatants.add(dummy);

        PlayerInputProcessor ip = new PlayerInputProcessor(player);
        Gdx.input.setInputProcessor(ip);
    }

}

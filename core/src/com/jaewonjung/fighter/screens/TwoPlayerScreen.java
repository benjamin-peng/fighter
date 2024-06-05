package com.jaewonjung.fighter.screens;

import com.badlogic.gdx.Gdx;
import com.jaewonjung.fighter.Fighter;
import com.jaewonjung.fighter.models.*;

public class TwoPlayerScreen extends CombatScreen {

    private Player playerOne;

    public TwoPlayerScreen (final Fighter game) {
        super(game);
    }

    @Override
    public void show () {
        camera.setToOrtho(false, game.dimensions[0], game.dimensions[1]);
        playerOne = new Player(game, lasers);
        combatants.add(playerOne);

        PlayerInputProcessor ip = new PlayerInputProcessor(playerOne);
        Gdx.input.setInputProcessor(ip);
    }

}

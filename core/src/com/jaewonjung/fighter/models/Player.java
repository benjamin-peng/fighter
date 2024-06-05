package com.jaewonjung.fighter.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.jaewonjung.fighter.Fighter;
import com.jaewonjung.fighter.screens.CombatScreen;

import java.util.*;

public class Player extends Combatant {
    private final Texture stillImage;
    private final Texture crouchImage;
    private final Texture shootImage;
    public final Animation<Texture> runningAnimation;
    public float jumpVelocity = 800;
    public HashMap<Rectangle, Boolean> canPass;
    public boolean platformPass; //whether the player has double tapped to trigger
    public int jumpsLeft = 2;
    public int facingDirection;
    public int movingDirection;
    public long[] keyTime;
    private final List<Laser> lasers;
    private final Fighter game;

    public PlayerStatus playerStatus;
    public Player(Fighter game, List<Laser> lasers) {
        this.stillImage = new Texture("stick.png");
        this.crouchImage = new Texture("crouch.png");
        this.shootImage = new Texture("shoot.png");
        this.sprite = new Sprite(stillImage);
        this.health = 100;
        this.name = "Stickman";
        this.healthBar = new HealthBar(game, name, health, 0);
        this.playerStatus = PlayerStatus.STILL;
        this.hitbox = new Rectangle(368, 0, 30, 64);
        this.velocity = new float[] {0, 0};
        this.onPlatform = false;
        this.canPass = new HashMap<>();
        this.platformPass = false;
        this.facingDirection = 1;
        this.movingDirection = 0;
        this.keyTime = new long[5];
        this.lasers = lasers;
        this.game = game;

        Texture[] runFrames = new Texture[6];
        for (int i = 0; i < 6; i++) {
            runFrames[i] = new Texture("stickrun/000" + (i+1) + ".png");
        }
        this.runningAnimation = new Animation<Texture>(0.025f, runFrames);

        Arrays.fill(this.keyTime, -1);
    }

    public void takeDamage(float damage) {
        health = Math.max(0, health - damage);
    }

    public void respawn() {

    }

    private void checkOnPlatform(ArrayList<Rectangle> platforms) {
        onPlatform = false;
        for (Rectangle platform: platforms) {
            canPass.putIfAbsent(platform, false);
            if (hitbox.getY() <= platform.getY()) {
                canPass.put(platform, true);
            }
            else {
                canPass.put(platform, false);
            }
            if (hitbox.overlaps(platform)) {
                if (!onPlatform && !platformPass && !canPass.get(platform) && velocity[1] < 0 && hitbox.getY() > platform.getY()) {
                    velocity[1] = 0;
                    hitbox.setY(platform.getY() + platform.getHeight() - 1);
                    onPlatform = true;
                    jumpsLeft = 2;
                }
            }
        }
    }

    private void updateVelocity() {
        if (hitbox.getY() <= 0) {
            velocity[1] = 0;
        }
        else if (!onPlatform) {
            velocity[1] += CombatScreen.gravity * Gdx.graphics.getDeltaTime();
        }
        if (movingDirection == 0 && velocity[0] != 0) {
            if (velocity[0] > 0) {
                velocity[0] = Math.max(0, velocity[0] - 1500 * Gdx.graphics.getDeltaTime());
            } else {
                velocity[0] = Math.min(0, velocity[0] + 1500 * Gdx.graphics.getDeltaTime());
            }
        }
    }

    private void updatePosition() {
        if (!onPlatform) {
            hitbox.setY(hitbox.getY() + velocity[1] * Gdx.graphics.getDeltaTime());
        }
        if ((hitbox.getY() <= 0 || onPlatform) &&
                (playerStatus == PlayerStatus.CROUCH || playerStatus == PlayerStatus.SHOOT)) { //decel when landing crouched
            movingDirection = 0;
        }
        hitbox.setX(hitbox.getX() + velocity[0] * Gdx.graphics.getDeltaTime());
        if (hitbox.getY() < 0) {
            hitbox.setY(0);
            jumpsLeft = 2;
        }
        if (platformPass && TimeUtils.millis() - keyTime[0] > 200) {
            platformPass = false;
        }
    }

    private void checkBounds() {
        if (hitbox.getX() < 0) {
            hitbox.setX(0);
        }
        if (hitbox.getX() > game.dimensions[0] - hitbox.getWidth()) {
            hitbox.setX(game.dimensions[0] - hitbox.getWidth());
        }
    }


    public void createLaser() {
        Laser l = new Laser(hitbox.getX(), hitbox.getY()+48, facingDirection, name);
        lasers.add(l);
    }

    public String getName() {
        return name;
    }

    public void update(ArrayList<Rectangle> platforms) {
        time += Gdx.graphics.getDeltaTime();
        healthBar.updateHealth(health);
        updatePosition();
        updateVelocity();
        checkBounds();
        checkOnPlatform(platforms);
    }

    private void setSpriteDirection() {
        if ((facingDirection == 1 && sprite.isFlipX()) || (facingDirection != 1 && !sprite.isFlipX())) {
            sprite.flip(true, false);
        }
    }

    private void updateSpriteRegion() {
        switch (playerStatus) {
            case CROUCH:
                //account for discrepancy in image size
                hitbox.setX((sprite.getRegionWidth() - crouchImage.getWidth()) / 2.0f + hitbox.getX());
                sprite.setRegion(crouchImage);
                break;
            case SHOOT:
                hitbox.setX((sprite.getRegionWidth() - shootImage.getWidth()) / 2.0f + hitbox.getX());
                sprite.setRegion(shootImage);
                setSpriteDirection();
                break;
            case STILL:
                hitbox.setX((sprite.getRegionWidth() - stillImage.getWidth()) / 2.0f + hitbox.getX());
                sprite.setRegion(stillImage);
                break;
            case RUNNING:
                if (velocity[0] == 0) {
                    playerStatus = PlayerStatus.STILL;
                }
                else {
                    Texture currentFrame = runningAnimation.getKeyFrame(time / 2f, true);
                    hitbox.setX((sprite.getRegionWidth() - currentFrame.getWidth()) / 2.0f + hitbox.getX());
                    sprite.setRegion(currentFrame);
                    setSpriteDirection();
                }
                break;
        }

        float width = sprite.getRegionWidth();
        float height = sprite.getRegionHeight();
        if (width != hitbox.getWidth() || height != hitbox.getHeight()) {
            hitbox.setWidth(width);
            hitbox.setHeight(height);
        }
    }


    public void render(SpriteBatch batch) {
        healthBar.render(batch);
        updateSpriteRegion();
        if (playerStatus == PlayerStatus.SHOOT) {
            long currentTime = TimeUtils.millis();
            if (currentTime - keyTime[4] > 300) {
                createLaser();
                keyTime[4] = currentTime;
            }
        }
        sprite.setBounds(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
        sprite.draw(batch);
    }

    public void dispose() {
        stillImage.dispose();
        crouchImage.dispose();
    }
}

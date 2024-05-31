package com.jaewonjung.fighter.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.jaewonjung.fighter.Fighter;

import java.util.*;

public class Player {
    private final Texture stillImage;
    private final Texture crouchImage;
    private final TextureRegion currentRegion;
    public final Animation<Texture> runningAnimation;
    private float time;
    public final Rectangle hitbox;
    public float velocityX;
    public float velocityY;
    public float width;
    public float height;
    public float gravity = (float) - 2000;
    public float jumpVelocity = 800;
    public boolean onPlatform;
    public HashMap<Rectangle, Boolean> canPass;
    public boolean platformPass; //whether the player has double tapped to trigger
    public int jumpsLeft = 2;
    public int health;
    public int facingDirection;
    public int movingDirection;
    private boolean inAttack = false;
    public long[] keyTime;
    private Fighter game;

    public PlayerStatus playerStatus;
    public Player(Fighter game) {
        this.stillImage = new Texture("stick.png");
        this.crouchImage = new Texture("crouch.png");
        this.currentRegion = new TextureRegion(stillImage);
        this.playerStatus = PlayerStatus.STILL;
        this.hitbox = new Rectangle(368, 0, 30, 64);
        this.velocityY = 0;
        this.velocityX = 0;
        this.width = currentRegion.getRegionWidth();
        this.height = currentRegion.getRegionHeight();
        this.onPlatform = false;
        this.canPass = new HashMap<>();
        this.platformPass = false;
        this.health = 100;
        this.facingDirection = 0;
        this.movingDirection = 0;
        this.keyTime = new long[4];
        this.game = game;
        this.time = 0f;

        Texture[] runFrames = new Texture[6];
        for (int i = 0; i < 6; i++) {
            runFrames[i] = new Texture("stickrun/000" + (i+1) + ".png");
        }
        this.runningAnimation = new Animation<Texture>(0.025f, runFrames);

        Arrays.fill(this.keyTime, -1);
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
                if (!onPlatform && !platformPass && !canPass.get(platform) && velocityY < 0 && hitbox.getY() > platform.getY()) {
                    velocityY = 0;
                    hitbox.setY(platform.getY() + platform.getHeight() - 1);
                    onPlatform = true;
                    jumpsLeft = 2;
                }
            }
        }
    }

    private void updateVelocity() {
        if (hitbox.getY() <= 0) {
            velocityY = 0;
        }
        else if (!onPlatform) {
            velocityY += gravity * Gdx.graphics.getDeltaTime();
        }
        if (movingDirection == 0 && velocityX != 0) {
            if (velocityX > 0) {
                velocityX = Math.max(0, velocityX - 1500 * Gdx.graphics.getDeltaTime());
            } else {
                velocityX = Math.min(0, velocityX + 1500 * Gdx.graphics.getDeltaTime());
            }
        }
    }

    private void updatePosition() {
        if (!onPlatform) {
            hitbox.setY(hitbox.getY() + velocityY * Gdx.graphics.getDeltaTime());
        }
        if ((hitbox.getY() <= 0 || onPlatform) && playerStatus == PlayerStatus.CROUCH) { //decel when landing crouched
            movingDirection = 0;
        }
        hitbox.setX(hitbox.getX() + velocityX * Gdx.graphics.getDeltaTime());
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

    public void update(ArrayList<Rectangle> platforms) {
        time += Gdx.graphics.getDeltaTime();
        checkOnPlatform(platforms);
        updatePosition();
        updateVelocity();
        checkBounds();
    }

    public void attack(Dummy opponent) {
        if (!inAttack) {
            Rectangle attackHitBox = new Rectangle(this.hitbox.x, this.hitbox.y, this.hitbox.width / 2 + 80, this.hitbox.height);
            if (attackHitBox.overlaps(opponent.hitbox)) {
                //attack render, move opponent to the direction of attack
                opponent.hitbox.setX(opponent.hitbox.getX() + this.facingDirection * 20);
                opponent.hitbox.setY(opponent.hitbox.getY() + 10);
            }
        }
    }

    private void updateCurrentRegion() {
        switch (playerStatus) {
            case CROUCH:
                //account for discrepancy in image size
                hitbox.setX((currentRegion.getRegionWidth() - crouchImage.getWidth()) / 2.0f + hitbox.getX());
                currentRegion.setRegion(crouchImage);
                break;
            case STILL:
                hitbox.setX((currentRegion.getRegionWidth() - stillImage.getWidth()) / 2.0f + hitbox.getX());
                currentRegion.setRegion(stillImage);
                break;
            case RUNNING:
                if (velocityX == 0) {
                    playerStatus = PlayerStatus.STILL;
                }
                else {
                    Texture currentFrame = runningAnimation.getKeyFrame(time / 2, true);
                    hitbox.setX((currentRegion.getRegionWidth() - currentFrame.getWidth()) / 2.0f + hitbox.getX());
                    currentRegion.setRegion(currentFrame);
                    if ((facingDirection == 1 && currentRegion.isFlipX()) || (facingDirection != 1 && !currentRegion.isFlipX())) {
                        currentRegion.flip(true, false);
                    }
                }
                break;
        }
        width = currentRegion.getRegionWidth();
        height = currentRegion.getRegionHeight();
        if (width != hitbox.getWidth() || height != hitbox.getHeight()) {
            hitbox.setWidth(width);
            hitbox.setHeight(height);
        }
    }


    public void render(SpriteBatch batch) {
        updateCurrentRegion();
        batch.draw(currentRegion, hitbox.x, hitbox.y, width, height);//, 100, (int) (height / width) * 100);
    }

    public void dispose() {
        stillImage.dispose();
        crouchImage.dispose();
    }
}

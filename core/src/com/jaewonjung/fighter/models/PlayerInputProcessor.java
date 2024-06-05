package com.jaewonjung.fighter.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Arrays;

public class PlayerInputProcessor implements InputProcessor {

    private final Player p;
    private long jumpTime;
    private long leftTime;
    private long rightTime;

    public PlayerInputProcessor(Player p) {
        this.p = p;
    }

    private void exitNoMotionState() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            p.velocity[0] = -400;
            p.facingDirection = -1;
            p.movingDirection = -1;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            p.velocity[0] = 400;
            p.facingDirection = 1;
            p.movingDirection = 1;
        }
        if (p.velocity[0] != 0) p.playerStatus = PlayerStatus.RUNNING;
        else p.playerStatus = PlayerStatus.STILL;
    }

    public boolean keyDown (int keycode) {
        switch (keycode) {
            case Input.Keys.Z:
                p.playerStatus = PlayerStatus.SHOOT;
                break;
            case Input.Keys.UP:
                if (p.jumpsLeft > 0) {
                    p.onPlatform = false;
                    p.velocity[1] = p.jumpVelocity;
                    p.jumpsLeft -= 1;
                    p.keyTime[0] = TimeUtils.millis();
                }
                break;
            case Input.Keys.DOWN:
                p.playerStatus = PlayerStatus.CROUCH;
                if (p.onPlatform || p.hitbox.getY() == 0) p.movingDirection = 0; //decel if on ground
                long currentTime = TimeUtils.millis();
                if (currentTime - p.keyTime[0] < 200) {
                    p.platformPass = true;
                }
                else {
                    p.velocity[1] -= 400;
                }
                p.keyTime[0] = currentTime;

                break;
            case Input.Keys.LEFT:
                p.velocity[0] = -400;
                p.facingDirection = -1;
                p.playerStatus = PlayerStatus.RUNNING;
                p.movingDirection = -1;
                p.keyTime[2] = TimeUtils.millis();
                break;
            case Input.Keys.RIGHT:
                p.facingDirection = 1;
                p.velocity[0] = 400;
                p.movingDirection = 1;
                p.playerStatus = PlayerStatus.RUNNING;
                p.keyTime[3] = TimeUtils.millis();
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean keyUp (int keycode) {
        switch (keycode) {
            case Input.Keys.Z:
            case Input.Keys.DOWN:
                exitNoMotionState();
                break;
            case Input.Keys.UP:
                float jumpDuration = TimeUtils.millis() - p.keyTime[0];
                if (jumpDuration < 300) {
                    p.velocity[1] = Math.min(0, p.velocity[1] - 80);
                }
                break;
            case Input.Keys.LEFT:
                float leftDuration = TimeUtils.millis() - p.keyTime[2];
                if (leftDuration < 300) {
                    p.velocity[0] = Math.min(0, p.velocity[0] + 200);
                }
                if (p.movingDirection == -1) {
                    p.movingDirection = 0;
                }
                break;
            case Input.Keys.RIGHT:
                float rightDuration = TimeUtils.millis() - p.keyTime[3];
                if (rightDuration < 300) {
                    p.velocity[0] = Math.max(0, p.velocity[0] - 200);
                }
                if (p.movingDirection == 1) {
                    p.movingDirection = 0;
                }
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean keyTyped (char character) {
        return false;
    }

    public boolean touchDown (int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchUp (int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchDragged (int x, int y, int pointer) {
        return false;
    }

    public boolean mouseMoved (int x, int y) {
        return false;
    }

    public boolean scrolled (float amountX, float amountY) {
        return false;
    }

    public boolean touchCancelled(int x, int y, int pointer, int button) {
        return false;
    }
}

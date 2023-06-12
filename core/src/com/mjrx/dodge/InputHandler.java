package com.mjrx.dodge;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;

public class InputHandler implements InputProcessor {
    DodgeGame game;
    Player player;
    private boolean wHeld = false;
    private boolean sHeld = false;
    private boolean aHeld = false;
    private boolean dHeld = false;

    public InputHandler(DodgeGame game) {
        this.game = game;
        this.player = game.player;
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean handled = false;

        // Modify velocity based on key pressed
        if (keycode == Keys.W) {
            wHeld = true;
            handled = true;
        }else if (keycode == Keys.S) {
            sHeld = true;
            handled = true;
        }

        if (keycode == Keys.D) {
            dHeld = true;
            handled = true;
        } else if (keycode == Keys.A) {
            aHeld = true;
            handled = true;
        }

        calculatePlayerVelocity();

        return handled;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean handled = false;

        // Modify velocity based on key pressed
        if (keycode == Keys.W) {
            wHeld = false;
            handled = true;
        } else if (keycode == Keys.S) {
            sHeld = false;
            handled = true;
        }

        if (keycode == Keys.D) {
            dHeld = false;
            handled = true;
        } else if (keycode == Keys.A) {
            aHeld = false;
            handled = true;
        }

        calculatePlayerVelocity();

        return handled;
    }

    /**
     * Calculate and set the player velocity, based on the 'key held' variables
     */
    public void calculatePlayerVelocity() {
        if (wHeld == sHeld) player.vel.y = 0;
        else if (wHeld) player.vel.y = (float) Player.SPEED;
        else if (sHeld) player.vel.y = (float) -Player.SPEED; // always true, but improved readability

        if (aHeld == dHeld) player.vel.x = 0;
        else if (dHeld) player.vel.x = (float) Player.SPEED;
        else if (aHeld) player.vel.x = (float) -Player.SPEED;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}

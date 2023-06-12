package com.mjrx.dodge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Disposable;

public class Player extends GameObject {
    public static final double WIDTH = 10;
    public static final double SPEED = 3;
    private Rectangle collisionBox;


    public Player(float x, float y) {
        super(x, y, 0f, 0f);

        collisionBox = new Rectangle(x, y, (float) WIDTH, (float) WIDTH);

        texture = new Texture(Gdx.files.internal("player.png"));
    }

    @Override
    public void update() {
        super.update();

        pos.x = MathUtils.clamp(pos.x, 0f, (float) (DodgeGame.VIEW_WIDTH - Player.WIDTH));
        pos.y = MathUtils.clamp(pos.y, 0f, (float) (DodgeGame.VIEW_HEIGHT - Player.WIDTH));
    }

    @Override
    public Rectangle getCollisionRect() {
        collisionBox.setPosition(pos);
        return collisionBox;
    }

    @Override
    public void dispose() {
        super.dispose();
        texture.dispose();
    }
}

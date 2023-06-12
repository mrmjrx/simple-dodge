package com.mjrx.dodge;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class Enemy extends GameObject {
    public static final double SIZE = 10;
    public static final double MAX_SPEED = 10d;
    public static final double OFFSET_BEFORE_DESPAWN = 20d;
    public int listIndex = -1;
    private Rectangle collisionRect;


    public Enemy(float x, float y, float velX, float velY, Texture texture) {
        super(x, y, velX, velY);

        collisionRect = new Rectangle(x, y, (float) SIZE, (float) SIZE);

        this.texture = texture;
    }

    @Override
    public Rectangle getCollisionRect() {
        collisionRect.setPosition(pos);
        return collisionRect;
    }

    public boolean isValidPos() {
        boolean xValid = ((0 - OFFSET_BEFORE_DESPAWN) <= pos.x && pos.x <= (OFFSET_BEFORE_DESPAWN + DodgeGame.VIEW_WIDTH - SIZE));
        boolean yValid = ((0 - OFFSET_BEFORE_DESPAWN) <= pos.y && pos.y <= (OFFSET_BEFORE_DESPAWN + DodgeGame.VIEW_HEIGHT - SIZE));

        return xValid && yValid;
    }
}

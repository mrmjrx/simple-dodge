package com.mjrx.dodge;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public abstract class GameObject implements Disposable {
    protected Texture texture;
    public Vector2 vel;
    public Vector2 pos;

    public GameObject(float x, float y, float velX, float velY) {
        pos = new Vector2(x, y);
        vel = new Vector2(velX, velY);
    }

    public void update() {
        pos.x += vel.x;
        pos.y += vel.y;
    }

    public void draw(SpriteBatch batch) {

        batch.draw(texture, pos.x, pos.y);
    }

    @Override
    public void dispose() {}

    public abstract Rectangle getCollisionRect();
}

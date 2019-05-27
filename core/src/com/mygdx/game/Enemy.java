package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    private static final int WIDTH = 58;
    private static final int HEIGHT = 58;
    private static final float SPEED = 210 / 60;
    private static final float FRAME_DURATION = 0.5F;
    private final Animation<TextureRegion> enemyAnimation;
    private static TiledGameMap map;
    private Rectangle collisionRect;
    private boolean isVertical;
    private boolean isStatic;
    private Vector2 pos;
    private float velocityX;
    private float velocityY;
    private float animationTimer;

    Enemy(float x, float y, boolean isVertical, TextureRegion enemyTexture1, TextureRegion enemyTexture2) {
        this.pos = new Vector2(x, y);
        collisionRect = new Rectangle(x, y, WIDTH, HEIGHT);
        this.isVertical = isVertical;
        velocityX = velocityY = SPEED;
        enemyAnimation = new Animation<>(FRAME_DURATION, enemyTexture1, enemyTexture2);
        enemyAnimation.setPlayMode(Animation.PlayMode.LOOP);
        animationTimer = 0;
    }

    Enemy(float x, float y, float width, float  height) {
        this.pos = new Vector2(x, y);
        collisionRect = new Rectangle(x, y, width, height);
        this.isStatic = true;
        enemyAnimation = null;
    }

    public void update(float delta) {
        animationTimer += delta;
        if (animationTimer > 100) animationTimer = 0;
        if (!isStatic) {
            if (isVertical) {
                //pos.y += velocityY * delta;
                pos.y += velocityY;
                if (map.doesRectCollideWithMap(pos.x, pos.y, WIDTH, HEIGHT)) {
                    velocityY = -velocityY;
                }
            } else {
                //pos.x += velocityX * delta;
                pos.x += velocityX;
                if (map.doesRectCollideWithMap(pos.x, pos.y, WIDTH, HEIGHT)) {
                    velocityX = -velocityX;
                }
            }
        }
    }

    public boolean GameOver(Vector2 cubePos) {
        if (isStatic) {
            return collisionRect.overlaps(new Rectangle(cubePos.x, cubePos.y, WIDTH, HEIGHT));
        } else {
            collisionRect.setPosition(pos);
            return collisionRect.overlaps(new Rectangle(cubePos.x, cubePos.y, WIDTH, HEIGHT));
        }
    }

    public void shapeRenderer(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(collisionRect.x, collisionRect.y, collisionRect.width, collisionRect.height);
    }

    public void draw(SpriteBatch batch) {
        if (!isStatic) {
            TextureRegion enemy = enemyAnimation.getKeyFrame(animationTimer);
            batch.draw(enemy, pos.x, pos.y);
        }
//        else {
//            batch.draw(enemy, pos.x, pos.y, collisionRect.width, collisionRect.height);
//        }
    }

    public static void setMap(TiledGameMap map) {
        Enemy.map = map;
    }
}
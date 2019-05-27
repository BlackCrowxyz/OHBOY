package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private static final int SPEED = 500;
    private static final int JUMP_VELOCITY = 10; //TODO
    private static final int GRAVITY = -25;
    private static final int WIDTH = 62;
    private static final int HEIGHT = 62;
    private static final int WEIGHT = 100;
    private static final int START_X_POSITION = 256;
    private static final int START_Y_POSITION = 448;
    private final TextureRegion standing;
    private final TextureRegion moving;
    private TiledGameMap map;
    private Vector2 pos;
    private float timer;
    private float velocityX;
    private float velocityY;
    private boolean grounded = false;
    private boolean right, left, jump;

    Player(float x, float y, TiledGameMap map, TextureRegion standing, TextureRegion moving) {
        this.pos = new Vector2(x, y);
        this.map = map;
        this.standing = standing;
        this.moving = moving;
        timer = 0;
        velocityX = 0;
        velocityY = 0;
    }

    public void update(float deltaTime) {
        timer += deltaTime;
        if (timer > 0.5F) moveActions(deltaTime);//TODO: i think i should fix this with better method
        applyGravity(deltaTime);
    }

    private void moveActions(float deltaTime) {
        if ((Gdx.input.isKeyPressed(Keys.SPACE) || jump) && grounded) {
            this.velocityY += JUMP_VELOCITY * getWeight();
        } else if ((Gdx.input.isKeyPressed(Keys.SPACE) || jump) && !grounded && this.velocityY > 0) {
            this.velocityY += JUMP_VELOCITY * getWeight() * deltaTime;
        }

        if (Gdx.input.isKeyPressed(Keys.RIGHT) || right) {
            moveX(SPEED * deltaTime);
        } else if (Gdx.input.isKeyPressed(Keys.LEFT) || left) {
            moveX(-SPEED * deltaTime);
        } else {
            velocityX = 0;
        }
    }

    private void applyGravity(float deltaTime) {
        float newY = pos.y;

        this.velocityY += GRAVITY * getWeight() * deltaTime;
        newY += this.velocityY * deltaTime;

        if (map.doesRectCollideWithMap(pos.x, newY, getWidth(), getHeight())) {
            if (velocityY < 0) {
                //this.pos.y = (float) Math.floor(pos.y);
                if (pos.y % 64 != 0) this.pos.y = pos.y - (pos.y % 64);
                grounded = true;
            }
            this.velocityY = 0;
        } else {
            this.pos.y = newY;
            grounded = false;
        }
    }

    private void moveX(float amount) {
        velocityX = (amount > 0) ? 1 : -1;
        float newX = pos.x + amount;
        if (!map.doesRectCollideWithMap(newX, pos.y, getWidth(), getHeight())){
            this.pos.x = newX;
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(pos.x, pos.y, getWidth(), getHeight());
    }

    public void draw(SpriteBatch batch) {
        TextureRegion toDraw = standing;
        if (velocityX != 0) toDraw = moving;
        if (velocityX > 0) {
            if (toDraw.isFlipX()) toDraw.flip(true, false);
        } else if (velocityX < 0){
            if (!toDraw.isFlipX()) toDraw.flip(true, false);
        }
        batch.draw(toDraw, pos.x, pos.y);
    }

    public float getX() {
        return pos.x;
    }

    public float getY() {
        return pos.y;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    private float getWeight() {
        return WEIGHT;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void doRestart() {
        timer = 0;
        //setPosition();
        this.pos.x = START_X_POSITION;
        this.pos.y = START_Y_POSITION;
        velocityX = 0;
        this.velocityY = (float) 0;
        //moveX(0);
    }

    public void dispose() {
    }
}
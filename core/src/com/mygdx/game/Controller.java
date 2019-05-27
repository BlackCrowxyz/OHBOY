package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Controller {
    private static final float WORLD_WIDTH = 1920;
    private static final float WORLD_HEIGHT = 1024;
    private Viewport viewport;
    private Stage stage;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upPressed;
    private boolean pausePressed;

    Controller(CubeGame cubeGame, SpriteBatch batch) {
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, new OrthographicCamera());
        viewport.apply(true);
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        TextureAtlas textureAtlas = cubeGame.getAssetManager().get("pack.pack");
        TextureRegion rightUpTexture = textureAtlas.findRegion("right");
        TextureRegion rightPressedTexture = textureAtlas.findRegion("rightPressed");
        TextureRegion leftUpTexture = textureAtlas.findRegion("left");
        TextureRegion leftPressedTexture = textureAtlas.findRegion("leftPressed");
        TextureRegion jumpUpTexture = textureAtlas.findRegion("up");
        TextureRegion jumpPressedTexture = textureAtlas.findRegion("upPressed");
        TextureRegion pauseUpTexture = textureAtlas.findRegion("pause");
        TextureRegion pausePressedTexture = textureAtlas.findRegion("pausePressed");

        Table table = new Table();
        table.left().bottom();

        ImageButton right = new ImageButton(new TextureRegionDrawable(new TextureRegion(rightUpTexture)), new TextureRegionDrawable(new TextureRegion(rightPressedTexture)));
        right.setSize(rightUpTexture.getRegionWidth(), rightUpTexture.getRegionHeight());
        right.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        ImageButton left = new ImageButton(new TextureRegionDrawable(new TextureRegion(leftUpTexture)), new TextureRegionDrawable(new TextureRegion(leftPressedTexture)));
        left.setSize(leftUpTexture.getRegionWidth(), leftUpTexture.getRegionHeight());
        left.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });

        ImageButton up = new ImageButton(new TextureRegionDrawable(new TextureRegion(jumpUpTexture)), new TextureRegionDrawable(new TextureRegion(jumpPressedTexture)));
        up.setSize(jumpUpTexture.getRegionWidth(), jumpUpTexture.getRegionHeight());
        up.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });

        ImageButton pause = new ImageButton(new TextureRegionDrawable(new TextureRegion(pauseUpTexture)), new TextureRegionDrawable(new TextureRegion(pausePressedTexture)));
        pause.setSize(pauseUpTexture.getRegionWidth(), pauseUpTexture.getRegionHeight());
        pause.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                pausePressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                pausePressed = false;
            }
        });

        table.add();
        table.add();
        table.add(pause).size(pause.getWidth(), pause.getHeight()).padBottom(WORLD_HEIGHT - 430).top().right().padLeft(1270);
        table.row();
        table.add(left).size(left.getWidth(), left.getHeight()).padRight(15).bottom();
        table.add(right).size(right.getWidth(), right.getHeight()).bottom();
        table.add(up).size(up.getWidth(), up.getHeight()).bottom().right().padRight(15);
        //table.setDebug(true);
        stage.addActor(table);
    }
    
    public void draw() {
        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isPausePressed() { return pausePressed; }

    public void setPausePressed(boolean pausePressed) { this.pausePressed = pausePressed; }

    public Stage getStage() { return stage; }
}

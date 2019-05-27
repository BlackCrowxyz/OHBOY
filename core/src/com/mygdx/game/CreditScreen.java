package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

class CreditScreen extends ScreenAdapter {
    private static final float WORLD_WIDTH = 1920;
    private static final float WORLD_HEIGHT = 1024;
    private static final int BACK_BUTTON_SIZE = 100;
    private static final float PETER_RIVER_RED = 155f / 255;
    private static final float PETER_RIVER_GREEN = 89f / 255;
    private static final float PETER_RIVER_BLUE = 182f / 255;
    private static final float PETER_RIVER_ALPHA = 1;
    private final CubeGame cubeGame;
    private Stage stage;
    private TextureAtlas textureAtlas;

    CreditScreen(CubeGame cubeGame) {
        this.cubeGame = cubeGame;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        textureAtlas = cubeGame.getAssetManager().get("pack.pack");

        TextureRegion creditTexture = textureAtlas.findRegion("Credits");
        Image credits = new Image(creditTexture);
        stage.addActor(credits);

        TextureRegion backTexture = textureAtlas.findRegion("back");
        TextureRegion backPressedTexture = textureAtlas.findRegion("backPressed");
        ImageButton back = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(backTexture)),
                new TextureRegionDrawable(new TextureRegion(backPressedTexture))
        );
        back.setPosition(WORLD_HEIGHT - BACK_BUTTON_SIZE, BACK_BUTTON_SIZE, Align.center);
        back.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                cubeGame.setScreen(new MenuScreen(cubeGame));
                // dispose();
            }
        });
        stage.addActor(back);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        stage.act(delta);
        stage.draw();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(PETER_RIVER_RED,PETER_RIVER_GREEN,PETER_RIVER_BLUE, PETER_RIVER_ALPHA); //seting a sky like color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        System.out.println("Dispose: CreditScreen");
        textureAtlas.dispose();
        stage.dispose();
        Gdx.input.setInputProcessor(null);
    }
}

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

class MenuScreen extends ScreenAdapter {

    private static final float WORLD_WIDTH = 1920;
    private static final float WORLD_HEIGHT = 1024;
    private static final float PETER_RIVER_RED = 155f / 255;
    private static final float PETER_RIVER_GREEN = 89f / 255;
    private static final float PETER_RIVER_BLUE = 182f / 255;
    private static final float PETER_RIVER_ALPHA = 1;
    private final CubeGame cubeGame;
    private Stage stage;

    MenuScreen(final CubeGame cubeGame) {
        this.cubeGame = cubeGame;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        TextureAtlas textureAtlas = cubeGame.getAssetManager().get("pack.pack");

        TextureRegion bgTexture = textureAtlas.findRegion("Background");
        Image logo = new Image(bgTexture);
        stage.addActor(logo);

        TextureRegion playTexture = textureAtlas.findRegion("Btn");
        TextureRegion playPressedTexture = textureAtlas.findRegion("BtnPressed");
        ImageButton play = new ImageButton(new TextureRegionDrawable(new TextureRegion(playTexture)), new TextureRegionDrawable(new TextureRegion(playPressedTexture)));
        //play.setPosition((WORLD_WIDTH / 4 - play.getWidth() / 3), (3 * WORLD_HEIGHT / 7), Align.center);
        //play.setPosition((WORLD_WIDTH) / 2, (WORLD_HEIGHT / 2 - play.getHeight()), Align.center);
        play.setPosition((WORLD_WIDTH) / 2, (2 * WORLD_HEIGHT / 3 - play.getHeight()), Align.center);
        play.addListener(new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                //cubeGame.setScreen(new GameScreen(cubeGame, 1));
                cubeGame.setScreen(new LevelSelectScreen(cubeGame, cubeGame.getBatch()));
                //dispose();
            }
        });
        stage.addActor(play);

        TextureRegion creditTexture = textureAtlas.findRegion("creditPressed");
        TextureRegion creditPressedTexture = textureAtlas.findRegion("credit");
        ImageButton credit = new ImageButton(new TextureRegionDrawable(new TextureRegion(creditTexture)), new TextureRegionDrawable(new TextureRegion(creditPressedTexture)));
        //credit.setPosition((3 * WORLD_WIDTH / 4 + credit.getWidth() / 3), (3 * WORLD_HEIGHT / 7), Align.center);
        //credit.setPosition((WORLD_WIDTH / 2), (WORLD_HEIGHT / 2 - 2 * credit.getHeight() - 25), Align.center);
        credit.setPosition((WORLD_WIDTH / 2), (WORLD_HEIGHT / 2 - credit.getHeight() - 25), Align.center);
        credit.addListener(new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                cubeGame.setScreen(new CreditScreen(cubeGame));
                //dispose();
            }
        });
        stage.addActor(credit);
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
        System.out.println("Dispose: MainMenu");
        stage.dispose();
        Gdx.input.setInputProcessor(null);
        //cubeGame.dispose();
    }
}
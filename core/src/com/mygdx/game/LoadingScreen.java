package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadingScreen extends ScreenAdapter {
    private static final float WORLD_WIDTH = 1920;
    private static final float WORLD_HEIGHT = 1024;
    private static final float PROGRESS_BAR_WIDTH = 100;
    private static final float PROGRESS_BAR_HEIGHT = 25;
    private static final float PETER_RIVER_RED = 155f / 255;
    private static final float PETER_RIVER_GREEN = 89f / 255;
    private static final float PETER_RIVER_BLUE = 182f / 255;
    private static final float PETER_RIVER_ALPHA = 1;
    private final CubeGame CubeGame;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private float progress;

    LoadingScreen(CubeGame CubeGame) {
        this.CubeGame = CubeGame;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply(true);
        shapeRenderer = new ShapeRenderer();
        progress = 0;

        //CubeGame.getAssetManager().load("music.wav", Music.class);

        CubeGame.getAssetManager().load("coin.wav", Sound.class);
        CubeGame.getAssetManager().load("die.wav", Sound.class);

        CubeGame.getAssetManager().load("pack.pack", TextureAtlas.class);

//        CubeGame.getAssetManager().load("levelTest.tmx", TiledMap.class);

        CubeGame.getAssetManager().load("level1.tmx", TiledMap.class);
        CubeGame.getAssetManager().load("level2.tmx", TiledMap.class);
        CubeGame.getAssetManager().load("level3.tmx", TiledMap.class);
        CubeGame.getAssetManager().load("level4.tmx", TiledMap.class);
        CubeGame.getAssetManager().load("level5.tmx", TiledMap.class);
        CubeGame.getAssetManager().load("level6.tmx", TiledMap.class);
        CubeGame.getAssetManager().load("level7.tmx", TiledMap.class);
        CubeGame.getAssetManager().load("level8.tmx", TiledMap.class);
        CubeGame.getAssetManager().load("level9.tmx", TiledMap.class);
        CubeGame.getAssetManager().load("level10.tmx", TiledMap.class);

        BitmapFontLoader.BitmapFontParameter bitmapFontParameter = new BitmapFontLoader.BitmapFontParameter();
        bitmapFontParameter.atlasName = "pack.pack";
        CubeGame.getAssetManager().load("RobotoLight48.fnt", BitmapFont.class, bitmapFontParameter);
        CubeGame.getAssetManager().load("RobotoBlack40.fnt", BitmapFont.class, bitmapFontParameter);

    }

    @Override
    public void render(float delta) {
        clearScreen();
        draw();
        update();
    }

    private void update() {
        if (CubeGame.getAssetManager().update()) {
            CubeGame.setScreen(new MenuScreen(CubeGame));
            shapeRenderer.dispose();
        } else {
            progress = CubeGame.getAssetManager().getProgress();
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(PETER_RIVER_RED,PETER_RIVER_GREEN,PETER_RIVER_BLUE, PETER_RIVER_ALPHA); //seting a sky like color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(
                WORLD_WIDTH / 2 - PROGRESS_BAR_WIDTH / 2, WORLD_HEIGHT / 2 - PROGRESS_BAR_HEIGHT / 2,
                progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        System.out.println("Dispose: LoadingScreen");
        CubeGame.dispose();
    }
}

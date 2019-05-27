package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.utils.viewport.Viewport;

class LevelSelectScreen extends ScreenAdapter {
    private static final float HALF_WORLD_WIDTH = 960;
    private static final float HALF_WORLD_HEIGHT = 512;
    private static final float FIRST_COL_BUTTON_WIDTH_POSITION = 600;
    private static final float SECOND_COL_BUTTON_WIDTH_POSITION = 300;
    private static final float FIRST_ROW_BUTTON_HEIGHT_POSITION = 100;
    private static final float SECOND_ROW_BUTTON_HEIGHT_POSITION = 400;
    private static final float PETER_RIVER_RED = 155f / 255;
    private static final float PETER_RIVER_GREEN = 89f / 255;
    private static final float PETER_RIVER_BLUE = 182f / 255;
    private static final float PETER_RIVER_ALPHA = 1;
    private final CubeGame cubeGame;
    private final SpriteBatch batch;
    private Stage stage;
    private TextureAtlas textureAtlas;
    private GlyphLayout layout;
    private BitmapFont bitmapFont;
    private Viewport viewport;

    LevelSelectScreen(CubeGame cubeGame, SpriteBatch batch) {
        this.cubeGame = cubeGame;
        this.batch = batch;
    }

    @Override
    public void show() {
        viewport = new FitViewport(HALF_WORLD_WIDTH + HALF_WORLD_WIDTH, HALF_WORLD_HEIGHT + HALF_WORLD_HEIGHT, new OrthographicCamera());
        viewport.apply(true);
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        textureAtlas = cubeGame.getAssetManager().get("pack.pack");
        bitmapFont = cubeGame.getAssetManager().get("RobotoBlack40.fnt");
        layout = new GlyphLayout();
        //lastLevel = cubeGame.getSavedLevel();

        TextureRegion backTexture = textureAtlas.findRegion("back");
        TextureRegion backPressedTexture = textureAtlas.findRegion("backPressed");
        ImageButton back = new ImageButton(new TextureRegionDrawable(new TextureRegion(backTexture)), new TextureRegionDrawable(new TextureRegion(backPressedTexture)));
        back.setPosition(100, HALF_WORLD_HEIGHT + 412, Align.center);
        back.addListener(new ActorGestureListener(){
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                cubeGame.setScreen(new MenuScreen(cubeGame));
            }
        });
        stage.addActor(back);

        buildImageButton(HALF_WORLD_WIDTH - FIRST_COL_BUTTON_WIDTH_POSITION, HALF_WORLD_HEIGHT - FIRST_ROW_BUTTON_HEIGHT_POSITION, 1);
        buildImageButton(HALF_WORLD_WIDTH - FIRST_COL_BUTTON_WIDTH_POSITION, HALF_WORLD_HEIGHT - SECOND_ROW_BUTTON_HEIGHT_POSITION, 6);
        buildImageButton(HALF_WORLD_WIDTH - SECOND_COL_BUTTON_WIDTH_POSITION, HALF_WORLD_HEIGHT - FIRST_ROW_BUTTON_HEIGHT_POSITION, 2);
        buildImageButton(HALF_WORLD_WIDTH - SECOND_COL_BUTTON_WIDTH_POSITION, HALF_WORLD_HEIGHT - SECOND_ROW_BUTTON_HEIGHT_POSITION, 7);
        buildImageButton(HALF_WORLD_WIDTH, HALF_WORLD_HEIGHT - FIRST_ROW_BUTTON_HEIGHT_POSITION, 3);
        buildImageButton(HALF_WORLD_WIDTH, HALF_WORLD_HEIGHT - SECOND_ROW_BUTTON_HEIGHT_POSITION, 8);
        buildImageButton(HALF_WORLD_WIDTH + SECOND_COL_BUTTON_WIDTH_POSITION, HALF_WORLD_HEIGHT - FIRST_ROW_BUTTON_HEIGHT_POSITION, 4);
        buildImageButton(HALF_WORLD_WIDTH + SECOND_COL_BUTTON_WIDTH_POSITION, HALF_WORLD_HEIGHT - SECOND_ROW_BUTTON_HEIGHT_POSITION, 9);
        buildImageButton(HALF_WORLD_WIDTH + FIRST_COL_BUTTON_WIDTH_POSITION, HALF_WORLD_HEIGHT - FIRST_ROW_BUTTON_HEIGHT_POSITION, 5);
        buildImageButton(HALF_WORLD_WIDTH + FIRST_COL_BUTTON_WIDTH_POSITION, HALF_WORLD_HEIGHT - SECOND_ROW_BUTTON_HEIGHT_POSITION, 10);
    }

    private void buildImageButton(float x, float y, final int levelNumber) {
        if (cubeGame.getSavedLevel() >= levelNumber) {
            TextureRegion downTexture = textureAtlas.findRegion("levelBtn");
            TextureRegion upTexture = textureAtlas.findRegion("levelPressedBtn");
            ImageButton imageButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(downTexture)), new TextureRegionDrawable(new TextureRegion(upTexture)));
            //imageButton.setPosition((HALF_WORLD_WIDTH ) / 2, (HALF_WORLD_HEIGHT / 2 + imageButton.getHeight()), Align.center);
            imageButton.setPosition(x, y + imageButton.getHeight(), Align.center);
            imageButton.addListener(new ActorGestureListener(){
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    super.tap(event, x, y, count, button);
                    cubeGame.setScreen(new GameScreen(cubeGame, levelNumber));
                }
            });
            stage.addActor(imageButton);
        } else {
            Image lockImage = new Image(textureAtlas.findRegion("lock"));
            lockImage.setPosition(x, y + lockImage.getHeight(), Align.center);
            stage.addActor(lockImage);
        }
    }

    @Override
    public void render(float delta) {
        clearScreen();
        stage.act(delta);
        stage.draw();
        drawLevelNumbers();
    }

    private void drawLevelNumbers() {
        cubeGame.getBatch().begin();
        for (int i = 1; i <= cubeGame.getSavedLevel(); i++) {
            String number = Integer.toString(i);
            if (i <= 5) {
                layout.setText(bitmapFont, number);
                bitmapFont.draw(
                        cubeGame.getBatch(),
                        number,
                        HALF_WORLD_WIDTH - FIRST_COL_BUTTON_WIDTH_POSITION - layout.width / 2 + ((i - 1) * SECOND_COL_BUTTON_WIDTH_POSITION),
                        HALF_WORLD_HEIGHT + FIRST_ROW_BUTTON_HEIGHT_POSITION + layout.height
                );
            } else {
                layout.setText(bitmapFont, number);
                bitmapFont.draw(
                        cubeGame.getBatch(),
                        number,
                        HALF_WORLD_WIDTH - FIRST_COL_BUTTON_WIDTH_POSITION - layout.width / 2 + ((i - 6) * SECOND_COL_BUTTON_WIDTH_POSITION),
                        HALF_WORLD_HEIGHT - SECOND_ROW_BUTTON_HEIGHT_POSITION / 2 + layout.height
                );
            }
        }
        cubeGame.getBatch().end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(PETER_RIVER_RED,PETER_RIVER_GREEN,PETER_RIVER_BLUE, PETER_RIVER_ALPHA); //seting a sky like color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        System.out.println("Dispose: LevelSelect");
        //stage.dispose();
        //Gdx.input.setInputProcessor(null);
        //cubeGame.dispose();
    }

}

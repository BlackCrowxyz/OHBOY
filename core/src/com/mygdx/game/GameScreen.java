package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen extends ScreenAdapter implements VideoEventListener{
    private static final float WORLD_WIDTH = 1920;
    private static final float WORLD_HEIGHT = 1024;
    private static final float LEVEL_WIDTH = 5760;
    private static final float MENU_BUTTON_GAP = 210;
    private static final float BUTTON_SIZE = 210;
//    private static final float PETER_RIVER_RED = 52f / 255;
//    private static final float PETER_RIVER_GREEN = 152f / 255;
//    private static final float PETER_RIVER_BLUE = 219f / 255;
    private static final float PETER_RIVER_RED = 155f / 255;
    private static final float PETER_RIVER_GREEN = 89f / 255;
    private static final float PETER_RIVER_BLUE = 182f / 255;
    private static final float PETER_RIVER_ALPHA = 1;
    private static final float PLAYER_POSITION_X = 256;
    private static final float PLAYER_POSITION_Y = 448;
    private Vector3 touchPoint;
    private float timer;
    private boolean isStoryWatched;
    private boolean showAd;

    private enum State {GAME_RUNNING, GAME_PAUSED, GAME_OVER, GAME_LEVEL_END,}
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;
    //private ShapeRenderer shapeRenderer;
    private BitmapFont bitmapFont;
    private TextureAtlas textureAtlas;
    private TiledGameMap gameMap;
    private TiledMap tiledMap;
    private CubeGame cubeGame;
    //private Music music;
    private Sound dieSound;
    private Controller controller;
    private int level;
    private int chosenLevel;
    private Player cube;
    private Array<Enemy> enemies;
    private Array<Text> texts;
    private State state;
    private Rectangle resumeBounds;
    private Rectangle restartBounds;
    private Rectangle menuBounds;

    GameScreen(CubeGame cubeGame, int chosenLevel) {
        this.cubeGame = cubeGame;
        this.chosenLevel = chosenLevel;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        //batch = cubeGame.getBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply(true);
        touchPoint = new Vector3();
        //shapeRenderer = new ShapeRenderer();
        controller = new Controller(cubeGame, batch);
        resumeBounds = new Rectangle((WORLD_WIDTH - MENU_BUTTON_GAP + 500) / 2, (WORLD_HEIGHT - MENU_BUTTON_GAP) / 2, BUTTON_SIZE, BUTTON_SIZE);
        restartBounds = new Rectangle((WORLD_WIDTH - MENU_BUTTON_GAP) / 2, (WORLD_HEIGHT - MENU_BUTTON_GAP) / 2, BUTTON_SIZE, BUTTON_SIZE);
        menuBounds = new Rectangle((WORLD_WIDTH - MENU_BUTTON_GAP - 500) / 2, (WORLD_HEIGHT - MENU_BUTTON_GAP) / 2, BUTTON_SIZE, BUTTON_SIZE);
        bitmapFont = cubeGame.getAssetManager().get("RobotoLight48.fnt");
//        music = cubeGame.getAssetManager().get("music.wav");
//        music.play();
//        music.setLooping(true);
        state = State.GAME_RUNNING;
        level = chosenLevel;
        timer = 0;
        showAd = true;
        isStoryWatched = false;
        gameMap = new TiledGameMap(cubeGame, batch, camera, level);
        textureAtlas = cubeGame.getAssetManager().get("pack.pack");
        dieSound = cubeGame.getAssetManager().get("die.wav");
        enemies = new Array<>();
        texts = new Array<>();
        cube = new Player(PLAYER_POSITION_X, PLAYER_POSITION_Y, gameMap, textureAtlas.findRegion("cube_standing"), textureAtlas.findRegion("cube_moving"));
        makeEnemies();
        makeTexts();
    }

    private void makeEnemies() {
        setTiledMap(level);
        Enemy.setMap(gameMap);
        importEnemiesFromMap();
    }

    private void makeTexts() { importTextsFromMap(); }

    private void importEnemiesFromMap() {
        MapLayer mapLayer = tiledMap.getLayers().get("Enemy_Object");
        if (mapLayer != null) {
            for (MapObject object :mapLayer.getObjects()) {
                if ((boolean) object.getProperties().get("isDynamic")) {        //Dynamic Enemy(lava)
                    enemies.add(new Enemy(
                            (float) object.getProperties().get("x"),
                            (float) object.getProperties().get("y"),
                            (boolean) object.getProperties().get("isVertical"),
                            textureAtlas.findRegion("lava"),
                            textureAtlas.findRegion("lava_standing")
                    ));
                } else {                                                        //Static lava
                    enemies.add(new Enemy(
                            (float) object.getProperties().get("x"),
                            (float) object.getProperties().get("y"),
                            (float) object.getProperties().get("width"),
                            (float) object.getProperties().get("height")
                    ));
                }
            }
        }
    }

    private void importTextsFromMap() {
        MapLayer mapLayer = tiledMap.getLayers().get("Text_Object");
        if (mapLayer != null) {
            for (MapObject object :mapLayer.getObjects()) {
                texts.add(new Text(
                        (Float) object.getProperties().get("x"),
                        (Float) object.getProperties().get("y"),
                        (String) object.getProperties().get("text")
                ));
            }
        }
    }

    private void setTiledMap(int level) {
        tiledMap = cubeGame.getAssetManager().get("level" + level + ".tmx", TiledMap.class);
//        tiledMap = cubeGame.getAssetManager().get("levelTest.tmx", TiledMap.class);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        draw();
        //drawDebug();
        androidInputHandler();
        update(delta);
    }

    private void androidInputHandler() {
        cube.setLeft(controller.isLeftPressed());
        cube.setRight(controller.isRightPressed());
        cube.setJump(controller.isUpPressed());
        if (controller.isPausePressed()) {
            controller.setPausePressed(false);
            pause();
        }
    }

    @Override
    public void pause () {
        //TODO: Pause() : check it for android when exiting game
        if (state == State.GAME_RUNNING) state = State.GAME_PAUSED;
    }

    @Override
    public void resume() {
        checkForResume();
    }

    private void draw(){
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        switch (state) {
            case GAME_LEVEL_END:
                break;
            case GAME_RUNNING:
                if (level == 1 && !isStoryWatched) drawStory();
                else drawGameScreen();
                if (!showAd) showAd = true;
                break;
            case GAME_PAUSED:
                drawPauseScreen();
                break;
            case GAME_OVER:
                break;
            default:
                break;
        }
    }

    private void drawStory() {
        timer += Gdx.graphics.getDeltaTime();

        if (Gdx.input.isTouched()) {
            isStoryWatched = true;
            timer = 0;
        }

        if (timer < 20) {
            batch.begin();
            batch.draw(textureAtlas.findRegion("Story"), 0, 0);
            batch.end();
        } else {
            isStoryWatched = true;
            timer = 0;
        }
    }

    private void drawPauseScreen() {
        if (showAd) {
            showAd = false;
            cubeGame.getAdsController().showInterstitialAd(new Runnable() {
                @Override
                public void run() {
                }
            });
        }

//        cubeGame.getAdsController().revokeAppConsent(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("********************* revokeAppConsent(new Runnable())");
//            }
//        });


//        if (oneTimeRun) {
//            oneTimeRun = false;
//            cubeGame.getAdsController().contactMe(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
//        }

        batch.begin();
        batch.draw(textureAtlas.findRegion("Resume"), resumeBounds.x, resumeBounds.y);
        batch.draw(textureAtlas.findRegion("Restart"), restartBounds.x, restartBounds.y);
        batch.draw(textureAtlas.findRegion("Home"), menuBounds.x, menuBounds.y);
        batch.end();
    }

    private void drawGameScreen() {
        gameMap.render();
        batch.begin();
        for (Text text : texts) bitmapFont.draw(batch, text.getLine(), text.getX(), text.getY());
        cube.draw(batch);
        for (Enemy enemy : enemies) enemy.draw(batch);
        batch.end();
//        if (Gdx.app.getType() == Application.ApplicationType.Android)
            controller.draw();
    }

//    private void drawDebug() {
//        shapeRenderer.setProjectionMatrix(camera.projection);
//        shapeRenderer.setTransformMatrix(camera.view);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        for (Enemy enemy : enemies) enemy.shapeRenderer(shapeRenderer);
//        cube.render(shapeRenderer);
//        shapeRenderer.end();
//    }

    private void update(float delta) {
        switch (state) {
            case GAME_OVER:
                doRestart();
                break;
            case GAME_PAUSED:
                resetCamera();
                checkForResume();
                break;
            case GAME_RUNNING:
                updateGameLogic(delta);
                break;
            case GAME_LEVEL_END:
                NextLevel();
                break;
            default:
                break;
        }
    }

    private void checkForResume() {
        camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
        if (Gdx.input.justTouched()) {
            if (resumeBounds.contains(touchPoint.x, touchPoint.y)) {
                state = State.GAME_RUNNING;
                Gdx.input.setInputProcessor(controller.getStage());
            }
            if (restartBounds.contains(touchPoint.x, touchPoint.y)) {
                doRestart();
            }
            if (menuBounds.contains(touchPoint.x, touchPoint.y)) {
                cubeGame.setScreen(new MenuScreen(cubeGame));
                doRestart();
            }
        }
    }

    private void updateGameLogic(float delta) {
        cube.update(delta);
        for (Enemy enemy : enemies) enemy.update(delta);
        setCameraPosition();
        checkForGameOver(delta);
        checkForNextLevel();
    }

    private void checkForGameOver(float delta) {
        //TODO:  expensive operation(badlogic) - my bad!
        timer += delta;
        for (Enemy enemy : enemies)
            if (enemy.GameOver(cube.getPos())) {
                if (timer > 0.1F) {
                    timer = 0;
                    state = State.GAME_OVER;
                }
            }

        if (isCubeGoingOutOfMap()) {
            if (timer > 0.1F) {
                state = State.GAME_OVER;
                timer = 0;
            }
        }
    }

    private void checkForNextLevel() {
        if (gameMap.getNumberOfTilesBeenTakenByUser() == gameMap.getNumberOfCoins()) {
            if (showAd) {
                //TODO: AD SHOW
                cubeGame.getAdsController().showInterstitialAd(() -> {
                    //System.out.println("Interstitial app closed");
                });
                showAd = false;
            }
            state = State.GAME_LEVEL_END;
        }
    }

    private void NextLevel() {
        showAd = true;
        if (level == 10) {
            level++;
            saveLevel();
            cubeGame.setScreen(new LevelSelectScreen(cubeGame, batch));
            return;
        }
        gameMap.doRestart();
        gameMap.nextMap(cubeGame, ++level);
        resetCamera();
        enemiesDoRestart();
        textsDoRestart();
        cube.doRestart();
        state = State.GAME_RUNNING;
        saveLevel();
    }

    private void textsDoRestart() {
        texts.clear();
        makeTexts();
    }

    private void enemiesDoRestart() {
        enemies.clear();
        makeEnemies();
    }

    private void doRestart() {
        dieSound.play();
        resetCamera();
        gameMap.doRestart();
        cube.doRestart();
        state = State.GAME_RUNNING;
    }

    private void resetCamera() {
        camera.position.set(WORLD_WIDTH / 2, camera.position.y, camera.position.z);
        camera.update();
        gameMap.setOrthogonalTiledMapRendererView(camera);
    }

    private void setCameraPosition() {
        if (!(cube.getX() + cube.getWidth() < WORLD_WIDTH / 2 || cube.getX() + cube.getWidth() > LEVEL_WIDTH - WORLD_WIDTH / 2)) {
            camera.position.set(cube.getX() + cube.getWidth(), camera.position.y, camera.position.z);
            camera.update();
            gameMap.setOrthogonalTiledMapRendererView(camera);
        } else if (cube.getX() + cube.getWidth() > LEVEL_WIDTH - WORLD_WIDTH / 2) {
            camera.position.set(LEVEL_WIDTH - WORLD_WIDTH / 2, camera.position.y, camera.position.z);
            camera.update();
            gameMap.setOrthogonalTiledMapRendererView(camera);
        }
    }

    private boolean isCubeGoingOutOfMap() {
        return cube.getY() + cube.getHeight() < -cube.getHeight();
    }

    private void saveLevel() {
        if (level > cubeGame.getSavedLevel())
            cubeGame.setSavedLevel(level);
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(PETER_RIVER_RED,PETER_RIVER_GREEN,PETER_RIVER_BLUE, PETER_RIVER_ALPHA); //seting a sky like color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void onRewardedEvent(String type, int amount) {
        // player has just finished the video and was rewarded
        state = State.GAME_LEVEL_END;
    }

    @Override
    public void onRewardedVideoAdLoadedEvent() {
        // video is ready and can be presented to the player
    }

    @Override
    public void onRewardedVideoAdClosedEvent() {
        // player has closed the video so no reward for him
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        controller.resize(width, height);
    }

    @Override
    public void dispose() {
        System.out.println("Disposed : GameScreen");
        batch.dispose();
        textureAtlas.dispose();
        //shapeRenderer.dispose();
        gameMap.dispose();
        tiledMap.dispose();
        //controller.dispose();
        cube.dispose();
        //music.dispose();
        dieSound.dispose();
        cubeGame.dispose();
    }
}
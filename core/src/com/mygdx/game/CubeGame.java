package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class CubeGame extends Game {

	private final AssetManager assetManager = new AssetManager();
	private Preferences savedLevel;
	private SpriteBatch batch;
	private AdsController adsController;

	public CubeGame(AdsController adsController){
		this.adsController = adsController;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		savedLevel = Gdx.app.getPreferences("CubeGame");
		//if (savedLevel.getInteger("level") == 0) setSavedLevel(1);
		setSavedLevel(10);
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		setScreen(new LoadingScreen(this));
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public int getSavedLevel() {
		return savedLevel.getInteger("level");
	}

	public void setSavedLevel(int level) {
		savedLevel.putInteger("level", level);
		savedLevel.flush();
	}

	public SpriteBatch getBatch() { return batch; }

	public AdsController getAdsController() {
		return adsController;
	}

	@Override
	public void dispose() {
		System.out.println("Dispose(): CubeGame");
		super.dispose();
		assetManager.dispose();
	}
}

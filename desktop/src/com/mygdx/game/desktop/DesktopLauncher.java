package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.CubeGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 960;
		config.height = 512;

//		config.width = 1920;
//		config.height = 1024;

//		config.width = 1280;
//		config.height = 720;

		new LwjglApplication(new CubeGame(), config);
	}
}

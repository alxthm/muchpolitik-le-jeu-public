package com.muchpolitik.lejeu.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.muchpolitik.lejeu.LeJeu;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Graphics.DisplayMode displayMode = LwjglApplicationConfiguration.getDesktopDisplayMode();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = (int) (displayMode.width * 0.9f);
		config.height = (int) (displayMode.height * 0.9f);

		new LwjglApplication(new LeJeu(), config);
	}
}

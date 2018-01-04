package com.muchpolitik.lejeu.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Graphics.DisplayMode displayMode = LwjglApplicationConfiguration.getDesktopDisplayMode();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		// configure graphics (no fullscreen as it has weird behaviour on mac and linux)
		config.width = displayMode.width;
		config.height = displayMode.height;
		config.useHDPI = true;

		// change the small icon at the top left of the window
		config.addIcon("ui/icon/icon-128.png", Files.FileType.Internal);
        config.addIcon("ui/icon/icon-32.png", Files.FileType.Internal);
        config.addIcon("ui/icon/icon-16.png", Files.FileType.Internal);

        // change window name
        config.title = "Much Politik - Le Jeu";

		new LwjglApplication(new LeJeuDesktop(), config);
	}
}

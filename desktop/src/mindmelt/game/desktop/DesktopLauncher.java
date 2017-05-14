package mindmelt.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import mindmelt.game.MindmeltGDX;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.foregroundFPS = 0;//60; // Setting to 0 disables foreground fps throttling
		config.backgroundFPS = 0; // Setting to 0 disables background fps throttling
		config.vSyncEnabled = false; // Setting to false disables vertical sync
		config.useGL30 = true;
		config.width = 640;
		config.height = 480;
		config.fullscreen = false;
		new LwjglApplication(new MindmeltGDX(), config);
	}
}

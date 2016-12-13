package se.doverfelt.pixturation.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import se.doverfelt.pixturation.Pixturation;

public class DesktopLauncher {
	public static void main (String[] arg) {
		/*LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		Graphics.DisplayMode temp = LwjglApplicationConfiguration.getDesktopDisplayMode();
		for (Graphics.DisplayMode d : LwjglApplicationConfiguration.getDisplayModes()) {
			System.out.println(d.toString());
			if ((d.refreshRate > temp.refreshRate && d.height >= temp.height) || d.height > temp.height) {
				temp = d;
			}
		}
		config.setFromDisplayMode(temp);
		System.out.println(("DisplayMode: " + temp));*/
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.fullscreen = false;
		config.resizable = false;
		//TODO Add icons in 128x 32x 16x res
		//config.addIcon("logo32.png", Files.FileType.Internal);
		new LwjglApplication(new Pixturation(), config);
	}
}

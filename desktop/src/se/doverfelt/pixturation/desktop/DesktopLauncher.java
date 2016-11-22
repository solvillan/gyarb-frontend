package se.doverfelt.pixturation.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import se.doverfelt.pixturation.Pixturation;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		Graphics.DisplayMode temp = LwjglApplicationConfiguration.getDesktopDisplayMode();
		for (Graphics.DisplayMode d : LwjglApplicationConfiguration.getDisplayModes()) {
			System.out.println(d.toString());
			if ((d.refreshRate > temp.refreshRate && d.height >= temp.height) || d.height > temp.height) {
				temp = d;
			}
		}
		config.setFromDisplayMode(temp);
		System.out.println(("DisplayMode: " + temp));
		new LwjglApplication(new Pixturation(), config);
	}
}

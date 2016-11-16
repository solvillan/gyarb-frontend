package se.doverfelt.pixturation;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import se.doverfelt.pixturation.scenes.LoadingScene;
import se.doverfelt.pixturation.scenes.Screen;

import java.util.HashMap;

public class Pixturation extends Game {

    HashMap<String, Screen> screens = new HashMap<String, Screen>();
	
	@Override
	public void create () {
        screens.put("loading", new LoadingScene());
        setScreen("loading");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (getScreen() instanceof Screen && getScreen() != null) {
            ((Screen) getScreen()).update(Gdx.graphics.getDeltaTime());
            getScreen().render(Gdx.graphics.getDeltaTime());
        } else if (getScreen() != null) {
            super.render();
        }
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}

    public void setScreen(String screen) {
        super.setScreen(screens.get(screen));
    }
}

//Server: gyarb.database.windows.net,1433 \r\nSQL Database: gyarb\r\nUser Name: solvillan\r\n\r\nPHP Data Objects(PDO) Sample Code:\r\n\r\ntry {\r\n   $conn = new PDO ( \"sqlsrv:server = tcp:gyarb.database.windows.net,1433; Database = gyarb\", \"solvillan\", \"{your_password_here}\");\r\n    $conn->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION );\r\n}\r\ncatch ( PDOException $e ) {\r\n   print( \"Error connecting to SQL Server.\" );\r\n   die(print_r($e));\r\n}\r\n\rSQL Server Extension Sample Code:\r\n\r\n$connectionInfo = array(\"UID\" => \"solvillan@gyarb\", \"pwd\" => \"{your_password_here}\", \"Database\" => \"gyarb\", \"LoginTimeout\" => 30, \"Encrypt\" => 1, \"TrustServerCertificate\" => 0);\r\n$serverName = \"tcp:gyarb.database.windows.net,1433\";\r\n$conn = sqlsrv_connect($serverName, $connectionInfo);

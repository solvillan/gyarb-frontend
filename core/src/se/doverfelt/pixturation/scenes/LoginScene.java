package se.doverfelt.pixturation.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Base64Coder;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import se.doverfelt.pixturation.Pixturation;
import se.doverfelt.pixturation.utils.HttpUtils;
import se.doverfelt.pixturation.net.LoginHandler;

import java.util.HashMap;

/**
 * Created by rickard on 2016-11-17.
 */
public class LoginScene extends AbstractScene {

    private String email = "", pass = "";
    private Pixturation pixturation;

    @LmlActor("email")
    private TextField emailField;

    @LmlActor("loginWindow")
    private Window window;

    @LmlActor("errorDialog")
    private Dialog errorDialog;

    @LmlActor("errorMessage")
    private Label errorMsg;

    private LoginHandler handler;
    private boolean added;

    /**
     * @param stage will be filled with actors when the view is passed to a LML parser. Should not be null.
     */
    public LoginScene(Stage stage) {
        super(stage);

    }

    @LmlAction("login")
    public void login(Button button) {
        Gdx.app.log("LoginButton", "Pressed!");
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("email", this.email);
        data.put("password", this.pass);
        HttpUtils.post("user/auth", data, handler);
    }

    @LmlAction("updateEmail")
    public void updateEmail(TextField field) {
        this.email = field.getText();
    }

    @LmlAction("updatePass")
    public void updatePass(TextField field) {
        this.pass = field.getText();
    }

    @Override
    public void create(Pixturation pixturation) {
        this.pixturation = pixturation;
        handler = new LoginHandler(pixturation);
    }

    @Override
    public FileHandle getTemplateFile() {
        return Gdx.files.internal("views/login.xml");
    }

    @Override
    public String getViewId() {
        return "login";
    }

    @Override
    public void update(float delta) {
        if (pixturation.getPreferences().contains("email") && !added && emailField != null) {
            emailField.setText(pixturation.getPreferences().getString("email"));
            added = true;
        }
    }

    @Override
    public void show() {
        super.show();
        if (pixturation.getPreferences().contains("token")) {
            if (handler.checkOldToken(pixturation.getPreferences().getString("token"))) {
                HttpUtils.setToken(pixturation.getPreferences().getString("token"));
                Gdx.app.log("Token", Base64Coder.decodeString(HttpUtils.getToken()));
                handler.authToken();
            }
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void hide() {

    }

    public void error(String msg) {
        errorMsg.setText(msg);
        errorDialog.setVisible(true);
        errorDialog.setZIndex(500);
        errorDialog.pack();
        errorDialog.setPosition((getStage().getWidth() / 2f) - (errorDialog.getWidth() / 2f), (getStage().getHeight() / 2f) - (errorDialog.getHeight() / 2f));
        window.setVisible(false);
    }

    @LmlAction("closeError")
    public boolean closeError(Object object) {
        errorDialog.setVisible(false);
        window.setVisible(true);
        return true;
    }
}

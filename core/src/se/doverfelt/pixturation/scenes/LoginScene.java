package se.doverfelt.pixturation.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import se.doverfelt.pixturation.Pixturation;
import se.doverfelt.pixturation.net.HttpUtils;
import se.doverfelt.pixturation.net.LoginHandler;

import java.util.HashMap;

/**
 * Created by rickard on 2016-11-17.
 */
public class LoginScene extends AbstractScene {

    private String email = "", pass = "";
    private Pixturation pixturation;

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
        HttpUtils.post("user/auth", data, new LoginHandler(pixturation));
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
    }

    @Override
    public FileHandle getTemplateFile() {
        return Gdx.files.internal("views/login.lml");
    }

    @Override
    public String getViewId() {
        return "login";
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(getStage());
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

}
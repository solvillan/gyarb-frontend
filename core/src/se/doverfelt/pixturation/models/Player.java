package se.doverfelt.pixturation.models;

/**
 * Created by Rickard on 2016-11-22.
 */
public class Player {

    private final String name;
    private final String email;

    public Player(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

package se.doverfelt.pixturation.models;

/**
 * Created by Rickard on 2016-11-22.
 */
public class Player {

    private final String name;
    private final String email;
    private final int id;

    public Player(String name, String email, int id) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Player && ((Player) obj).getId() == this.id;
    }

    public int getId() {
        return id;
    }
}

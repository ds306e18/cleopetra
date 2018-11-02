package dk.aau.cs.ds306e18.tournament.ui;

public class Stage {
    private int id;
    private String name;

    public Stage(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
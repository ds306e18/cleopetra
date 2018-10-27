package dk.aau.cs.ds306e18.tournament.model;

public class Stage {

    private String name;
    private Format format;
    private int numberOfTeamsWanted = 16;

    public Stage(String name, Format format) {
        this.name = name;
        this.format = format;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        if (this.format.getStatus() != StageStatus.PENDING) throw new IllegalStateException("Stage has already started.");
        this.format = format;
    }

    public int getNumberOfTeamsWanted() {
        return numberOfTeamsWanted;
    }

    public void setNumberOfTeamsWanted(int numberOfTeamsWanted) {
        if (this.format.getStatus() != StageStatus.PENDING) throw new IllegalStateException("Stage has already started.");
        this.numberOfTeamsWanted = numberOfTeamsWanted;
    }
}

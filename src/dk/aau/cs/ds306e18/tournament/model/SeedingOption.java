package dk.aau.cs.ds306e18.tournament.model;

public enum SeedingOption {
    SEED_BY_ORDER("By order in list"),
    NO_SEEDING("No seeding"),
    RANDOM_SEEDING("Random seeding");

    private String optionText;

    SeedingOption(String optionText) {
        this.optionText = optionText;
    }

    @Override
    public String toString() {
        return optionText;
    }
}

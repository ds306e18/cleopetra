package dk.aau.cs.ds306e18.tournament.ui;

import dk.aau.cs.ds306e18.tournament.model.format.*;

public enum StageFormatOption {
    SINGLE_ELIMINATION("Single Elimination", SingleEliminationFormat.class),
    DOUBLE_ELIMINATION("Double Elimination", DoubleEliminationFormat.class),
    SWISS_SYSTEM("Swiss", SwissFormat.class),
    ROUND_ROBIN("Round Robin", RoundRobinFormat.class);

    private final String name;
    private final Class<?> clazz;

    StageFormatOption(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return name;
    }

    public Format getNewInstance() {
        try {
            return (Format) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static StageFormatOption getOption(Format format) {
        for (StageFormatOption option : values()) {
            if (option.clazz.isInstance(format)) {
                return option;
            }
        }
        return null;
    }
}

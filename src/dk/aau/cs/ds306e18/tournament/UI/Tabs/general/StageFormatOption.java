package dk.aau.cs.ds306e18.tournament.UI.Tabs.general;

import dk.aau.cs.ds306e18.tournament.model.Format;
import dk.aau.cs.ds306e18.tournament.model.RoundRobinStage;
import dk.aau.cs.ds306e18.tournament.model.SingleEliminationStage;
import dk.aau.cs.ds306e18.tournament.model.SwissStage;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public enum StageFormatOption {
    SINGLE_ELIMINATION("Single Elimination", SingleEliminationStage.class),
    SWISS_SYSTEM("Swiss", SwissStage.class),
    ROUND_ROBIN("Round Robin", RoundRobinStage.class);

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

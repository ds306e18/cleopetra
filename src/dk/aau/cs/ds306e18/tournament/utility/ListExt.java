package dk.aau.cs.ds306e18.tournament.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ListExt {

    /**
     * Returns a list containing n elements of the supplied object.
     */
    public static <T> List<T> nOf(int n, Supplier<T> generator) {
        return Stream.generate(generator).limit(n).collect(Collectors.toList());
    }
}

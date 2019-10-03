package dk.aau.cs.ds306e18.tournament.model.format;

import dk.aau.cs.ds306e18.tournament.model.Team;
import dk.aau.cs.ds306e18.tournament.utility.PowMath;

import java.util.ArrayList;
import java.util.List;

public class Seeding {

    /** Generates a sequence of integers (from 0 to n - 1) that follows this pattern: [0, 7, 3, 4, 1, 6, 2, 5].
     * n must be a power of two. */
    public static List<Integer> generateFairOrder(int n) {
        if (Integer.bitCount(n) != 1)
            throw new IllegalArgumentException("n must be a power of two.");

        List<Integer> ints = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            ints.set(i, i);
        }
        return fairSeedList(ints);
    }

    /** Generates a sequence of integers (from 0 to 2^n - 1) that follows this pattern: [0, 7, 3, 4, 1, 6, 2, 5].
     * The size of the list will be 2^n. */
    public static List<Integer> generateFairOrderPow(int n) {
        return generateFairOrder((int) Math.pow(2, n));
    }

    /** Returns a new list that contains the same as the given list but the elements has been reordered.
     * [1, 2, 3, 4] becomes [1, 4, 2, 3]. And [1, 2, 3, 4, 5, 6, 7, 8] becomes [1, 8, 4, 5, 2, 7, 3, 6].
     * @param list a list, which size is a power of two. */
    public static <T> List<T> fairSeedList(List<T> list) {
        if (!PowMath.isPowOf2(list.size())) throw new AssertionError("Size of list (" + list.size() + ") is not a power of two.");

        if (Integer.bitCount(list.size()) != 1)
            throw new IllegalArgumentException("The size of the list must be a power of two.");

        list = new ArrayList<>(list);

        int slice = 1;
        int halfSize = list.size() / 2;

        while (slice < halfSize) {
            List<T> temp = new ArrayList<>(list);
            list.clear();

            while (temp.size() > 0) {
                int lastIndex = temp.size();
                list.addAll(temp.subList(0, slice));
                list.addAll(temp.subList(lastIndex - slice, lastIndex));
                temp.removeAll(temp.subList(lastIndex - slice, lastIndex));
                temp.removeAll(temp.subList(0, slice));
            }

            slice *= 2;
        }

        return list;
    }

    /**
     * Takes a list of elements and returns a new list with the same elements, but rearranged so by alternately picking
     * the first element, and then the last element, until a new list is constructed. I.e. [1, 2, 3, 4] becomes
     * [1, 4, 2, 3], and [1, 2, 3, 4, 5, 6, 7] becomes [1, 7, 2, 6, 3, 5, 4].
     * @param list a list of any size
     */
    public static <T> List<T> simplePairwiseSeedList(List<T> list) {
        int n = list.size();
        int pairs = n / 2;
        int i = 0;

        List<T> newList = new ArrayList<>();

        while (i < pairs) {
            newList.add(list.get(i));
            newList.add(list.get(n - i - 1));
            i++;
        }

        // In case list size is odd, add middle element
        if (n % 2 == 1) {
            newList.add(list.get(n / 2));
        }

        return newList;
    }
}

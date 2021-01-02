package dk.aau.cs.ds306e18.tournament.model.format;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SeedingTest {

    @Test
    public void fairSeedList01() {

        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        List<Integer> seededList = Seeding.fairSeedList(list);

        assertEquals(1, (int) seededList.get(0));
        assertEquals(4, (int) seededList.get(1));
        assertEquals(2, (int) seededList.get(2));
        assertEquals(3, (int) seededList.get(3));
    }

    @Test
    public void fairSeedList02() {

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> seededList = Seeding.fairSeedList(list);

        assertEquals(1, (int) seededList.get(0));
        assertEquals(8, (int) seededList.get(1));
        assertEquals(4, (int) seededList.get(2));
        assertEquals(5, (int) seededList.get(3));
        assertEquals(2, (int) seededList.get(4));
        assertEquals(7, (int) seededList.get(5));
        assertEquals(3, (int) seededList.get(6));
        assertEquals(6, (int) seededList.get(7));
    }

    @Test
    public void simplePairwiseSeedList01() {

        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        List<Integer> pairwiseList = Seeding.simplePairwiseSeedList(list);

        assertEquals(1, (int) pairwiseList.get(0));
        assertEquals(4, (int) pairwiseList.get(1));
        assertEquals(2, (int) pairwiseList.get(2));
        assertEquals(3, (int) pairwiseList.get(3));
    }

    @Test
    public void simplePairwiseSeedList02() {

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> pairwiseList = Seeding.simplePairwiseSeedList(list);

        assertEquals(1, (int) pairwiseList.get(0));
        assertEquals(9, (int) pairwiseList.get(1));
        assertEquals(2, (int) pairwiseList.get(2));
        assertEquals(8, (int) pairwiseList.get(3));
        assertEquals(3, (int) pairwiseList.get(4));
        assertEquals(7, (int) pairwiseList.get(5));
        assertEquals(4, (int) pairwiseList.get(6));
        assertEquals(6, (int) pairwiseList.get(7));
        assertEquals(5, (int) pairwiseList.get(8));
    }
}
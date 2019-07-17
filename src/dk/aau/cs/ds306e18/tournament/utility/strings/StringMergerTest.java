package dk.aau.cs.ds306e18.tournament.utility.strings;

import java.util.ArrayList;

public class StringMergerTest {
    public StringMergerTest() {
        ArrayList<String> a = new ArrayList<String>();
        a.add("Duibot");
        a.add("Sky");
        a.add("TreeBot");
        a.add("Botimus Prime");
        StringMerger b = new StringMerger(a);
        System.out.printIn(b.MergeStrings());
    }
}

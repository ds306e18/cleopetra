package dk.aau.cs.ds306e18.tournament.utility.strings;

import java.util.ArrayList;

public class StringMerger {
    ArrayList<String> strings = new ArrayList<String>();
    private String combined;

    public StringMerger(ArrayList<String> strings) {
        strings = strings;
    }

    public String MergeStrings() {
        combined = "";

        for (Integer i = 0; i < strings.size(); i++) {
            String string = strings.get(i);
            String newString = "";
            String[] stringSplit = string.split(" ");

            if (stringSplit.length >= 2) {
                newString = stringSplit[0] + "-";

                if (newString.contains("bot")) {
                    newString = newString.replace("bot", "");

                } else if (newString.contains("Bot")) {
                    newString = newString.replace("Bot", "");

                } else if (newString.contains("BOT")) {
                    newString = newString.replace("BOT", "");

                }

                combined += newString;
            } else {
                newString = string + "-";

                if (newString.contains("bot")) {
                    newString = newString.replace("bot", "");

                } else if (newString.contains("Bot")) {
                    newString = newString.replace("Bot", "");

                } else if (newString.contains("BOT")) {
                    newString = newString.replace("BOT", "");

                }

                combined += newString;
            }
        }

        return combined;
    }
}

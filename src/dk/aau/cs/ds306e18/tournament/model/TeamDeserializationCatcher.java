package dk.aau.cs.ds306e18.tournament.model;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamDeserializationCatcher implements TypeAdapterFactory {

    public static List<Team> teams;

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getType().equals(new TypeToken<ArrayList<Team>>(){}.getType())) {
            final TypeAdapter<T> delegateList = gson.getDelegateAdapter(this, type);
            final TypeAdapter<Team> deletageTeam = gson.getDelegateAdapter(this, new TypeToken<Team>(){});
            return new TypeAdapter<T>() {
                @Override
                public void write(JsonWriter out, T value) throws IOException {
                    // Serialize to an array
                    String s = gson.toJson(value);
                    out.jsonValue(s);
                }

                @Override
                public T read(JsonReader in) throws IOException {
                    // Re-create the arraylist
                    ArrayList<Team> teams = new ArrayList<>();
                    in.beginArray();
                    while (in.peek().equals(JsonToken.BEGIN_OBJECT)) {
                        teams.add(deletageTeam.read(in));
                    }
                    in.endArray();
                    // Storing the arraylist as a public static list allows other to find teams based on their id
                    TeamDeserializationCatcher.teams = teams;
                    return (T) teams;
                }
            };
        }
        return null;
    }
}

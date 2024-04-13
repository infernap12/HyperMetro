package metro;

import com.google.gson.*;


import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class MetroSystemJsonDeserialiser implements JsonDeserializer<MetroSystem> {
    @Override
    public MetroSystem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Iterator<Map.Entry<String, JsonElement>> iterator = json.getAsJsonObject().entrySet().iterator(); //String line.getName(), JsonElement lineMap


        //map that represents the system
        Map<String, MetroLine> metroMap = new HashMap<>(); //String line.getName(), MetroLine line
        while (iterator.hasNext()) {// let's do it functional bro style
            Map.Entry<String, JsonElement> metroEntry = iterator.next();
            MetroLine metroLine = new MetroLine(metroEntry.getKey());

            TreeMap<Integer, String> map = new TreeMap<>(
                    metroEntry.getValue().getAsJsonObject().entrySet().stream()
                            .collect(
                                    Collectors.toMap(
                                            x -> Integer.parseInt(x.getKey()),
                                            x -> x.getValue().getAsString()
                                    )
                            )
            );

            map.forEach((key, value) -> metroLine.addLast(value));
            metroMap.put(metroLine.getName(), metroLine);
        }
        return new MetroSystem(metroMap);
    }
}

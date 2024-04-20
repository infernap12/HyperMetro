package metro;

import com.google.gson.*;


import java.lang.reflect.Type;
import java.util.*;

public class MetroSystemJsonDeserialiser implements JsonDeserializer<MetroSystem> {
    @Override
    public MetroSystem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Iterator<Map.Entry<String, JsonElement>> iterator = json.getAsJsonObject().entrySet().iterator(); //String line.getName(), JsonElement lineMap

        Set<Command> commandList = new HashSet<>();
        //map that represents the system
        Map<String, MetroLine> metroMap = new HashMap<>(); //String line.getName(), MetroLine line
        //iterate top level "metro lines"
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> metroEntry = iterator.next();
            String lineName = metroEntry.getKey();
            MetroLine metroLine = new MetroLine(lineName);

            TreeMap<Integer, String> nameMap = new TreeMap<>();
            Map<Integer, Integer> timeMap = new HashMap<>();
            //let's iterate the stations, shall we?
            for (Map.Entry<String, JsonElement> e : metroEntry.getValue().getAsJsonObject().entrySet()) {
                int order = Integer.parseInt(e.getKey());
                JsonObject station = e.getValue().getAsJsonObject();
                String stationName = station.getAsJsonPrimitive("name").getAsString();
                int time;
                if (station.has("time") && !station.get("time").isJsonNull()) {
                    time = station.getAsJsonPrimitive("time").getAsInt();
                } else {
                    time = 0;
                }
                JsonArray transfers = station.getAsJsonArray("transfer");
                if (!transfers.isEmpty()) {
                    for (JsonElement jsonElement : transfers) {
                        JsonObject transfer = jsonElement.getAsJsonObject();
                        String transferLine = transfer.getAsJsonPrimitive("line").getAsString();
                        String transferStation = transfer.getAsJsonPrimitive("station").getAsString();
                        commandList.add(new Command(List.of("Connect", lineName, stationName, transferLine, transferStation)));
                    }
                }

                nameMap.put(order, stationName);
                timeMap.put(order, time);
            }

            nameMap.forEach((key, value) -> metroLine.addLast(value, timeMap.get(key)));
            metroMap.put(metroLine.getName(), metroLine);
        }
        MetroSystem metroSystem = new MetroSystem(metroMap);
        commandList.forEach(metroSystem::invoke);
        return metroSystem;
    }
}

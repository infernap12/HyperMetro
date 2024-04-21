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

            List<String> stationNames = new ArrayList<>();
            Map<String, Integer> timeMap = new HashMap<>();
            Map<String, String[]> nextMap = new HashMap<>();
            Map<String, String[]> prevMap = new HashMap<>();
            //let's iterate the stations, shall we?
            for (JsonElement jsonStation : metroEntry.getValue().getAsJsonArray().asList()) {
                JsonObject station = jsonStation.getAsJsonObject();
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
                JsonArray nextElement = station.getAsJsonArray("next");
                if (!nextElement.isEmpty()) {
                    nextMap.put(stationName, nextElement.asList().stream().map(JsonElement::getAsString).toArray(String[]::new));
                }
                JsonArray prevElement = station.getAsJsonArray("prev");
                if (!prevElement.isEmpty()) {
                    prevMap.put(stationName, prevElement.asList().stream().map(JsonElement::getAsString).toArray(String[]::new));
                }

                stationNames.add(stationName);
                timeMap.put(stationName, time);
            }

            stationNames.forEach(x -> {

            });

            //stationNames.forEach((name) -> metroLine.addLast(name, timeMap.get(name)));
            MetroLine metroLine = new MetroLine(lineName, stationNames, timeMap, nextMap, prevMap);
            metroMap.put(metroLine.getName(), metroLine);
        }
        MetroSystem metroSystem = new MetroSystem(metroMap);
        commandList.forEach(metroSystem::invoke);
        return metroSystem;
    }
}

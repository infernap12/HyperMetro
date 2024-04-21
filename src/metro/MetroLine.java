package metro;

import javax.naming.Name;
import java.util.*;

public class MetroLine {
    //    private static final Station depot = new Station(null, "Depot", null);
//    private Station head = null; // just peak the head and tail
//    private Station tail = null;
    private final String name;
    private final LinkedList<Station> stations = new LinkedList<>();

    public MetroLine(String lineName, List<String> stationNames, Map<String, Integer> timeMap, Map<String, String[]> nextMap, Map<String, String[]> prevMap) {
        this.name = lineName;
        stationNames.forEach(x -> stations.add(new Station(timeMap.get(x), x)));
        Iterator<Station> stationIterator = stations.iterator();
        while (stationIterator.hasNext()) {
            Station station = stationIterator.next();
            if (nextMap.get(station.name) != null) {
                Arrays.stream(nextMap.get(station.name)).map(this::getByName).forEach(station.next::add);
            }
            if (prevMap.get(station.name) != null) {
                Arrays.stream(prevMap.get(station.name)).map(this::getByName).forEach(station.prev::add);
            }
        }
    }

    public LinkedList<Station> getStations() {
        return stations;
    }

    public MetroLine(String lineName) {
        this.name = lineName;
    }

    public void addLast(String stationName, int time) {
        Station previous = stations.peekLast();
        Station newStation = new Station(time, previous, stationName, null);
        if (previous != null) {
            previous.next.add(newStation);
        }
        stations.addLast(newStation);
    }


    public void addHead(String stationName, int time) {
        Station next = stations.peekFirst();
        Station newStation = new Station(time, null, stationName, stations.peekFirst());
        if (next != null) {
            next.prev.add(newStation);
        }
        stations.addFirst(newStation);
    }

    public Station getByName(String stationName) {
        Station station = null;
        for (Station next : stations) {
            if (next.name.equals(stationName)) {
                station = next;
                break;
            }
        }
        if (station == null) {
            throw new IllegalArgumentException("Station does not exist");
        } else {
            return station;
        }
    }

    public void remove(String stationName) throws IllegalArgumentException {
        Station station;
        station = getByName(stationName);
        remove(station);
    }

    public void remove(Station thisStation) {
        for (Station previousStation : thisStation.prev) {
            previousStation.next.remove(thisStation);
            previousStation.next.addAll(thisStation.next);
        }
        for (Station nextStation : thisStation.next) {
            nextStation.prev.remove(thisStation);
            nextStation.prev.addAll(thisStation.prev);
        }
    }

    public String getName() {
        return name;
    }

    public void output() {
        System.out.println("depot");
        for (Station next : this.stations) {
//            String previousName = next.prev == null ? "depot" : next.prev.name;
//            String currentName = next.name;
//            String nextName = next.next == null ? "depot" : next.next.name;
//            System.out.printf("%s - %s - %s%n", previousName, currentName, nextName);
            System.out.println(next.outputString());

        }
        System.out.println("depot");
    }

}

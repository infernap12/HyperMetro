package metro;

import java.util.*;

public class MetroLine {
    //    private static final Station depot = new Station(null, "Depot", null);
//    private Station head = null; // just peak the head and tail
//    private Station tail = null;
    private final String name;
    private final LinkedList<Station> stations;

    public LinkedList<Station> getStations() {
        return stations;
    }

    public MetroLine(String lineName) {
        this.name = lineName;
        stations = new LinkedList<>();
    }

    public void addLast(String stationName) {
        Station previous = stations.peekLast();
        Station newStation = new Station(previous, stationName, null);
        if (previous != null) {
            previous.next = newStation;
        }
        stations.addLast(newStation);
    }


    public void addHead(String stationName) {
        Station next = stations.peekFirst();
        Station newStation = new Station(null, stationName, stations.peekFirst());
        if (next != null) {
            next.prev = newStation;
        }
        stations.addFirst(newStation);
    }

    public Station getByName(String stationName) {
        for (Station next : stations) {
            if (next.name.equals(stationName)) {
                return next;
            }
        }
        return null;
    }

    private void remove(Station station) {
        Station previous = station.prev;
        Station next = station.next;
        previous.next = next;
        next.prev = previous;
        stations.remove(station);
    }

    public String getName() {
        return name;
    }

    public void output() {
        for (Station next : this.stations) {
            String previousName = next.prev == null ? "depot" : next.prev.name;
            String currentName = next.name;
            String nextName = next.next == null ? "depot" : next.next.name;
            System.out.printf("%s - %s - %s%n", previousName, currentName, nextName);

        }
    }

}
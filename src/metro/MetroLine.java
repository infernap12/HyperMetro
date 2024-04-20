package metro;

import javax.naming.Name;
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

    public void addLast(String stationName, int time) {
        Station previous = stations.peekLast();
        Station newStation = new Station(time, previous, stationName, null);
        if (previous != null) {
            previous.next = newStation;
        }
        stations.addLast(newStation);
    }


    public void addHead(String stationName, int time) {
        Station next = stations.peekFirst();
        Station newStation = new Station(time, null, stationName, stations.peekFirst());
        if (next != null) {
            next.prev = newStation;
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

    public void remove(Station station) {
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

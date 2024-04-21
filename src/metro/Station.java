package metro;

import java.util.HashSet;
import java.util.Set;

public class Station {
    public static final int TRANSFER_TIME = 5;
    String name;
    int time;
    Set<Station> prev = new HashSet<>();
    Set<Station> next = new HashSet<>();
    private final Set<EdgeTransfer> edgeTransfers = new HashSet<>();

    Station(int time, String name) {
        this.name = name;
        this.time = time;
    }

    Station(int time, Station prev, String name, Station next) {
        this.name = name;
        this.time = time;
        this.next.add(next);
        this.prev.add(prev);

    }

    public Set<EdgeTransfer> getTransfers() {
        return edgeTransfers;
    }

    public void connect(MetroLine line, Station station) {
        edgeTransfers.add(new EdgeTransfer(line, station));
    }

    public record EdgeTransfer(MetroLine metroLine, Station station) {
    }

    public String outputString() {
        StringBuilder sb = new StringBuilder(name);
        if (!edgeTransfers.isEmpty()) {
            for (EdgeTransfer edgeTransfer : edgeTransfers) {
                sb.append(" - %s (%s)".formatted(edgeTransfer.station.name, edgeTransfer.metroLine.getName()));
            }
        }
        return sb.toString();
    }
}

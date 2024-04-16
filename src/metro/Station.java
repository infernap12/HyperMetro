package metro;

import java.util.HashSet;
import java.util.Set;

public class Station {
    String name;
    Station prev;
    Station next;
    private final Set<Transfer> transfers = new HashSet<>();

    Station(Station prev, String name, Station next) {
        this.name = name;
        this.next = next;
        this.prev = prev;
    }

    public Set<Transfer> getTransfers() {
        return transfers;
    }

    public void connect(MetroLine line, Station station) {
        transfers.add(new Transfer(line, station));
    }

    public record Transfer(MetroLine metroLine, Station station) {
    }

    public String outputString() {
        StringBuilder sb = new StringBuilder(name);
        if (!transfers.isEmpty()) {
            for (Transfer transfer : transfers) {
                sb.append(" - %s (%s)".formatted(transfer.station.name, transfer.metroLine.getName()));
            }
        }
        return sb.toString();
    }
}

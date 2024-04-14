package metro;

import java.util.Map;

public class MetroSystem {
    private Map<String, MetroLine> lines;

    public MetroSystem(Map<String, MetroLine> lines) {
        this.lines = lines;
    }

    public Map<String, MetroLine> getLines() {
        return lines;
    }


    public void invoke(Command command) {
        switch (command.type) {
            case APPEND -> {
                MetroLine line = lines.get(command.lineName);
                line.addLast(command.stationName);
            }
            case EXIT -> {
            }
            case OUTPUT -> {
                lines.get(command.lineName).output();
            }
            case ADD_HEAD -> {
                lines.get(command.lineName).addHead(command.stationName);
            }
            case REMOVE -> {
                MetroLine line = lines.get(command.lineName);
                line.remove(command.stationName);
            }
        }
    }
}

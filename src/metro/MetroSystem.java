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
            case ADD -> {
            }
            case OUTPUT -> {
                lines.get(command.lineName).output();
            }
            case EXIT -> {
            }
            case ADD_HEAD -> {
            }
        }
    }
}

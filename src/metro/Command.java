package metro;

import java.util.List;

public class Command {
    CommandType type;
    String lineName;
    String stationName;

    public Command(List<String> tokens) {
        CommandType type = CommandType.get(tokens.get(0));
        this.type = type;
        switch (type) {
            case EXIT:
                break;
            case ADD:
            case ADD_HEAD:
                this.stationName = tokens.get(2);
            case OUTPUT:
                this.lineName = tokens.get(1);
        }
    }

    enum CommandType {
        ADD,
        ADD_HEAD,
        EXIT,
        OUTPUT;

        public static CommandType get(String group) {
            return CommandType.valueOf(group.toUpperCase().replaceAll(" |-", "_").replaceAll("/", ""));
        }
    }
}

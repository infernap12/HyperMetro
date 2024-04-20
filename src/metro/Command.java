package metro;

import java.util.List;
import java.util.Objects;

public class Command {
    CommandType type;
    String lineName;
    String stationName;
    String transferLine;
    String transferStation;
    int time;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;  // Check for reference equality.
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;  // Check for null and ensure exact class match.
        }

        Command command = (Command) obj;

        // Regular checks that apply to all types.
        boolean regularChecks = type == command.type &&
                                Objects.equals(lineName, command.lineName) &&
                                Objects.equals(stationName, command.stationName) &&
                                Objects.equals(transferLine, command.transferLine) &&
                                Objects.equals(transferStation, command.transferStation);

        if (type == CommandType.CONNECT) {
            // Additional check for the CONNECT type with swapped values.
            boolean swappedChecks = Objects.equals(lineName, command.transferLine) &&
                                    Objects.equals(stationName, command.transferStation) &&
                                    Objects.equals(transferLine, command.lineName) &&
                                    Objects.equals(transferStation, command.stationName);

            return regularChecks || swappedChecks;
        }

        return regularChecks;
    }

    @Override
    public int hashCode() {
        if (type == CommandType.CONNECT) {
            // Use the unordered pair logic for CONNECT type
            int pair1Hash = Objects.hash(lineName, transferLine) + Objects.hash(transferLine, lineName);
            int pair2Hash = Objects.hash(stationName, transferStation) + Objects.hash(transferStation, stationName);
            return Objects.hash(type, pair1Hash, pair2Hash);
        } else {
            // Standard hash code calculation for other types
            return Objects.hash(type, lineName, stationName, transferLine, transferStation);
        }
    }


    public Command(List<String> tokens) {
        CommandType type = CommandType.get(tokens.get(0));
        this.type = type;
        switch (type) {
            case CONNECT, ROUTE, FASTEST_ROUTE -> {
                transferLine = tokens.get(3);
                transferStation = tokens.get(4);
                this.stationName = tokens.get(2);
                this.lineName = tokens.get(1);
            }

            case ADD -> {
                this.stationName = tokens.get(2);
                this.lineName = tokens.get(1);
                this.time = Integer.parseInt(tokens.get(3));
            }

            case REMOVE, APPEND, ADD_HEAD -> {
                this.stationName = tokens.get(2);
                this.lineName = tokens.get(1);
            }

            case OUTPUT -> this.lineName = tokens.get(1);

        }
    }

    enum CommandType {
        ADD_HEAD,
        ADD,
        APPEND,
        CONNECT,
        EXIT,
        OUTPUT,
        REMOVE,
        ROUTE,
        FASTEST_ROUTE;

        public static CommandType get(String group) {
            return CommandType.valueOf(group.toUpperCase().replaceAll(" |-", "_").replaceAll("/", ""));
        }
    }
}

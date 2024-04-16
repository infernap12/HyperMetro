package metro;

import java.util.*;

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
            case CONNECT -> {
                MetroLine line = lines.get(command.lineName);
                Station byName = line.getByName(command.stationName);
                MetroLine transferLine = lines.get(command.transferLine);
                Station transferStation = transferLine.getByName(command.transferStation);
                byName.connect(transferLine, transferStation);
                transferStation.connect(line, byName);
            }
            case ROUTE -> {
                MetroLine rootLine = lines.get(command.lineName);
                Station rootStation = rootLine.getByName(command.stationName);
                MetroLine destinationLine = lines.get(command.transferLine);
                Station destinationStation = destinationLine.getByName(command.transferStation);
                List<RouteNode> bfs = bfs(rootLine, rootStation, destinationLine, destinationStation);
                printRoute(bfs);
            }
        }
    }

    private void printRoute(List<RouteNode> route) {
        for (RouteNode routeNode : route) {
            if (routeNode.isTransfer()) {
                System.out.println("Transition to line " + routeNode.line.getName());
            }
            System.out.println(routeNode.station.name);
        }
    }

    private List<RouteNode> bfs(MetroLine rootLine, Station rootStation, MetroLine destinationLine, Station destinationStation) {
        Deque<Station> que = new ArrayDeque<>();
        HashSet<Station> visited = new HashSet<>();
        HashMap<Station, RouteNode> nodeMap = new HashMap<>();

        visited.add(rootStation);
        que.add(rootStation);
        nodeMap.put(rootStation, new RouteNode(rootStation, null, rootLine, false));


        while (!que.isEmpty()) {
            Station currentStation = que.pop();
            RouteNode currentNode = nodeMap.get(currentStation);

            //if we hit destination, build the path
            if (currentStation.equals(destinationStation)) {
                RouteNode node = currentNode;
                List<RouteNode> list = new ArrayList<>();
                while (node != null) {
                    list.add(node);
                    node = nodeMap.get(node.parent);
                }
                Collections.reverse(list);
                return list;
            }

            for (Station.Transfer transfer : currentStation.getTransfers()) {
                Station transferStation = transfer.station();
                MetroLine transferLine = transfer.metroLine();
                if (!visited.contains(transferStation)) {
                    visited.add(transferStation);
                    que.add(transferStation);
                    nodeMap.put(transferStation, new RouteNode(
                            transferStation,
                            currentStation,
                            transferLine,
                            true
                    ));
                }
            }

            for (Station adjacentStation : Arrays.asList(currentStation.next, currentStation.prev)) {
                if (adjacentStation != null && !visited.contains(adjacentStation)) {
                    visited.add(adjacentStation);
                    que.add(adjacentStation);
                    nodeMap.put(adjacentStation, new RouteNode(
                            adjacentStation,
                            currentStation,
                            currentNode.line,
                            false
                    ));
                }
            }

        }
        throw new RuntimeException("No path found.");
    }

    public record RouteNode(Station station, Station parent, MetroLine line, boolean isTransfer) {

    }
}


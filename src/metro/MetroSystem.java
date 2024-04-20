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
                line.addLast(command.stationName, 0);
            }
            case EXIT -> {
            }
            case OUTPUT -> {
                lines.get(command.lineName).output();
            }
            case ADD_HEAD -> {
                lines.get(command.lineName).addHead(command.stationName, 0);
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
            case ROUTE, FASTEST_ROUTE -> {
                MetroLine rootLine = lines.get(command.lineName);
                Station rootStation = rootLine.getByName(command.stationName);
                MetroLine destinationLine = lines.get(command.transferLine);
                Station destinationStation = destinationLine.getByName(command.transferStation);
                List<RouteNode> nodeList = command.type == Command.CommandType.ROUTE ? bfs(rootLine, rootStation, destinationLine, destinationStation) : dijkstra(rootLine, rootStation, destinationLine, destinationStation);
                printRoute(nodeList);
                if (command.type == Command.CommandType.FASTEST_ROUTE) {
                    int routeTime = nodeList.get(nodeList.size() - 1).distance();//get the last nodes distance

                    System.out.printf("Total: %d minutes in the way%n", routeTime);
                }
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

            for (Station.EdgeTransfer edgeTransfer : currentStation.getTransfers()) {
                Station transferStation = edgeTransfer.station();
                MetroLine transferLine = edgeTransfer.metroLine();
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

    private List<RouteNode> dijkstra(MetroLine rootLine, Station rootStation, MetroLine destinationLine, Station destinationStation) {
        PriorityQueue<RouteNode> que = new PriorityQueue<>();
        que.add(new RouteNode(rootStation, null, rootLine, false, 0));
        HashSet<Station> processedStations = new HashSet<>();
        HashMap<Station, RouteNode> nodeMap = new HashMap<>();
        lines.values().forEach(metroLine -> metroLine.getStations().forEach(station -> nodeMap.put(station, new RouteNode(station, null, metroLine, false, Integer.MAX_VALUE))));

        while (!que.isEmpty()) {
            RouteNode currentNode = que.poll();
            Station currentStation = currentNode.station;
            MetroLine currentLine = currentNode.line;
            int currentDistanceFromStart = currentNode.distance();

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

            if (!processedStations.contains(currentStation)) {
                //do transfers
                for (Station.EdgeTransfer edgeTransfer : currentStation.getTransfers()) {
                    Station transferStation = edgeTransfer.station();
                    MetroLine transferLine = edgeTransfer.metroLine();
                    if (!processedStations.contains(transferStation)) {
                        int storedNodeDistance = nodeMap.get(transferStation).distance;
                        int newDistance = currentDistanceFromStart + Station.TRANSFER_TIME;
                        if (newDistance < storedNodeDistance) {
                            RouteNode node = new RouteNode(transferStation, currentStation, transferLine, true, newDistance);
                            que.add(node);
                            nodeMap.put(transferStation, node);
                        }
                    }
                }
                // do next/ previous
                if (currentStation.next != null && !processedStations.contains(currentStation.next)) {
                    int storedNodeDistance = nodeMap.get(currentStation.next).distance;
                    int newDistance = currentDistanceFromStart + currentStation.time;
                    if (newDistance < storedNodeDistance) {
                        RouteNode node = new RouteNode(currentStation.next, currentStation, currentLine, false, newDistance);
                        que.add(node);
                        nodeMap.put(currentStation.next, node);
                    }
                }
                if (currentStation.prev != null && !processedStations.contains(currentStation.prev)) {
                    int storedNodeDistance = nodeMap.get(currentStation.prev).distance;
                    int newDistance = currentDistanceFromStart + currentStation.prev.time;
                    if (newDistance < storedNodeDistance) {
                        RouteNode node = new RouteNode(currentStation.prev, currentStation, currentLine, false, newDistance);
                        que.add(node);
                        nodeMap.put(currentStation.prev, node);
                    }
                }
                //mark processed
                processedStations.add(currentStation);
            } else {
                continue;
            }
        }
        throw new RuntimeException("No path found.");
    }

    public record RouteNode(Station station, Station parent, MetroLine line, boolean isTransfer, int distance) implements Comparable<RouteNode>{
        public RouteNode(Station station, Station parent, MetroLine line, boolean isTransfer) {
            this(station, parent, line, isTransfer, 0);
        }

        @Override
        public int compareTo(RouteNode routeNode) {
            return Integer.compare(this.distance, routeNode.distance);
        }
    }
}


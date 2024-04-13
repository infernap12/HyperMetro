package metro;

public class Station {
    String name;
    Station prev;
    Station next;

    Station(Station prev, String name, Station next) {
        this.name = name;
        this.next = next;
        this.prev = prev;
    }
}

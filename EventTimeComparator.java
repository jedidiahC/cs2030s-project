package cs2030.simulator;

import java.util.Comparator;

class EventTimeComparator implements Comparator<Event> { 
    public int compare(Event e1, Event e2) {
        return new Double(e1.getTime()).compareTo(new Double(e2.getTime()));
    }
}

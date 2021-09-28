package cs2030.simulator;

import java.util.Comparator;

class EventTimeComparator implements Comparator<Event> { 
    public int compare(Event e1, Event e2) {
        int timeComparison = Double.valueOf(e1.getTime()).compareTo(e2.getTime());

        if (timeComparison != 0) {
            return timeComparison;
        }

        return Integer.valueOf(e1.getEventPriority()).compareTo(e2.getEventPriority());
    }
}

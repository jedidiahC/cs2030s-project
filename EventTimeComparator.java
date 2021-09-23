package cs2030.simulator;

import java.util.Comparator;

class EventTimeComparator implements Comparator<Event> { 
    public int compare(Event e1, Event e2) {
        int timeComparison = new Double(e1.getTime()).compareTo(new Double(e2.getTime()));

        if (timeComparison != 0) {
            return timeComparison;
        }

        return new Integer(e1.getEventPriority()).compareTo(new Integer(e2.getEventPriority()));
    }
}

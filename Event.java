package cs2030.simulator;

import java.util.Optional;

abstract class Event { 
    private static final int DEFAULT_EVENT_PRIORITY = 100;

    private final double time;

    public abstract String toString();

    abstract SimulatorState process(SimulatorState simulatorState);
    
    abstract Optional<Event> nextEvent(SimulatorState simulatorState);

    public Event(double time) {
        this.time = time;
    }

    double getTime() {
        return this.time;
    }

    int getEventPriority() {
        return DEFAULT_EVENT_PRIORITY;
    }

    SimulatorStats updateStats(SimulatorState state, SimulatorStats stats) {
        return stats;
    }
}

package cs2030.simulator;

abstract class Event { 
    private final double time;

    public abstract String toString();

    abstract SimulatorState process(SimulatorState simulatorState);
    
    abstract Event nextEvent(SimulatorState simulatorState);

    public Event(double time) {
        this.time = time;
    }

    double getTime() {
        return this.time;
    }

    boolean hasNextEvent() {
        return true;
    }

    int getEventPriority() {
        return 1;
    }

    SimulatorStats updateSimulatorStats(SimulatorState state, SimulatorStats stats) {
        return stats;
    }
}

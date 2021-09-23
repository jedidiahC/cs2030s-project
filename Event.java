package cs2030.simulator;

abstract class Event { 
    private final double time;

    public abstract String toString();

    abstract EventResult process(SimulatorState simulatorState);

    public Event(double time) {
        this.time = time;
    }

    double getTime() {
        return this.time;
    }

    boolean hasFollowupEvent() {
        return true;
    }

    int getEventPriority() {
        return 1;
    }
}

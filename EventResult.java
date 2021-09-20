package cs2030.simulator;

class EventResult {
    private final Event followupEvent;
    private final SimulatorState simulatorState;

    EventResult(Event followupEvent, SimulatorState simulatorState) {
        this.followupEvent = followupEvent;
        this.simulatorState = simulatorState;
    }

    Event getFollowup() {
       return followupEvent; 
    }

    SimulatorState getSimulatorState() {
        return simulatorState;
    }
}

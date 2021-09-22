
package cs2030.simulator;

class LeaveEvent extends Event { 
    private final Customer customer;

    LeaveEvent(double time, Customer customer) {
        super(time);
        this.customer = customer;
    }
    
    @Override
    public EventResult process(SimulatorState simulatorState) {
        return new EventResult(this, simulatorState);
    }

    @Override
    public String toString() {
        return String.format("%.3f %s leaves", getTime(), customer);
    }

    @Override
    boolean hasFollowupEvent() {
        return false;
    }
}

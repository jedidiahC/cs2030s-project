
package cs2030.simulator;

class LeaveEvent extends Event { 
    private final Customer customer;

    LeaveEvent(double time, Customer customer) {
        super(time);
        this.customer = customer;
    }
    
    @Override
    SimulatorState process(SimulatorState simulatorState) {
        return simulatorState;
    }
    
    @Override
    Event nextEvent(SimulatorState simulatorState) {
        return this;
    }

    @Override
    SimulatorStats updateStats(SimulatorState state, SimulatorStats stats) {
        return stats.trackCustomerLeft();        
    }

    @Override
    public String toString() {
        return String.format("%.3f %s leaves", getTime(), customer);
    }

    @Override
    boolean hasNextEvent() {
        return false;
    }
}

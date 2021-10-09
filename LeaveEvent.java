
package cs2030.simulator;

class LeaveEvent extends CustomerEvent { 
    LeaveEvent(double time, Customer customer) {
        super(time, customer);
    }

    @Override
    SimulatorStats updateStats(SimulatorState state, SimulatorStats stats) {
        return stats.trackCustomerLeft();        
    }

    @Override
    public String toString() {
        return String.format("%s leaves", super.toString());
    }
}

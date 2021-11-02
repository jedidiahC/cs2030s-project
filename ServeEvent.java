package cs2030.simulator;

import java.util.Optional;

class ServeEvent extends CustomerAssignedEvent { 
    private static final int EVENT_PRIORITY = 2;

    ServeEvent(double time, Customer customer, int server) {
        super(time, customer, server);
    }

    @Override 
    SimulatorState process(SimulatorState state) {
        Server server = state
            .getServer(this.getServerAssigned())
            .queueCustomer(this.getCustomer(), this.getTime());

        return state.updateServer(server);
    }

    @Override
    Optional<Event> nextEvent(SimulatorState state) {
        return Optional.<Event>of(
                new DoneEvent(getCompletionTime(), this.getCustomer(), this.getServerAssigned())
                );
    }

    double getCompletionTime() {
        return this.getTime() + this.getCustomer().getServiceTime();
    }

    @Override
    public String toString() {
        return String.format("%s serves by server %s", super.toString(), getServerAssigned());
    }

    @Override
    SimulatorStats updateStats(SimulatorState state, SimulatorStats stats) {
        return stats.trackCustomerServed();        
    }

    @Override
    int getEventPriority() {
        return EVENT_PRIORITY;
    }
}

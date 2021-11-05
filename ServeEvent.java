package cs2030.simulator;

import java.util.Optional;

class ServeEvent extends CustomerAssignedEvent { 
    private static final int EVENT_PRIORITY = 2;

    ServeEvent(double time, Customer customer, Server server) {
        super(time, customer, server);
    }

    @Override 
    SimulatorState process(SimulatorState state) {
        Server server = this.retrieveServer(state)
            .serveCustomer(this.getCustomer(), this.getTime());

        return state.updateServer(server);
    }

    @Override
    Optional<Event> nextEvent(SimulatorState state) {
        return Optional.<Event>of(
                new DoneEvent(getCompletionTime(), this.getCustomer(), this.getServer())
                );
    }

    double getCompletionTime() {
        return this.getTime() + this.getCustomer().getServiceTime();
    }

    @Override
    public String toString() {
        return String.format("%s serves by %s", super.toString(), this.getServer());
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

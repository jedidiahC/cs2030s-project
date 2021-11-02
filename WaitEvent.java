package cs2030.simulator;

import java.util.Optional;

class WaitEvent extends CustomerAssignedEvent { 
    WaitEvent(double time, Customer customer, int server) {
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
        Server server = state.getServer(this.getServerAssigned());
        return Optional.<Event>of(
                new ShouldServeEvent(
                    server.estimateServeTime(this.getCustomer()), 
                    this.getCustomer(), 
                    this.getServerAssigned()
                    )
                );
    }

    @Override
    SimulatorStats updateStats(SimulatorState state, SimulatorStats stats) {
        Server server = state.getServer(this.getServerAssigned());
        return stats.trackWaitingTime(server.estimateServeTime(this.getCustomer()) - this.getTime());        
    }

    @Override
    public String toString() {
        return String.format("%s waits at server %s", super.toString(), this.getServerAssigned());
    }
}

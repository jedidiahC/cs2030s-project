
package cs2030.simulator;

import java.util.Optional;

class ShouldServeEvent extends CustomerAssignedEvent { 
    private static final int EVENT_PRIORITY = 2;

    ShouldServeEvent(double time, Customer customer, int server) {
        super(time, customer, server);
    }

    @Override SimulatorState process(SimulatorState state) {
        return state;
    }

    @Override
    Optional<Event> nextEvent(SimulatorState state) {
        return Optional.<Event>of(pickEvent(state));
    }

    Event pickEvent(SimulatorState state) {
        Server server = state.getServer(this.getServerAssigned());

        if (server.isResting(this.getTime())) {
            // Extend waiting time.
            return new ShouldServeEvent(
                    server.estimateServeTime(this.getCustomer()),
                    this.getCustomer(),
                    server.getServerId()
                    );
        } else {
            return new ServeEvent(this.getTime(), this.getCustomer(), server.getServerId());
        }
    }

    @Override
    SimulatorStats updateStats(SimulatorState state, SimulatorStats stats) {
        Server server = state.getServer(this.getServerAssigned());
        if (server.isResting(this.getTime())) {
            return stats.trackWaitingTime(server.estimateServeTime(this.getCustomer()) - this.getTime());        
        } else {
            return stats;
        }
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    int getEventPriority() {
        return EVENT_PRIORITY;
    }
}

package cs2030.simulator;

import java.util.Optional;
import java.util.function.Function;

class DoneEvent extends CustomerAssignedEvent { 
    private final Function<Server, Optional<Event>> scheduleNextCustomer;

    DoneEvent(double time, Customer customer, Server server, 
           Function<Server, Optional<Event>> scheduleNextCustomer) {
        super(time, customer, server);
        this.scheduleNextCustomer = scheduleNextCustomer;
    }
    
    @Override
    SimulatorState process(SimulatorState state) {
        Server server = retrieveServer(state).completeService(this.getTime(), this.getCustomer());
        return state.updateServer(server);
    }

    @Override
    Optional<Event> nextEvent(SimulatorState state) {
        return scheduleNextCustomer.apply(retrieveServer(state));
    }

    @Override
    public String toString() {
        return String.format("%s done serving by %s", 
                super.toString(), 
                this.getServer());
    }
}

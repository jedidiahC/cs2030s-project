package cs2030.simulator;

import java.util.Optional;
import java.util.function.BiFunction;

class DoneEvent extends CustomerAssignedEvent { 
    private final BiFunction<Server, Double, Optional<Event>> getNextShouldServe;

    DoneEvent(double time, Customer customer, Server server, 
           BiFunction<Server, Double, Optional<Event>> getNextShouldServe) {
        super(time, customer, server);
        this.getNextShouldServe = getNextShouldServe;
    }
    
    @Override
    SimulatorState process(SimulatorState state) {
        Server server = retrieveServer(state).completeService(this.getTime(), this.getCustomer());
        return state.updateServer(server);
    }

    @Override
    Optional<Event> nextEvent(SimulatorState state) {
        return getNextShouldServe.apply(retrieveServer(state), getTime());
    }

    @Override
    public String toString() {
        return String.format("%s done serving by %s", 
                super.toString(), 
                this.getServer());
    }
}

package cs2030.simulator;

import java.util.Optional;
import java.util.function.BiFunction;

class ServeEvent extends CustomerAssignedEvent { 
    private final BiFunction<Server, Double, Optional<Event>> getNextShouldServe;

    ServeEvent(double time, Customer customer, Server server, 
            BiFunction<Server, Double, Optional<Event>> getNextShouldServe) {
        super(time, customer, server);
        this.getNextShouldServe = getNextShouldServe;
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
                new DoneEvent(getCompletionTime(), this.getCustomer(), this.getServer(),
                    this.getNextShouldServe)
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
        return stats
            .trackWaitingTime(this.getTime() - this.getCustomer().getArrivalTime())
            .trackCustomerServed();        
    }
}

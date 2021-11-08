
package cs2030.simulator;

import java.util.Optional;
import java.util.function.BiFunction;

class ShouldServeEvent extends CustomerAssignedEvent { 
    ShouldServeEvent(double time, Customer customer, Server server) {
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
        Server server = this.retrieveServer(state);

        Customer customer = this.getCustomer();

        BiFunction<Server, Double, Optional<Event>> getNextShouldServe = (s, time) -> {
            return server.nextInQueue().map(c -> new ShouldServeEvent(time, c, s));
        };

        if (server.canServeNow(this.getTime(), customer)) {
            return new ServeEvent(this.getTime(), customer, server, getNextShouldServe);
        }             

        // Extend waiting time if resting.
        return new ShouldServeEvent(
            server.estimateServeTime(customer),
            customer,
            server
            );
    }

    @Override
    public String toString() {
        return "";
    }
}

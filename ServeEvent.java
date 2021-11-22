package cs2030.simulator;

import java.util.Optional;
import java.util.function.Function;

class ServeEvent extends CustomerAssignedEvent { 
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
        Function<Server, Optional<Event>> scheduleNextCustomer = s -> {
            return s.nextInQueue().map(c -> new ServeEvent(s.estimateServeTime(c), c, s));
        };

        return Optional.<Event>of(
                new DoneEvent(getCompletionTime(), this.getCustomer(), this.getServer(),
                    scheduleNextCustomer)
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

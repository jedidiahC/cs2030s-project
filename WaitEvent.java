package cs2030.simulator;

import java.util.Optional;

class WaitEvent extends CustomerAssignedEvent { 
    WaitEvent(double time, Customer customer, Server server) {
        super(time, customer, server);
    }

    @Override
    SimulatorState process(SimulatorState state) {
        Server server = this.retrieveServer(state)
            .queueCustomer(this.getCustomer(), this.getTime()); 

        return state.updateServer(server);
    }

    @Override
    public String toString() {
        return String.format("%s waits at %s", super.toString(), this.getServer());
    }
}

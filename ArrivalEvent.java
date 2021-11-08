package cs2030.simulator;

import java.util.ArrayList;
import java.util.Optional;

class ArrivalEvent extends CustomerEvent { 
    ArrivalEvent(double time, Customer customer) {
        super(time, customer);
    }

    @Override 
    Optional<Event> nextEvent(SimulatorState state) {
        return Optional.<Event>of(pickNextEvent(state));
    }

    Event pickNextEvent(SimulatorState state) {
        int serverId = state.assignServer(this.getTime(), this.getCustomer());

        if (!state.hasServer(serverId)) {
            return new LeaveEvent(this.getTime(), this.getCustomer());
        } 

        Server server = state.getServer(serverId);  
        
        if (server.canServeNow(this.getTime(), this.getCustomer())) {
            return new ShouldServeEvent(this.getTime(), this.getCustomer(), server);
        } else if (server.canQueue(this.getTime(),this.getCustomer())) {
            return new WaitEvent(this.getTime(), this.getCustomer(), server);
        } else {
            return new LeaveEvent(this.getTime(), this.getCustomer());
        }
    }

    @Override
    public String toString() {
        return String.format("%s arrives", super.toString());
    }
}


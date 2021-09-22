package cs2030.simulator;

import java.util.ArrayList;

class ArrivalEvent extends Event { 
    private final Customer customer;

    ArrivalEvent(double time, Customer customer) {
        super(time);
        this.customer = customer;
    }
    
    @Override
    EventResult process(SimulatorState simulatorState) {
        ArrayList<Server> servers = simulatorState.getServers();
        Event followupEvent; 

        for (Server server : servers) {
            if (server.canServe(this.customer.getCustomerId())) {
                followupEvent = new ServeEvent(this.getTime(), this.customer, server);
                return new EventResult(followupEvent, simulatorState);
            } else if (server.canQueue()) {
                followupEvent = new WaitEvent(this.getTime(), this.customer, server);
                return new EventResult(followupEvent, simulatorState);
            } 
        }

        followupEvent = new LeaveEvent(this.getTime(), this.customer);
        return new EventResult(followupEvent, simulatorState);
    }

    @Override
    public String toString() {
        return String.format("%.3f %s arrives", getTime(), customer);
    }
}


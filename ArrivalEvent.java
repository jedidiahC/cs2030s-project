package cs2030.simulator;

import java.util.ArrayList;

class ArrivalEvent extends Event { 
    private static final int EVENT_PRIORITY = 3;
    private final Customer customer;

    ArrivalEvent(double time, Customer customer) {
        super(time);
        this.customer = customer;
    }
    
    @Override
    SimulatorState process(SimulatorState simulatorState) {
        return simulatorState;
    }

    @Override 
    Event nextEvent(SimulatorState simulatorState) {
        ArrayList<Server> servers = simulatorState.getServers();

        for (Server server : servers) {
            if (server.canServe(this.customer.getCustomerId())) {
                return new ServeEvent(this.getTime(), this.customer, server);
            }
        }

        for (Server server : servers) {
            if (server.canQueue()) {
                return new WaitEvent(this.getTime(), this.customer, server);
            } 
        }

        return new LeaveEvent(this.getTime(), this.customer);
    }

    @Override
    public String toString() {
        return String.format("%.3f %s arrives", getTime(), customer);
    }

    @Override
    int getEventPriority() {
        return EVENT_PRIORITY;
    }
}


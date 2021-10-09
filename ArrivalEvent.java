package cs2030.simulator;

import java.util.ArrayList;

class ArrivalEvent extends CustomerEvent { 
    private static final int EVENT_PRIORITY = 3;

    ArrivalEvent(double time, Customer customer) {
        super(time, customer);
    }

    @Override 
    boolean hasNextEvent() {
        return true;
    }

    @Override 
    Event nextEvent(SimulatorState simulatorState) {
        int serverId = simulatorState.assignServer(this.getCustomer());

        if (!simulatorState.hasServer(serverId)) {
            return new LeaveEvent(this.getTime(), this.getCustomer());
        } 

        Server server = simulatorState.getServer(serverId);  

        if (server.canServe(this.getCustomer())) {
            return new ServeEvent(this.getTime(), this.getCustomer(), serverId);
        }

        if (server.canQueue(this.getCustomer())) {
            return new WaitEvent(this.getTime(), this.getCustomer(), serverId);
        } 

        return new LeaveEvent(this.getTime(), this.getCustomer());
    }

    @Override
    public String toString() {
        return String.format("%s arrives", super.toString());
    }

    @Override
    int getEventPriority() {
        return EVENT_PRIORITY;
    }
}


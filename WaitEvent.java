package cs2030.simulator;

class WaitEvent extends Event { 
    private final Customer customer;
    private final Server server;

    WaitEvent(double time, Customer customer, Server server) {
        super(time);
        this.customer = customer;
        this.server = server;
    }
    
    @Override
    SimulatorState process(SimulatorState simulatorState) {
        Server server = simulatorState
            .getUpdatedServer(this.server)
            .queueCustomer(customer.getCustomerId()); 

        return simulatorState.updateServer(server);
    }

    @Override 
    Event nextEvent(SimulatorState simulatorState) {
        Server server = simulatorState
            .getUpdatedServer(this.server)
            .queueCustomer(customer.getCustomerId()); 

        return new ServeEvent(server.getNextServeTime(), this.customer, server);
    }

    @Override
    SimulatorStats updateStats(SimulatorState state, SimulatorStats stats) {
        Server server = state.getUpdatedServer(this.server);
        return stats.trackWaitingTime(server.getNextServeTime() - this.getTime());        
    }

    @Override
    public String toString() {
        return String.format("%.3f %s waits at server %s", getTime(), customer, server);
    }
}

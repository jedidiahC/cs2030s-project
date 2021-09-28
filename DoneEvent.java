package cs2030.simulator;

class DoneEvent extends Event { 
    private final Customer customer;
    private final Server server;

    DoneEvent(double time, Customer customer, Server server) {
        super(time);
        this.customer = customer;
        this.server = server;
    }
    
    @Override
    SimulatorState process(SimulatorState simulatorState) {
        Server updatedServer = simulatorState
            .getUpdatedServer(this.server)
            .completeService(this.customer.getCustomerId());

        return simulatorState.updateServer(updatedServer);
    }

    @Override
    Event nextEvent(SimulatorState state) {
        return this;
    }

    @Override
    public String toString() {
        return String.format("%.3f %s done serving by server %s", getTime(), customer, server);
    }

    @Override
    boolean hasNextEvent() {
        return false;
    }
}

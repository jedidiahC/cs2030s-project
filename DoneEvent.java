
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
    public EventResult process(SimulatorState simulatorState) {
        Server updatedServer = simulatorState
            .getUpdatedServer(this.server)
            .completeService(this.customer.getCustomerId());
        simulatorState = simulatorState.updateServer(updatedServer);

        return new EventResult(this, simulatorState);
    }

    @Override
    public String toString() {
        return String.format("%.3f %s done serving by server %s", getTime(), customer, server);
    }

    @Override
    boolean hasFollowupEvent() {
        return false;
    }
}

package cs2030.simulator;

class ServeEvent extends Event { 
    private static final int EVENT_PRIORITY = 2;
    private final Customer customer;
    private final Server server;

    ServeEvent(double time, Customer customer, Server server) {
        super(time);
        this.customer = customer;
        this.server = server;
    }

    @Override
    SimulatorState process(SimulatorState simulatorState) {
        Server server = simulatorState
            .getUpdatedServer(this.server)
            .serveCustomer(this.customer.getCustomerId(), getCompletionTime());

        return simulatorState.updateServer(server);
    }

    @Override
    Event nextEvent(SimulatorState simulatorState) {
        return new DoneEvent(getCompletionTime(), this.customer, this.server);
    }

    double getCompletionTime() {
        return this.getTime() + this.customer.getServiceTime();
    }

    @Override
    public String toString() {
        return String.format("%.3f %s serves by server %s", getTime(), customer, server);
    }

    @Override
    SimulatorStats updateStats(SimulatorState state, SimulatorStats stats) {
        return stats.trackCustomerServed();        
    }

    @Override
    int getEventPriority() {
        return EVENT_PRIORITY;
    }
}

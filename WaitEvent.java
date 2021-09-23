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
    public EventResult process(SimulatorState simulatorState) {
        Server server = simulatorState
            .getUpdatedServer(this.server)
            .queueCustomer(customer.getCustomerId()); 

        Event followupEvent = new ServeEvent(server.getNextServeTime(), this.customer, server);

        simulatorState = simulatorState.updateServer(server);

        return new EventResult(followupEvent, simulatorState);
    }

    @Override
    public String toString() {
        return String.format("%.3f %s waits at server %s", getTime(), customer, server);
    }
}

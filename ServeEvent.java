package cs2030.simulator;

class ServeEvent extends Event { 
    private final Customer customer;
    private final Server server;

    ServeEvent(double time, Customer customer, Server server) {
        super(time);
        this.customer = customer;
        this.server = server;
    }
    
    @Override
    public EventResult process(SimulatorState simulatorState) {
        double completionTime = this.getTime() + customer.getServiceTime();

        Server server = this.server.serveCustomer(this.customer.getCustomerId(), completionTime);
        simulatorState = simulatorState.updateServer(server);
        Event followupEvent = new DoneEvent(completionTime, this.customer, this.server);

        return new EventResult(followupEvent, simulatorState);
    }

    @Override
    public String toString() {
        return String.format("%.3f %s serves by server %s", getTime(), customer, server);
    }

    @Override
    int getEventPriority() {
        return 2;
    }
}

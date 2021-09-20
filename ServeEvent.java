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
        return new EventResult(new ServeEvent(this.getTime(), null, null), simulatorState);
    }

    @Override
    public String toString() {
        return String.format("%.3f ", getTime() );
    }
}

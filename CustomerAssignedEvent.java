package cs2030.simulator;

class CustomerAssignedEvent extends CustomerEvent { 
    private final Server server;

    CustomerAssignedEvent(double time, Customer customer, Server server) {
        super(time, customer);
        this.server = server;
    }

    protected Server retrieveServer(SimulatorState state) {
        return state.getServer(this.server.getServerId());
    }

    protected Server getServer() {
        return this.server;
    }
}

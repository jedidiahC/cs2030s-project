package cs2030.simulator;

class DoneEvent extends CustomerAssignedEvent { 
    DoneEvent(double time, Customer customer, Server server) {
        super(time, customer, server);
    }
    
    @Override
    SimulatorState process(SimulatorState state) {
        Server server = retrieveServer(state).completeService(this.getTime(), this.getCustomer());
        return state.updateServer(server);
    }

    @Override
    public String toString() {
        return String.format("%s done serving by %s", 
                super.toString(), 
                this.getServer());
    }
}

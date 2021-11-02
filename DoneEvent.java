package cs2030.simulator;

class DoneEvent extends CustomerAssignedEvent { 
    DoneEvent(double time, Customer customer, int server) {
        super(time, customer, server);
    }
    
    @Override
    SimulatorState process(SimulatorState simulatorState) {
        Server updatedServer = simulatorState
            .getServer(this.getServerAssigned())
            .completeService(this.getTime(), this.getCustomer());

        return simulatorState.updateServer(updatedServer);
    }

    @Override
    public String toString() {
        return String.format("%s done serving by server %s", 
                super.toString(), 
                this.getServerAssigned());
    }
}

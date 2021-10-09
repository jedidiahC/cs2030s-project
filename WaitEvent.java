package cs2030.simulator;

class WaitEvent extends CustomerAssignedEvent { 
    WaitEvent(double time, Customer customer, int server) {
        super(time, customer, server);
    }
    
    @Override
    SimulatorState process(SimulatorState simulatorState) {
        Server server = simulatorState
            .getServer(this.getServerAssigned())
            .queueCustomer(this.getCustomer(), this.getTime()); 

        return simulatorState.updateServer(server);
    }

    @Override boolean hasNextEvent() {
        return true;
    }

    @Override 
    Event nextEvent(SimulatorState simulatorState) {
        Server server = simulatorState.getServer(this.getServerAssigned());
        return new ServeEvent(server.getNextServeTime(), 
                this.getCustomer(), 
                this.getServerAssigned());
    }

    @Override
    SimulatorStats updateStats(SimulatorState state, SimulatorStats stats) {
        Server server = state.getServer(this.getServerAssigned());
        return stats.trackWaitingTime(server.getNextServeTime() - this.getTime());        
    }

    @Override
    public String toString() {
        return String.format("%s waits at server %s", super.toString(), this.getServerAssigned());
    }
}

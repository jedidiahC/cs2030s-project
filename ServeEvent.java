package cs2030.simulator;

class ServeEvent extends CustomerAssignedEvent { 
    private static final int EVENT_PRIORITY = 2;

    ServeEvent(double time, Customer customer, int server) {
        super(time, customer, server);
    }

    @Override SimulatorState process(SimulatorState simulatorState) {
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
        return new DoneEvent(getCompletionTime(), this.getCustomer(), this.getServerAssigned());
    }

    double getCompletionTime() {
        return this.getTime() + this.getCustomer().getServiceTime();
    }

    @Override
    public String toString() {
        return String.format("%s serves by server %s", super.toString(), getServerAssigned());
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

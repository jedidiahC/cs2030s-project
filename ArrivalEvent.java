package cs2030.simulator;

class ArrivalEvent extends Event { 
    private final Customer customer;

    ArrivalEvent(double time, Customer customer) {
        super(time);
        this.customer = customer;
    }
    
    @Override
    EventResult process(SimulatorState simulatorState) {
        Server[] servers = simulatorState.getServers;

        for (Server server : servers) {
             
        }

        Event followupEvent = new ServeEvent(this.getTime(), this.customer, null);

        return new EventResult(folllowupEvent, simulatorState);
    }

    @Override
    public String toString() {
        return String.format("%.3f ", getTime());
    }
}


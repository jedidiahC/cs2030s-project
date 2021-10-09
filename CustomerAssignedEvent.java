package cs2030.simulator;

class CustomerAssignedEvent extends CustomerEvent { 
    private final int serverAssigned;

    CustomerAssignedEvent(double time, Customer customer, int serverAssigned) {
        super(time, customer);
        this.serverAssigned = serverAssigned;
    }

    protected int getServerAssigned() {
        return this.serverAssigned;
    }
}

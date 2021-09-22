package cs2030.simulator;

class Server {
    private static final int NO_CUSTOMER_ID = -1;

    private final int serverId;
    private final int customerInQueue;
    private final int customerServed;
    private final double nextServeTime;

    Server(int serverId) {
        this.serverId = serverId;
        this.customerInQueue = NO_CUSTOMER_ID;
        this.customerServed = NO_CUSTOMER_ID;
        this.nextServeTime = 0;
    }

    Server(int serverId, int customerInQueue, int customerServed, double nextServeTime) {
        this.serverId = serverId;
        this.customerInQueue = customerInQueue;
        this.customerServed = customerServed;
        this.nextServeTime = nextServeTime;
    }

    int getServerId() {
        return this.serverId;
    }

    Server serveCustomer(int customer, double nextServeTime) {
        if (canServe(customer)) {
            return new Server(this.serverId, NO_CUSTOMER_ID, customer, nextServeTime);
        }

        return this;
    }

    Server queueCustomer(int customer) {
        if (canQueue()) {
            return new Server(this.serverId, customer, this.customerServed, this.nextServeTime);
        }

        return this;
    }

    Server completeService(int customer) {
        if (customer == this.customerServed) {
            return new Server(this.serverId, this.customerInQueue, this.customerServed, 0);
        } 

        return this;
    }

    boolean canServe(int customer) {
        return isIdle() || customer == this.customerInQueue;
    }

    boolean canQueue() {
        return !hasQueue() && isServing();
    }

    boolean isIdle() {
        return !isServing() && !hasQueue();
    }

    boolean isServing() {
        return this.customerServed != NO_CUSTOMER_ID;
    }

    boolean hasQueue() {
        return this.customerInQueue != NO_CUSTOMER_ID;
    }
    
    double getNextServeTime() {
        return this.nextServeTime;
    }

    @Override
    public String toString() {
        return String.format("%d", getServerId());
    }
}

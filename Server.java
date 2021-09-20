package cs2030.simulator;

class Server {
    private final int serverId;
    private final Customer customer;
    private final int inQueue;

    Server(int serverId, Customer customer, int inQueue) {
        this.serverId = serverId;
        this.customer = customer;
        this.inQueue = inQueue;
    }

    Server serveCustomer(Customer customer) {
        this.customer = customer;
    }
}

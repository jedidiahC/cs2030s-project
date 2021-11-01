package cs2030.simulator;

import java.util.PriorityQueue;

class Server {
    private final int serverId;
    private final PriorityQueue<Customer> customerQueue;
    private final int maxQueueLength;
    private final double nextServiceTime;

    Server(int serverId, int maxQueueLength) {
        this.serverId = serverId;
        this.maxQueueLength = maxQueueLength;
        this.customerQueue = new PriorityQueue<Customer>(); 
        this.nextServiceTime = 0;
    }

    Server(Server server, PriorityQueue<Customer> queue, double nextServiceTime) {
        this.serverId = server.getServerId();
        this.customerQueue = queue;
        this.maxQueueLength = server.getMaxQueueLength();
        this.nextServiceTime = nextServiceTime;
    }

    int getServerId() {
        return this.serverId;
    }

    protected PriorityQueue<Customer> getCustomerQueue() {
        return this.customerQueue;   
    }

    protected int getMaxQueueLength() {
        return this.maxQueueLength;   
    }

    Server queueCustomer(Customer customer, double currTime) {
        if (canQueue(customer)) {
            double nextServiceTime = this.nextServiceTime;

            if (currTime > nextServiceTime) {
                nextServiceTime = currTime;
            }

            this.customerQueue.add(customer);
            return new Server(this, this.customerQueue, 
                    nextServiceTime + customer.getServiceTime());
        }

        return this;
    }

    Server completeService(Customer customer) { 
        if (customer == this.customerQueue.peek()) {
            this.customerQueue.poll();
            return new Server(this, this.customerQueue, this.nextServiceTime + getRestTime());
        } 

        return this;
    }

    double getRestTime() {
        return 0.0f;
    }

    boolean canServe(Customer customer) {
        return this.customerQueue.size() == 0;
    }

    boolean canQueue(Customer customer) {
        return !isServing(customer) && this.customerQueue.size() < this.maxQueueLength + 1;
    }

    boolean isServing(Customer customer) {
        return this.customerQueue.peek() == customer;
    }

    double getNextServeTime() {
        return this.nextServiceTime;
    }

    @Override
    public String toString() {
        return String.format("%d", getServerId());
    }
}

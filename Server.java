package cs2030.simulator;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.LinkedList;

class Server {
    private final int serverId;
    private final Queue<Customer> customerQueue;
    private final int maxQueueLength;
    private final Queue<Double> restTimes;

    private final double endRestTime;
    private final double nextServiceTime;

    Server(int serverId, int maxQueueLength, Queue<Double> restTimes) {
        this.serverId = serverId;
        this.maxQueueLength = maxQueueLength;
        this.customerQueue = new LinkedList<Customer>(); 
        this.restTimes = restTimes;

        this.endRestTime = 0;
        this.nextServiceTime = 0;
    }

    Server(Server server, Queue<Customer> queue, double nextServiceTime, double endRestTime) {
        this.serverId = server.getServerId();
        this.restTimes = server.restTimes;

        this.customerQueue = queue;
        this.maxQueueLength = server.getMaxQueueLength();

        this.nextServiceTime = nextServiceTime;
        this.endRestTime = endRestTime;
    }

    int getServerId() {
        return this.serverId;
    }

    protected Queue<Customer> getCustomerQueue() {
        return this.customerQueue;   
    }

    protected int getMaxQueueLength() {
        return this.maxQueueLength;   
    }

    Server queueCustomer(Customer customer, double currTime) {
        if (canQueue(currTime, customer)) {
            double nextServiceTime = this.nextServiceTime;

            if (currTime > nextServiceTime) {
                nextServiceTime = currTime;
            }

            this.customerQueue.add(customer);
            return new Server(this, 
                    this.customerQueue, 
                    nextServiceTime + customer.getServiceTime(),
                    this.endRestTime
                    );
        }

        return this;
    }

    Server completeService(double currTime, Customer customer) { 
        if (customer == this.customerQueue.peek()) {
            this.customerQueue.poll();

            double restDuration = rest();
            double nextServiceTime = this.nextServiceTime + restDuration;

            double endRestTime = restDuration == 0 ? 
                this.endRestTime : 
                currTime + restDuration; 


            return new Server(this, this.customerQueue, nextServiceTime, endRestTime);
        } 
        
        return this;
    }

    double rest() {
        double restDuration = this.restTimes.size() > 0 ? this.restTimes.poll() : 0;
        return restDuration; 
    }

    boolean canServe(double time, Customer customer) {
        return this.customerQueue.size() == 0 && !isResting(time);
    }

    boolean canQueue(double time, Customer customer) {
        return !(isInQueue(customer) || isServing(customer) || isQueueFull() || isResting(time));
    }

    boolean isQueueFull() {
        return this.customerQueue.size() >= this.maxQueueLength + 1;
    }

    boolean isServing(Customer customer) {
        return isNextInQueue(customer);
    }

    boolean isNextInQueue(Customer customer) {
        return this.customerQueue.peek() == customer;
    }

    boolean isInQueue(Customer c) {
        return this.customerQueue.contains(c);
    }

    double estimateServeTime(Customer customer) {
        if (!isInQueue(customer)) {
            return this.nextServiceTime;
        } else {
            double estimatedTime = this.endRestTime;
            for (Customer c : this.customerQueue) {
                if (c == customer) {
                    return estimatedTime;
                }
                estimatedTime += c.getServiceTime();
            }
            return estimatedTime;
        }
    }

    boolean isResting(double time) {
        return time < this.endRestTime;
    }

    @Override
    public String toString() {
        return String.format("%d", getServerId());
    }
}

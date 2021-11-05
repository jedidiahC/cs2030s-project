package cs2030.simulator;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Optional;

class Server {
    private final int serverId;
    private final Queue<Customer> customerQueue;
    private final int maxQueueLength;
    private final Queue<Double> restTimes;
    private final Optional<Customer> customerServiced;

    private final double endRestTime;
    private final double nextServiceTime;

    protected Server(int serverId, int maxQueueLength, 
            Queue<Customer> customerQueue, Queue<Double> restTimes, 
            Optional<Customer> customerServiced, 
            double endRestTime, double nextServiceTime) {
        this.serverId = serverId;
        this.maxQueueLength = maxQueueLength;
        this.customerQueue = customerQueue; 
        this.restTimes = restTimes;
        this.customerServiced = customerServiced;
        this.endRestTime = endRestTime;
        this.nextServiceTime = nextServiceTime;
    }

    static Server createServer(int serverId, int maxQueueLength, Queue<Double> restTimes) {
        return new Server(serverId, maxQueueLength, new LinkedList<Customer>(), restTimes, Optional.<Customer>empty(), 0, 0);
    }

    protected Server update(
            Queue<Customer> customerQueue,
            Optional<Customer> customerServiced, 
            double endRestTime, double nextServiceTime) {
        return new Server(serverId, maxQueueLength, 
                customerQueue, restTimes, 
                customerServiced, 
                endRestTime, nextServiceTime); 
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
    
    protected Optional<Customer> getCustomerServiced() {
        return this.customerServiced;
    }

    protected Queue<Double> getRestTimes() {
        return this.restTimes;
    }

    Server queueCustomer(Customer customer, double currTime) {
        if (canQueue(currTime, customer)) {
            this.customerQueue.add(customer);
            return update(this.customerQueue, this.customerServiced, 
                    this.endRestTime,
                    getNextServiceTime(currTime, customer.getServiceTime())
                    ); 
        }

        System.out.println("Unable to queue " + customer);
        return this;
    }

    Server serveCustomer(Customer customer, double currTime) {
        double nextServiceTime = this.nextServiceTime;

        if (isNextInQueue(customer)) {
            this.customerQueue.poll();
        } else {
            nextServiceTime = getNextServiceTime(currTime, customer.getServiceTime());
        }

        return update(this.customerQueue, 
                Optional.<Customer>of(customer), 
                this.endRestTime,
                nextServiceTime
                );
    }

    double getNextServiceTime(double currTime, double serveTime) {
        return Math.max(currTime, this.nextServiceTime) + serveTime;
    }

    Server completeService(double currTime, Customer customer) { 
        if (isServing(customer)) {
            double restDuration = rest();
            double nextServiceTime = this.nextServiceTime + restDuration;

            double endRestTime = restDuration == 0 ? 
                this.endRestTime : 
                currTime + restDuration; 

            return update(this.customerQueue, Optional.<Customer>empty(), endRestTime, nextServiceTime);
        } 
        
        return this;
    }

    double rest() {
        double restDuration = this.restTimes.size() > 0 ? this.restTimes.poll() : 0;
        return restDuration; 
    }

    boolean canServeNow(double time, Customer customer) {
        return this.customerQueue.size() == 0 && customerServiced.isEmpty() && !isResting(time);
    }

    boolean canQueue(double time, Customer customer) {
        return !(isInQueue(customer) || isQueueFull());
    }

    boolean isQueueFull() {
        return this.customerQueue.size() >= this.maxQueueLength;
    }

    boolean isServing(Customer customer) {
        return this.customerServiced.map(c -> c == customer).orElse(false);
    }

    boolean isNextInQueue(Customer customer) {
        return this.customerQueue.peek() == customer;
    }

    boolean isInQueue(Customer c) {
        return this.customerQueue.contains(c);
    }

    boolean Optional<Customer> nextInQueue() {
        return Optional.<Customer>ofNullable(this.customerQueue.peek());
    }

    double estimateServeTime(Customer customer) {
        double estimatedTime = this.endRestTime;

        if (isServing(customer)) { 
            return estimatedTime + customer.getServiceTime();
        } else if (isInQueue(customer)) {
            for (Customer c : this.customerQueue) {
                if (c == customer) {
                    return estimatedTime;
                }
                estimatedTime += c.getServiceTime();
            }
            return estimatedTime;
        } 

        return this.nextServiceTime;
    }

    boolean isResting(double time) {
        return time < this.endRestTime;
    }

    @Override
    public String toString() {
        return String.format("server %d", getServerId());
    }
}

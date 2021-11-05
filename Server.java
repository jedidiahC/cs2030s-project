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

    private final double nextServiceTime;

    protected Server(int serverId, int maxQueueLength, 
            Queue<Customer> customerQueue, Queue<Double> restTimes, 
            Optional<Customer> customerServiced, 
            double nextServiceTime) {
        this.serverId = serverId;
        this.maxQueueLength = maxQueueLength;
        this.customerQueue = customerQueue; 
        this.restTimes = restTimes;
        this.customerServiced = customerServiced;
        this.nextServiceTime = nextServiceTime;
    }

    static Server createServer(int serverId, int maxQueueLength, Queue<Double> restTimes) {
        return new Server(serverId, maxQueueLength, new LinkedList<Customer>(), 
                restTimes, Optional.<Customer>empty(), 0);
    }

    protected Server update(
            Queue<Customer> customerQueue,
            Optional<Customer> customerServiced, 
            double nextServiceTime) {
        return new Server(serverId, maxQueueLength, 
                customerQueue, restTimes, 
                customerServiced, 
                nextServiceTime); 
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

    protected double getNextServiceTime() {
        return this.nextServiceTime;
    }

    Server queueCustomer(Customer customer, double currTime) {
        if (canQueue(currTime, customer)) {
            this.customerQueue.add(customer);
            return update(this.customerQueue, this.customerServiced, this.nextServiceTime);
        }

        System.out.println("Unable to queue " + customer);
        return this;
    }

    Server serveCustomer(Customer customer, double currTime) {
        if (isNextInQueue(customer)) {
            this.customerQueue.poll();
        } 

        double nextServiceTime = calculateNextServiceTime(currTime, customer.getServiceTime());

        return update(this.customerQueue, 
                Optional.<Customer>of(customer), 
                nextServiceTime
                );
    }

    double calculateNextServiceTime(double currTime, double serveTime) {
        return Math.max(currTime, this.nextServiceTime) + serveTime;
    }

    Server completeService(double currTime, Customer customer) { 
        if (isServing(customer)) {
            double nextServiceTime = this.nextServiceTime + rest();
            return update(this.customerQueue, Optional.<Customer>empty(), nextServiceTime);
        } 
        
        return this;
    }

    double rest() {
        return this.restTimes.size() > 0 ? this.restTimes.poll() : 0;
    }

    boolean canServeNow(double time, Customer customer) {
        return customerServiced
            .map(c -> false)
            .orElse(
                time >= this.nextServiceTime && 
                (this.customerQueue.size() == 0 || isNextInQueue(customer)));
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

    Optional<Customer> nextInQueue() {
        return Optional.<Customer>ofNullable(this.customerQueue.peek());
    }

    double estimateServeTime(Customer customer) {
        double estimatedTime = this.nextServiceTime;

        if (isInQueue(customer)) {
            for (Customer c : this.customerQueue) {
                if (c == customer) {
                    break;
                }
                estimatedTime += c.getServiceTime();
            }
        } 

        return estimatedTime;
    }

    @Override
    public String toString() {
        return String.format("server %d", getServerId());
    }
}

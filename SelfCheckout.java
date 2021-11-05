package cs2030.simulator;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Optional;

class SelfCheckout extends Server {
    static SelfCheckout createSelfCheckout(int serverId, Queue<Customer> sharedQueue, int maxQueueLength) {
        return new SelfCheckout(serverId, maxQueueLength, sharedQueue, new LinkedList<Double>(), Optional.<Customer>empty(), 0, 0);
    }

    protected SelfCheckout(int serverId, int maxQueueLength, 
            Queue<Customer> customerQueue, Queue<Double> restTimes, 
            Optional<Customer> customerServiced, 
            double endRestTime, double nextServiceTime) {
        super(serverId, maxQueueLength, customerQueue, restTimes, customerServiced, endRestTime, nextServiceTime);
    }

    @Override
    protected SelfCheckout update(
            Queue<Customer> customerQueue,
            Optional<Customer> customerServiced, 
            double endRestTime, double nextServiceTime) {
        return new SelfCheckout(this.getServerId(), this.getMaxQueueLength(), 
                customerQueue, this.getRestTimes(), 
                customerServiced, 
                endRestTime, nextServiceTime); 
    }

    @Override
    boolean canServeNow(double time, Customer customer) {
        return this.getCustomerServiced().isEmpty();
    }

    @Override
    double rest() {
        return 0; 
    }

    @Override
    public String toString() {
        return String.format("self-check %d", this.getServerId());
    }
}

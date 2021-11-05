package cs2030.simulator;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Optional;

class SelfCheckout extends Server {
    static SelfCheckout createSelfCheckout(int serverId, 
            Queue<Customer> sharedQueue, int maxQueueLength) {
        return new SelfCheckout(serverId, maxQueueLength, 
                sharedQueue, new LinkedList<Double>(), 
                Optional.<Customer>empty(), 0);
    }

    protected SelfCheckout(int serverId, int maxQueueLength, 
            Queue<Customer> customerQueue, Queue<Double> restTimes, 
            Optional<Customer> customerServiced, 
            double nextServiceTime) {
        super(serverId, maxQueueLength, customerQueue, 
                restTimes, customerServiced, nextServiceTime);
    }

    @Override
    protected SelfCheckout update(
            Queue<Customer> customerQueue,
            Optional<Customer> customerServiced, 
            double nextServiceTime) {
        return new SelfCheckout(this.getServerId(), this.getMaxQueueLength(), 
                customerQueue, this.getRestTimes(), 
                customerServiced, 
                nextServiceTime); 
    }
    
    @Override double estimateServeTime(Customer customer) {
        return this.getNextServiceTime();
    }

    @Override
    public String toString() {
        return String.format("self-check %d", this.getServerId());
    }
}

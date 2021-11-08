package cs2030.simulator;

import java.util.Comparator;

class ServerComparator implements Comparator<Server> { 
    private final Customer customer;
    private final double time;

    public ServerComparator(Customer customer, double time) {
        this.customer = customer;
        this.time = time;
    }

    public int compare(Server s1, Server s2) {
        int compare = Boolean.compare(
                s2.canServeNow(time, customer), 
                s1.canServeNow(time, customer));

        if (compare == 0) {
            compare = Boolean.compare(
                    s2.canQueue(time, customer), 
                    s1.canQueue(time, customer));
        } 

        if (compare == 0 && customer.isGreedy()) {
            compare = Integer.compare(
                    s1.getQueueSize(), 
                    s2.getQueueSize());
        }
        return compare;
    }
}

package cs2030.simulator;

import java.util.function.Supplier;
import java.util.Optional;

class Customer implements Comparable<Customer> {
    private final int customerId;
    private final Supplier<Double> serviceTime;
    private final double arrivalTime;
    private final boolean isGreedy;

    private Optional<Double> serviceTimeCache;

    Customer(int customerId, Supplier<Double> serviceTime, double arrivalTime, boolean isGreedy) {
        this.customerId = customerId;
        this.serviceTime = serviceTime;
        this.arrivalTime = arrivalTime;
        this.isGreedy = isGreedy;

        this.serviceTimeCache = Optional.<Double>empty();
    }

    int getCustomerId() {
        return this.customerId;
    }
    
    Double getServiceTime() {
        Double serviceTime = this.serviceTimeCache.orElseGet(this.serviceTime);
        this.serviceTimeCache = Optional.<Double>of(serviceTime);
        return serviceTime;
    }
 
    double getArrivalTime() {
        return this.arrivalTime;
    }
    
    boolean isGreedy() {
        return this.isGreedy;
    }

    @Override
    public String toString() {
        return String.format("%d%s", this.customerId, isGreedy ? "(greedy)" : "");
    }

    @Override
    public int compareTo(Customer c) {
        return 0;
    }
}

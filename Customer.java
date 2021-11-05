package cs2030.simulator;

import java.util.function.Supplier;
import java.util.Optional;

class Customer implements Comparable<Customer> {
    private final int customerId;
    private final Supplier<Double> serviceTime;
    private final double arrivalTime;

    private Optional<Double> serviceTimeCache;

    Customer(int customerId, Supplier<Double> serviceTime, double arrivalTime) {
        this.customerId = customerId;
        this.serviceTime = serviceTime;
        this.arrivalTime = arrivalTime;

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

    @Override
    public String toString() {
        return String.format("%d", this.customerId);
    }

    @Override
    public int compareTo(Customer c) {
        return 0;
    }
}

package cs2030.simulator;

class Customer implements Comparable<Customer> {
    private final int customerId;
    private final double serviceTime;
    private final double arrivalTime;

    Customer(int customerId, double serviceTime, double arrivalTime) {
        this.customerId = customerId;
        this.serviceTime = serviceTime;
        this.arrivalTime = arrivalTime;
    }

    int getCustomerId() {
        return this.customerId;
    }
    
    double getServiceTime() {
        return this.serviceTime;
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

package cs2030.simulator;

class Customer {
    private final int customerId;
    private final double serviceTime;

    Customer(int customerId, double serviceTime) {
        this.customerId = customerId;
        this.serviceTime = serviceTime;
    }
}

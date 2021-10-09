
package cs2030.simulator;

import java.util.ArrayList;

abstract class CustomerEvent extends Event { 
    private final Customer customer;

    CustomerEvent(double time, Customer customer) {
        super(time);
        this.customer = customer;
    }

    protected Customer getCustomer() {
        return this.customer;
    }
    
    @Override
    SimulatorState process(SimulatorState state) {
        return state;
    }

    @Override
    Event nextEvent(SimulatorState state) {
        return this;
    }

    @Override
    boolean hasNextEvent() {
        return false;
    }

    @Override
    public String toString() {
        return String.format("%.3f %s", getTime(), customer);
    }
}


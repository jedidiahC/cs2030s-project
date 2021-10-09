package cs2030.simulator; 

import java.util.ArrayList;

class SimulatorStats {
    private final double totalWaitingTime;
    private final int numOfCustomersServed;
    private final int numOfCustomersLeft;

    SimulatorStats() {
        this.totalWaitingTime = 0.0f;
        this.numOfCustomersServed = 0;
        this.numOfCustomersLeft = 0;
    }

    SimulatorStats(double totalWaitingTime, int numOfCustomersServed, int numOfCustomersLeft) {
        this.totalWaitingTime = totalWaitingTime;
        this.numOfCustomersServed = numOfCustomersServed;
        this.numOfCustomersLeft = numOfCustomersLeft;
    }

    SimulatorStats trackWaitingTime(double waitingTime) {
        return updateStats(0, 0, waitingTime);
    }

    SimulatorStats trackCustomerServed() {
        return updateStats(1, 0, 0);
    }

    SimulatorStats trackCustomerLeft() {
        return updateStats(0, 1, 0);
    }

    private SimulatorStats updateStats(int servedDelta, int leftDelta, double waitingTimeDelta) {
        return new SimulatorStats(
                this.totalWaitingTime + waitingTimeDelta, 
                this.numOfCustomersServed + servedDelta, 
                this.numOfCustomersLeft + leftDelta
                ); 
    }

    double getAvgWaitingTime() {
        if (numOfCustomersServed == 0) { 
            return 0; 
        }

        return this.totalWaitingTime / numOfCustomersServed;
    }

    int getNumOfCustomersServed() {
        return this.numOfCustomersServed;
    }

    int getNumOfCustomersWhoLeft() {
        return this.numOfCustomersLeft;
    }

    @Override
    public String toString() {
        return String.format(
                "[%.3f %d %d]", 
                getAvgWaitingTime(), 
                getNumOfCustomersServed(), 
                getNumOfCustomersWhoLeft(
                    ));
    }
}

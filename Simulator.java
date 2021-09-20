package cs2030.simulator;

import java.util.PriorityQueue;
import java.util.List;

public class Simulator {
    private static final double SERVICE_TIME = 1.0f;

    private final PriorityQueue<Event> eventPq;

    public Simulator() {
        eventPq = new PriorityQueue<Event>();
    }

    public void simulate(List<Double> arrivalTimes, int numOfServers) {
        addArrivalTimesAsEvents(arrivalTimes);

        while (!eventPq.isEmpty()) {
            Event event = eventPq.poll();    
            System.out.println(event);
        }
    }

    private void addArrivalTimesAsEvents(List<Double> arrivalTimes) {
        int customerId = 1; 
        for (Double arrivalTime: arrivalTimes) {
            eventPq.add(new ArrivalEvent(arrivalTime, new Customer(++customerId, SERVICE_TIME)));
        }
    }
}

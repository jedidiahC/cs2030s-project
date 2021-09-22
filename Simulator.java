package cs2030.simulator;

import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class Simulator {
    private static final double SERVICE_TIME = 1.0f;

    public void simulate(List<Double> arrivalTimes, int numOfServers) {
        Comparator<Event> comparator = new EventTimeComparator();
        PriorityQueue<Event> eventPq = new PriorityQueue<Event>(arrivalTimes.size(), comparator);
    
        initNewArrivalEvents(arrivalTimes, eventPq);

        ArrayList<Server> servers = new ArrayList<Server>();

        for (int i = 1; i <= numOfServers; i++) {
            servers.add(new Server(i));
        }
        SimulatorState currSimulatorState = new SimulatorState(servers);

        while (!eventPq.isEmpty()) {
            Event event = eventPq.poll();    
            System.out.println(event);

            EventResult eventResult = event.process(currSimulatorState);
            currSimulatorState = eventResult.getSimulatorState();

            if (event.hasFollowupEvent()) {
                eventPq.add(eventResult.getFollowup());
            }
        }
    }

    private void initNewArrivalEvents(List<Double> arrivalTimes, PriorityQueue<Event> eventPq) {
        int customerId = 1; 
        for (Double arrivalTime: arrivalTimes) {
            eventPq.add(new ArrivalEvent(arrivalTime, new Customer(++customerId, SERVICE_TIME)));
        }
    }
}

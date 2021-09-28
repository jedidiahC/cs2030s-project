package cs2030.simulator;

import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class Simulator {
    private static final double SERVICE_TIME = 1.0f;

    /**
     * Simulates a multi-server system.
     */
    public void simulate(List<Double> arrivalTimes, int numOfServers) {
        Comparator<Event> comparator = new EventTimeComparator();
        PriorityQueue<Event> eventPq = new PriorityQueue<Event>(arrivalTimes.size(), comparator);
    
        initNewArrivalEvents(arrivalTimes, eventPq);

        ArrayList<Server> servers = new ArrayList<Server>();

        for (int i = 1; i <= numOfServers; i++) {
            servers.add(new Server(i));
        }

        SimulatorState state = new SimulatorState(servers);
        SimulatorStats stats = new SimulatorStats();

        while (!eventPq.isEmpty()) {
            Event event = eventPq.poll();    
            System.out.println(event);

            if (event.hasNextEvent()) {
                eventPq.add(event.nextEvent(state));
            } 

            stats = event.updateSimulatorStats(state, stats);
            state = event.process(state);
        }
        
        System.out.println(stats);
    }

    private void initNewArrivalEvents(List<Double> arrivalTimes, PriorityQueue<Event> eventPq) {
        int customerId = 1; 
        for (Double arrivalTime: arrivalTimes) {
            eventPq.add(new ArrivalEvent(arrivalTime, new Customer(customerId++, SERVICE_TIME)));
        }
    }
}

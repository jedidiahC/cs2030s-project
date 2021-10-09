package cs2030.simulator;

import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class Simulator {
    private static final double DEFAULT_SERVICE_TIME = 1.0f;
    private static final int DEFAULT_SERVER_QUEUE_SIZE = 1;

    /**
     * Simulates a multi-server system.
     */
    public void simulate(List<Double> arrivalTimes, 
            List<Double> serviceTimes, 
            int numOfServers, 
            int queueSize) {

        ArrayList<Server> servers = initServers(numOfServers, queueSize);

        PriorityQueue<Event> eventPq = new PriorityQueue<Event>(
                arrivalTimes.size(), 
                new EventTimeComparator());

        initPq(arrivalTimes, serviceTimes, eventPq);

        SimulatorState state = new SimulatorState(servers);
        SimulatorStats stats = new SimulatorStats();

        while (!eventPq.isEmpty()) {
            Event event = eventPq.poll();    
            System.out.println(event);

            if (event.hasNextEvent()) {
                eventPq.add(event.nextEvent(state));
            } 

            stats = event.updateStats(state, stats);
            state = event.process(state);
        }

        System.out.println(stats);
    }

    /**
     * Simulates a multi-server system.
     */
    public void simulate(List<Double> arrivalTimes, int numOfServers) {
        ArrayList<Double> serviceTimes = new ArrayList<Double>();
        for (double arrivalTime : arrivalTimes) {
            serviceTimes.add(DEFAULT_SERVICE_TIME);
        }

        simulate(arrivalTimes, serviceTimes, numOfServers, DEFAULT_SERVER_QUEUE_SIZE);
    }

    ArrayList<Server> initServers(int numOfServers, int queueSize) {
        ArrayList<Server> servers = new ArrayList<Server>();

        for (int i = 1; i <= numOfServers; i++) {
            servers.add(new Server(i, queueSize));
        }

        return servers;
    }

    void initPq(List<Double> arrivalTimes, 
            List<Double> serviceTimes, 
            PriorityQueue<Event> eventPq) {
        int customerId = 1;
        for (int i = 0; i < arrivalTimes.size(); i++) {
            eventPq.add(
                    new ArrivalEvent(arrivalTimes.get(i), 
                        new Customer(customerId++, serviceTimes.get(i), arrivalTimes.get(i))));
        }
    }
}

package cs2030.simulator;

import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

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

        List<Server> servers = initServers(numOfServers, queueSize);

        PriorityQueue<Event> eventPq = new PriorityQueue<Event>(
                arrivalTimes.size(), 
                new EventTimeComparator());

        initPq(arrivalTimes, serviceTimes, eventPq);

        SimulatorState state = new SimulatorState(servers);
        SimulatorStats stats = new SimulatorStats();

        while (!eventPq.isEmpty()) {
            Event event = eventPq.poll();    

            System.out.println(event);
            event.nextEvent(state).ifPresent(eventPq::add);

            stats = event.updateStats(state, stats);
            state = event.process(state);
        }

        System.out.println(stats);
    }

    /**
     * Simulates a multi-server system based on arrival times and number of servers.
     */
    public void simulate(List<Double> arrivalTimes, int numOfServers) {
        List<Double> serviceTimes = arrivalTimes.stream()
            .map(x -> DEFAULT_SERVICE_TIME)
            .collect(Collectors.toList());

        simulate(arrivalTimes, serviceTimes, numOfServers, DEFAULT_SERVER_QUEUE_SIZE);
    }

    List<Server> initServers(int numOfServers, int queueSize) {
        return IntStream
            .range(0, numOfServers)
            .mapToObj(i -> new Server(i + 1, queueSize))
            .collect(Collectors.toList());
    }

    void initPq(List<Double> arrivalTimes, 
            List<Double> serviceTimes, 
            PriorityQueue<Event> eventPq) {
        int customerId = 1;

        IntStream.range(0, arrivalTimes.size())
            .forEach(i -> {
                Double arrivalTime = arrivalTimes.get(i);
                Customer customer = new Customer(i + 1, serviceTimes.get(i), arrivalTime);
                eventPq.add(new ArrivalEvent(arrivalTime, customer)); 
            });
    }
}

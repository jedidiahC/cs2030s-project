package cs2030.simulator;

import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

public class Simulator {
    private static final double DEFAULT_SERVICE_TIME = 1.0f;
    private static final int DEFAULT_SERVER_QUEUE_SIZE = 1;

    /**
     * Simulates a multi-server system based on arrival times and number of servers.
     */
    public void simulate(List<Double> arrivalTimes, int numOfServers) {
        List<Double> serviceTimes = arrivalTimes.stream()
            .map(x -> DEFAULT_SERVICE_TIME)
            .collect(Collectors.toList());

        simulate(arrivalTimes, serviceTimes, numOfServers, DEFAULT_SERVER_QUEUE_SIZE);
    }

    /**
     * Simulates a multi-server system.
     */
    public void simulate(List<Double> arrivalTimes, 
            List<Double> serviceTimes, 
            int numOfServers, 
            int queueSize) {
        simulate(arrivalTimes, serviceTimes, new ArrayList<Double>(), numOfServers, queueSize);
    }

    public void simulate(List<Double> arrivalTimes, 
            List<Double> serviceTimes, 
            List<Double> restTimes,
            int numOfServers, 
            int queueSize
            ) {
        simulate(arrivalTimes, serviceTimes, restTimes, numOfServers, queueSize, 0);
    }

    /**
     * Simulates a multi-server system.
     */
    public void simulate(List<Double> arrivalTimes, 
            List<Double> serviceTimes, 
            List<Double> restTimes,
            int numOfServers, 
            int queueSize,
            int numOfSelfCheckout) {

        List<Server> servers = initServers(numOfServers, queueSize, restTimes);
        initSelfCheckout(servers, numOfSelfCheckout, queueSize);

        PriorityQueue<Event> eventPq = new PriorityQueue<Event>(
                arrivalTimes.size(), 
                new EventTimeComparator());

        initEventPq(arrivalTimes, serviceTimes, eventPq);

        SimulatorState state = new SimulatorState(servers);
        SimulatorStats stats = new SimulatorStats();

        while (!eventPq.isEmpty()) {
            Event event = eventPq.poll();    

            if (event.toString() != "") {
                System.out.println(event);
            }

            event.nextEvent(state).ifPresent(eventPq::add);

            stats = event.updateStats(state, stats);
            state = event.process(state);
        }

        System.out.println(stats);
    }

    List<Server> initServers(int numOfServers, int queueSize, List<Double> restTimes) {
        LinkedList<Double> restTimeQueue = new LinkedList<Double>();
        restTimeQueue.addAll(restTimes);

        return IntStream
            .range(0, numOfServers)
            .mapToObj(i -> Server.createServer(i + 1, queueSize, restTimeQueue))
            .collect(Collectors.toList());
    }

    void initSelfCheckout(List<Server> servers, int numOfSelfCheckout, int queueSize) {
        LinkedList<Customer> sharedQueue = new LinkedList<Customer>();
        int k = servers.size();
        IntStream
            .range(0, numOfSelfCheckout)
            .forEach(i -> servers.add(
                        SelfCheckout.createSelfCheckout(k + i + 1, sharedQueue, queueSize))
                    );
    }

    void initEventPq(List<Double> arrivalTimes, 
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

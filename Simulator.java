package cs2030.simulator;

import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Supplier;
import java.util.function.Function;
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

    /**
     * Simulates a multi-server system.
     */
    public void simulate(List<Double> arrivalTimes, 
            List<Double> serviceTimes, 
            List<Double> restTimes,
            int numOfServers, 
            int queueSize) {
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

        initEventPq(eventPq, arrivalTimes, 
            i -> () -> serviceTimes.get(i),
            () -> false);

        SimulatorState state = new SimulatorState(servers);
        SimulatorStats stats = new SimulatorStats();

        while (!eventPq.isEmpty()) {
            Event event = eventPq.poll();    

            if (event.toString() != "") {
                System.out.println(event);
            }

            event.nextEvent(state).ifPresent(eventPq::add);

            state = event.process(state);
            stats = event.updateStats(state, stats);
        }

        System.out.println(stats);
    }

    /**
     * Simulates a multi-server system.
     */
    public void simulateRandom(
        int seed,
        int numOfServers,
        int maxQueueLength,
        int numOfSelfCheckout,
        int numOfCustomers,
        double arrivalRate,
        double serviceRate,
        double restingRate,
        double restP,
        double greedyP) {

        RandomGenerator rg = new RandomGenerator(
                seed,
                arrivalRate,
                serviceRate,
                restingRate
            );

        List<Double> arrivalTimes = new ArrayList<Double>();
        double arrivalTime = 0;

        for (int i = 0; i < numOfCustomers; i++) {
            arrivalTime += i == 0 ? 0 : rg.genInterArrivalTime();
            arrivalTimes.add(arrivalTime);
        }

        List<Double> restTimes = new ArrayList<Double>();

        for (int i = 0; i < numOfCustomers; i++) {
            restTimes.add(rg.genRandomRest() < restP ?
                    rg.genRestPeriod() :
                    0);
        }

        List<Server> servers = initServers(numOfServers, maxQueueLength, restTimes);
        initSelfCheckout(servers, numOfSelfCheckout, maxQueueLength);

        PriorityQueue<Event> eventPq = new PriorityQueue<Event>(
                arrivalTimes.size(), 
                new EventTimeComparator());

        initEventPq(eventPq, arrivalTimes, 
            i -> rg::genServiceTime,
            () -> rg.genCustomerType() < greedyP);

        SimulatorState state = new SimulatorState(servers);
        SimulatorStats stats = new SimulatorStats();

        while (!eventPq.isEmpty()) {
            Event event = eventPq.poll();    

            if (event.toString() != "") {
                System.out.println(event);
            }

            event.nextEvent(state).ifPresent(eventPq::add);

            state = event.process(state);
            stats = event.updateStats(state, stats);
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
                    SelfCheckout.createSelfCheckout(k + i + 1, sharedQueue, queueSize)
                )
            );
    }

    void initEventPq(PriorityQueue<Event> eventPq, List<Double> arrivalTimes, 
            Function<Integer, Supplier<Double>> getServiceTime,
            Supplier<Boolean> isGreedy
    ) {
        IntStream.range(0, arrivalTimes.size())
            .forEach(i -> {
                Double arrivalTime = arrivalTimes.get(i);
                Customer customer = new Customer(
                        i + 1, 
                        getServiceTime.apply(i), 
                        arrivalTime, 
                        isGreedy.get());
                eventPq.add(new ArrivalEvent(arrivalTime, customer)); 
            });
    }
}

package cs2030.simulator;

import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.IntToDoubleFunction;
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
     * Simulates a multi-server system based on arrival times, service times, 
     * number of servers and queue size.
     */
    public void simulate(List<Double> arrivalTimes, 
            List<Double> serviceTimes, 
            int numOfServers, 
            int queueSize) {
        simulate(arrivalTimes, serviceTimes, new ArrayList<Double>(), numOfServers, queueSize);
    }

    /**
     * Simulates a multi-server system based on arrival times, service times, 
     * rest times, number of servers and queue size.
     */
    public void simulate(List<Double> arrivalTimes, 
            List<Double> serviceTimes, 
            List<Double> restTimes,
            int numOfServers, 
            int queueSize) {
        simulate(arrivalTimes, serviceTimes, restTimes, numOfServers, queueSize, 0);
    }

    /**
     * Simulates a multi-server system based on arrival times, service times, 
     * rest times, number of servers, queue size and number of self checkout counters.
     */
    public void simulate(List<Double> arrivalTimes, 
            List<Double> serviceTimes, 
            List<Double> restTimes,
            int numOfServers, 
            int queueSize,
            int numOfSelfCheckout) {

        List<Server> servers = initServers(numOfServers, queueSize, restTimes);
        addSelfCheckout(servers, numOfSelfCheckout, queueSize);

        PriorityQueue<Event> eventPq = initEventPq(arrivalTimes, 
            i -> () -> serviceTimes.get(i),
            () -> false);

        startSimulatorLoop(eventPq, servers);
    }

    /**
     * Simulates a multi-server system using pseudo-random arrival times.
     */
    public void simulate(int seed, int numOfServers,
        int maxQueueLength, int numOfSelfCheckout,
        int numOfCustomers, double arrivalRate,
        double serviceRate, double restingRate,
        double restP, double greedyP) {

        RandomGenerator rg = new RandomGenerator(
                seed,
                arrivalRate,
                serviceRate,
                restingRate
            );

        List<Double> arrivalTimes = initArrivalTimes(numOfCustomers,
            i -> i == 0 ? 0 : rg.genInterArrivalTime()
        );

        List<Double> restTimes = new ArrayList<Double>();
        IntStream.range(0, numOfCustomers)
            .mapToDouble(i -> rg.genRandomRest() < restP ?
                rg.genRestPeriod() :
                0)
            .forEach(restTimes::add);

        List<Server> servers = initServers(numOfServers, maxQueueLength, restTimes);
        addSelfCheckout(servers, numOfSelfCheckout, maxQueueLength);

        PriorityQueue<Event> eventPq = initEventPq(arrivalTimes, 
            i -> rg::genServiceTime,
            () -> rg.genCustomerType() < greedyP);

        startSimulatorLoop(eventPq, servers);
    }

    private List<Double> initArrivalTimes(int numOfCustomers, IntToDoubleFunction generator) {
        List<Double> arrivalTimes = new ArrayList<Double>();
        double arrivalTime = 0;
        IntStream.range(0, numOfCustomers)
            .mapToDouble(generator)
            .reduce(arrivalTime, (currTime, delta) -> { 
                double newArrivalTime = currTime + delta;
                arrivalTimes.add(newArrivalTime);
                return newArrivalTime;
            });
        return arrivalTimes;
    }

    private List<Server> initServers(int numOfServers, int queueSize, List<Double> restTimes) {
        LinkedList<Double> restTimeQueue = new LinkedList<Double>();
        restTimeQueue.addAll(restTimes);

        return IntStream
            .range(0, numOfServers)
            .mapToObj(i -> Server.createServer(i + 1, queueSize, restTimeQueue))
            .collect(Collectors.toList());
    }

    private void addSelfCheckout(List<Server> servers, int numOfSelfCheckout, int queueSize) {
        LinkedList<Customer> sharedQueue = new LinkedList<Customer>();
        int k = servers.size();
        IntStream
            .range(0, numOfSelfCheckout)
            .forEach(i -> servers.add(
                    SelfCheckout.createSelfCheckout(k + i + 1, sharedQueue, queueSize)
                )
            );
    }

    private PriorityQueue<Event> initEventPq(List<Double> arrivalTimes, Function<Integer, 
            Supplier<Double>> getServiceTime, Supplier<Boolean> isGreedy) {
        PriorityQueue<Event> eventPq = new PriorityQueue<Event>(
                arrivalTimes.size(), 
                new EventTimeComparator());

        IntStream.range(0, arrivalTimes.size())
            .mapToObj(i -> 
                new Customer(i + 1, getServiceTime.apply(i), arrivalTimes.get(i), isGreedy.get())
            )
            .map(c -> new ArrivalEvent(c.getArrivalTime(), c))
            .forEach(eventPq::add);

        return eventPq;
    }

    private void startSimulatorLoop(PriorityQueue<Event> eventPq, List<Server> servers) {
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
}

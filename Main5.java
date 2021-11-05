

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import cs2030.simulator.Simulator;
import cs2030.simulator.RandomGenerator;

class Main5 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input: numOfServers, maxQueueLength, numOfCustomers
        int seed = sc.nextInt();
        int numOfServers = sc.nextInt();
        int numOfSelfCheckout = sc.nextInt();
        int maxQueueLength = sc.nextInt();
        int numOfCustomers = sc.nextInt();

        double arrivalRate = sc.nextDouble();
        double serviceRate = sc.nextDouble();
        double restingRate = sc.nextDouble();
        double restP= sc.nextDouble();
        double greedyP = sc.nextDouble();

        Simulator s = new Simulator();
        s.simulateRandom(seed, numOfServers, maxQueueLength, numOfSelfCheckout,
            numOfCustomers, arrivalRate, serviceRate, restingRate, restP, greedyP);
    }
}

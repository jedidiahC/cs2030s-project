import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import cs2030.simulator.Simulator;

class Main3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input: numOfServers, maxQueueLength, numOfCustomers
        int numOfServers = sc.nextInt();
        int maxQueueLength = sc.nextInt();
        int numOfCustomers = sc.nextInt();

        List<Double> arrivalTimes = new ArrayList<Double>();
        List<Double> serviceTimes = new ArrayList<Double>();

        for (int i = 0; i < numOfCustomers; i++) {
            arrivalTimes.add(sc.nextDouble());
            serviceTimes.add(sc.nextDouble());
        }

        List<Double> restTimes = new ArrayList<Double>();

        for (int i = 0; i < numOfCustomers; i++) {
            restTimes.add(sc.nextDouble());
        }

        Simulator s = new Simulator();
        s.simulate(arrivalTimes, serviceTimes, restTimes, numOfServers, maxQueueLength);
    }
}

package cs2030.simulator; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;
import java.util.stream.Stream;
import java.util.function.Predicate;
import java.util.function.Supplier;

class SimulatorState {
    private final HashMap<Integer, Server> servers;

    SimulatorState(List<Server> servers) {
        this.servers = new HashMap<Integer, Server>();

        for (Server server: servers) {
            this.servers.put(server.getServerId(), server);
        }
    }

    SimulatorState(HashMap<Integer, Server> servers) {
        this.servers = servers;
    }

    // Return -1 if no idle server found.
    int assignServer(double time, Customer customer) {
        return getStream()
            .sorted((s1, s2) -> {
                int compare = Boolean.compare(
                        s2.canServeNow(time, customer), 
                        s1.canServeNow(time, customer));
                if (compare == 0) {
                    compare = Boolean.compare(
                            s2.canQueue(time, customer), 
                            s1.canQueue(time, customer));
                } 
                if (compare == 0 && customer.isGreedy()) {
                    compare = Integer.compare(
                            s1.getQueueSize(), 
                            s2.getQueueSize());
                }
                return compare;
            })
        .findFirst()
            .map(s -> s.getServerId())
            .orElse(-1);
        //return findServer(s -> s.canServeNow(time, customer))
        //   .or(() -> findServer(s -> s.canQueue(time, customer)))
        //  .map(s -> s.getServerId())
        // .orElse(-1);
    }

    Optional<Server> findServer(Predicate<Server> pred) {
        return getStream().filter(pred).findFirst();
    }

    Stream<Server> getStream() {
        return servers.values().stream();
    }

    SimulatorState updateServer(Server server) {
        servers.put(server.getServerId(), server);
        return this;
    }

    boolean hasServer(int serverId) {
        return this.servers.containsKey(serverId);
    }

    Server getServer(int serverId) {
        return this.servers.get(serverId);
    }
}

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

    SimulatorState(Collection<Server> servers) {
        this.servers = initHashMap(servers);
    }

    SimulatorState(HashMap<Integer, Server> servers) {
        this.servers = servers;
    }

    private HashMap<Integer, Server> initHashMap(Collection<Server> servers) {
        HashMap<Integer, Server> hashMap = new HashMap<Integer, Server>();
        servers.stream().forEach(s -> hashMap.put(s.getServerId(), s));
        return hashMap;
    }

    // Return -1 if no idle server found.
    int assignServer(double time, Customer customer) {
        return serverStream()
            .sorted(new ServerComparator(customer, time))
        .findFirst()
            .map(s -> s.getServerId())
            .orElse(-1);
    }

    Optional<Server> findServer(Predicate<Server> pred) {
        return serverStream().filter(pred).findFirst();
    }

    SimulatorState updateServer(Server server) {
        HashMap<Integer, Server> newHashMap = new HashMap<Integer, Server>();
        newHashMap.putAll(this.servers);
        newHashMap.put(server.getServerId(), server);
        return new SimulatorState(newHashMap);
    }

    private Stream<Server> serverStream() {
        return servers.values().stream();
    }

    boolean hasServer(int serverId) {
        return this.servers.containsKey(serverId);
    }

    Server getServer(int serverId) {
        return this.servers.get(serverId);
    }
}

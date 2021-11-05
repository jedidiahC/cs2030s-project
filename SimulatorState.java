package cs2030.simulator; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
        return findServer(s -> s.canServeNow(time, customer))
            .or(() -> findServer(s -> s.canQueue(time, customer)))
            .map(s -> s.getServerId())
            .orElse(-1);
    }

    Optional<Server> findServer(Predicate<Server> pred) {
        return servers.values().stream().filter(pred).findFirst();
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

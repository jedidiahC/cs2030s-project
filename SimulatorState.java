package cs2030.simulator; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

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
    int assignServer(Customer customer) {
        int serverId = findServer(s -> s.canServe(customer));

        if (serverId != -1) {
            return serverId;
        } else {
            return findServer(s -> s.canQueue(customer));
        }
    }

    int findServer(Predicate<Server> pred) {
        Collection<Server> servers = this.servers.values();

        return servers.stream().filter(pred)
            .map(server -> server.getServerId())
            .findFirst().orElse(-1);
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

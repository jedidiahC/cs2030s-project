package cs2030.simulator; 

import java.util.ArrayList;
import java.util.HashMap;

class SimulatorState {
    private final HashMap<Integer, Server> servers;

    SimulatorState(ArrayList<Server> servers) {
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
        for (Server server: this.servers.values()) {
            if (server.canServe(customer)) {
                return server.getServerId(); 
            }
        }

        for (Server server: this.servers.values()) {
            if (server.canQueue(customer)) {
                return server.getServerId(); 
            }
        }
        return -1;
    }

    SimulatorState updateServer(Server server) {
        servers.put(server.getServerId(), server);
        return new SimulatorState(servers);
    }

    boolean hasServer(int serverId) {
        return this.servers.containsKey(serverId);
    }

    Server getServer(int serverId) {
        return this.servers.get(serverId);
    }
}

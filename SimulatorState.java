package cs2030.simulator; 

import java.util.ArrayList;

class SimulatorState {
    private final ArrayList<Server> servers;

    SimulatorState(ArrayList<Server> servers) {
        this.servers = servers;
    }

    ArrayList<Server> getServers() {
        return this.servers;
    }

    SimulatorState updateServer(Server server) {
        for (int serverIndex = 0; serverIndex < this.servers.size(); serverIndex++) {
            if (this.servers.get(serverIndex).getServerId() == server.getServerId()) {
                this.servers.set(serverIndex, server);
            }
        }

        return new SimulatorState(this.servers);
    }

    Server getUpdatedServer(Server oldServer) {
        for (Server server : this.servers) {
            if (server.getServerId() == oldServer.getServerId()) {
                return server;
            }
        }
        
        return oldServer;
    }
}

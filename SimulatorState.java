package cs2030.simulator; 

class SimulatorState {
    private final Server[] servers;

    SimulatorState(Server[] servers) {
        this.servers = servers;
    }

    Server[] getServers() {
        return servers;
    }
}

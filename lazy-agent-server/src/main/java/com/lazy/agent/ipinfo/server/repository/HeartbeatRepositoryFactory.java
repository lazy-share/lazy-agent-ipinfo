package com.lazy.agent.ipinfo.server.repository;

public class HeartbeatRepositoryFactory {


    private static final HeartbeatRepository heartbeatRepository = new HeartbeatMemoryRepository();

    public static HeartbeatRepository of() {
        return heartbeatRepository;
    }
}

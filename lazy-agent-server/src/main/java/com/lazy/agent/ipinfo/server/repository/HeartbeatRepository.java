package com.lazy.agent.ipinfo.server.repository;

import com.lazy.agent.ipinfo.base.Probe;

import java.util.List;
import java.util.Map;

public interface HeartbeatRepository {

    void add(Probe server);

    void update(Probe server);

    Map<String, List<Probe>> list();

}

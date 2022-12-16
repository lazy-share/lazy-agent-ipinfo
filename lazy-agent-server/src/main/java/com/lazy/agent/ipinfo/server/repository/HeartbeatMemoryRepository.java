package com.lazy.agent.ipinfo.server.repository;

import com.lazy.agent.ipinfo.base.Config;
import com.lazy.agent.ipinfo.base.Probe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HeartbeatMemoryRepository implements HeartbeatRepository {

    private final Map<String, List<Probe>> probeHolder = new ConcurrentHashMap<>();

    @Override
    public void add(Probe probe) {
        List<Probe> list = probeHolder.computeIfAbsent(probe.getIp(), k -> new ArrayList<>());
        list.add(probe);
    }

    @Override
    public synchronized void update(Probe probe) {
        if (probe == null) {
            return;
        }
        List<Probe> probes = probeHolder.computeIfAbsent(probe.getIp(), k -> new ArrayList<>());
        boolean found = false;
        for (Probe oldProbe : probes) {
            if (oldProbe.equals(oldProbe)) {
                found = true;
                oldProbe.setLastUpdateTime(System.currentTimeMillis());
                break;
            }
        }
        if (!found) {
            if (probe.getCreateTime() <= 0){
                probe.setCreateTime(System.currentTimeMillis());
            }
            probe.setLastUpdateTime(System.currentTimeMillis());
            probes.add(probe);
        }
    }

    @Override
    public Map<String, List<Probe>> list() {
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<String, List<Probe>> entry : probeHolder.entrySet()) {
            //每次在查询的时候触发数据清理，将超过心跳上报间隔时间 * 2倍数 的还未更新过的探针清理出去
            entry.getValue().removeIf(next -> (currentTime - next.getLastUpdateTime()) > (Config.SEND_HEARTBEAT_TIME + Config.SEND_HEARTBEAT_DOWN_LINE_TIME));
        }
        return this.probeHolder;
    }
}

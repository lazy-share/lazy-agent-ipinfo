package com.lazy.agent.ipinfo.base;

import java.io.Serializable;
import java.util.Objects;

/**
 * 客户端探针类
 *
 * @author laizhiyuan
 */
public class Probe implements Serializable {

    private static final long serialVersionUID = 98765432124352L;
    // 客户端上报的服务名称
    private String serverName;
    // 客户端上报的ip地址
    private String ip;
    private String id;
    // 客户端首次上报的时间
    private long createTime;
    // 客户端当前上报的时间
    private long lastUpdateTime;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Probe probe = (Probe) o;
        return Objects.equals(serverName, probe.serverName) && Objects.equals(ip, probe.ip) && Objects.equals(id, probe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverName, ip, id);
    }
}

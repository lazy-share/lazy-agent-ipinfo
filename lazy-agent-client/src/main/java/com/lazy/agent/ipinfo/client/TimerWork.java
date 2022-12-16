package com.lazy.agent.ipinfo.client;

import com.lazy.agent.ipinfo.base.Config;
import com.lazy.agent.ipinfo.base.IpAddress;

/**
 * 定时执行上报
 */
public class TimerWork {

    //健康上报类，一个客户端实例化一个上报类
    private SendHeartbeat sendHeartbeat;

    public TimerWork(String serverName) {
        if (serverName == null || "".equals(serverName)) {
            System.out.println("请传递服务名称参数");
            return;
        }
        String ip = IpAddress.getIp();
        if (ip == null || "".equals(ip)) {
            System.out.println("ip没有找到");
            return;
        }
        sendHeartbeat = new SendHeartbeat(serverName, ip);
        while (true) {
            try {
                sendHeartbeat.send();
                //每隔30s发送一次心跳
                Thread.sleep(Config.SEND_HEARTBEAT_TIME);
            } catch (Exception ignored) {
            }
        }
    }
}

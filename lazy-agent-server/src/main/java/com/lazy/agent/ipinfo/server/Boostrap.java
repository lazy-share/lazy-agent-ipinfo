package com.lazy.agent.ipinfo.server;

import com.lazy.agent.ipinfo.server.console.StartHeartbeatConsoleTask;
import com.lazy.agent.ipinfo.server.hearbeat.StartReceivedHeartbeatTask;

/**
 * 引导启动类
 */
public class Boostrap {

    //记录控制台启动时间
    public static long startTime = System.currentTimeMillis();

    public Boostrap(String arguments) {
        //解析端口
        Integer serverPort = null;
        Integer consolePort = null;
        if (arguments != null && !"".equals(arguments)) {
            String[] ports = arguments.split(",");
            try {
                serverPort = Integer.parseInt(ports[0]);
                consolePort = Integer.parseInt(ports[1]);
            } catch (Exception ignored) {

            }
        }

        //启动接收心跳健康信息服务
        ThreadPoolHolder.bootstrapExecutor.submit(new StartReceivedHeartbeatTask(serverPort));
        //启动监控控制台服务
        ThreadPoolHolder.bootstrapExecutor.submit(new StartHeartbeatConsoleTask(consolePort));
    }

}

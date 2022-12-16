package com.lazy.agent.ipinfo.server;

import java.lang.instrument.Instrumentation;

public class Agent {

    //执行main方法前，会执行该参数
    //"-javaagent:D:\Projects\agent-server.jar=这里传参，可以传很多信息参数，服务端口就是通过这里逗号分隔方式传入"
    public static void premain(String arguments, Instrumentation instrumentation) {
        try {
            AgentClassLoader.loadClass(instrumentation);
            new Boostrap(arguments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

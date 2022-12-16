package com.lazy.agent.ipinfo.client;

import java.lang.instrument.Instrumentation;

public class Agent {
    //执行main方法前，会执行该方法，并且可以传递参数
    //只需要在启动JVM参数增加： "-javaagent:D:\Projects\agent-client.jar=这里传参，可以传很多信息参数，服务名称就是通过这里传入"
    public static void premain(String serviceName, Instrumentation instrumentation) {
        try {
            //自定义类和lib加载器，构建运行时环境
            AgentClassLoader.loadClass(instrumentation);
            //通过创建一条异步后台线程定时上报健康信息
            new Thread(() -> new TimerWork(serviceName)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

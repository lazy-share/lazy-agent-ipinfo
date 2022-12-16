package com.lazy.agent.ipinfo.base;

public class Config {

    /**
     * IP前缀
     */
    public static final String IP_PRE = "127";
    /**
     * 发送心跳时间间隔,单位毫秒
     */
    public static final long SEND_HEARTBEAT_TIME = 60000;
    /**
     * 发送心跳时间下线时间间隔,单位毫秒
     */
    public static final long SEND_HEARTBEAT_DOWN_LINE_TIME = 10000;
    /**
     * 控制台会话超时时间，单位毫秒
     */
    public static final long CONSOLE_HTTP_SESSION_TIME = 1800000;
    /**
     * 心跳服务监听IP
     */
    public static final String HEARTBEAT_SERVER_IP = "127.0.0.1";
    /**
     * 心跳服务监听默认端口
     */
    public static final int HEARTBEAT_SERVER_PORT = 9520;
    /**
     * 心跳控制台监听默认端口
     */
    public static final int HEARTBEAT_CONSOLE_PORT = 9550;
    /**
     * 控制台HTTP BASIC 用户名
     */
    public static final String CONSOLE_HTTP_BASIC_USERNAME = "lazy";
    /**
     * 控制台HTTP BASIC 密码
     */
    public static final String CONSOLE_HTTP_BASIC_PASSWORD = "lazy123";
    /**
     * 控制台标题
     */
    public static final String CONSOLE_TITLE = "应用健康监控平台";


}

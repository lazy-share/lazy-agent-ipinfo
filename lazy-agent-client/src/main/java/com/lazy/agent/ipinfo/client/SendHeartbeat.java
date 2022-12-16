package com.lazy.agent.ipinfo.client;

import com.lazy.agent.ipinfo.base.Config;
import com.lazy.agent.ipinfo.base.Probe;
import com.lazy.agent.ipinfo.base.SnowflakeIdUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 发送健康类
 */
public class SendHeartbeat {

    // 探针
    private final Probe probe;

    public SendHeartbeat(String serverName, String ip) {
        //实例化探针对象，一个应用对于一个探针
        this.probe = new Probe();
        probe.setServerName(serverName);
        probe.setIp(ip);
        probe.setId(SnowflakeIdUtils.getInstance().nextId() + "");
        //实例化ReportHeartbeat时，记录探针客户端首次创建时间
        probe.setCreateTime(System.currentTimeMillis());
    }

    //上报心跳
    public void send() {
        OutputStream out = null;
        Socket socket = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = null;
        try {
            //HEARTBEAT_SERVER_IP = 上报监控平台服务端 IP，HEARTBEAT_SERVER_PORT = 上报监控平台服务端 端口
            socket = new Socket(Config.HEARTBEAT_SERVER_IP, Config.HEARTBEAT_SERVER_PORT);
            os = new ObjectOutputStream(bos);
            os.writeObject(probe);
            os.flush();
            byte[] b = bos.toByteArray();
            out = socket.getOutputStream();
            out.write(b);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭socket
            this.closeSocket(socket, out, bos, os);
        }
    }

    //关闭socket
    private void closeSocket(Socket socket, OutputStream out, ByteArrayOutputStream bos, ObjectOutputStream os) {
        if (out != null) {
            try {
                out.close();
            } catch (IOException ignored) {
            }
        }
        if (os != null) {
            try {
                os.close();
            } catch (IOException ignored) {
            }
        }
        try {
            bos.close();
        } catch (IOException ignored) {
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
            socket = null;
        }
    }
}

package com.lazy.agent.ipinfo.server.console;

import com.lazy.agent.ipinfo.base.Config;
import com.lazy.agent.ipinfo.server.ThreadPoolHolder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p>
 *
 * </p>
 *
 * @author lzy
 * @since 2022/12/15.
 */
public class StartHeartbeatConsoleTask implements Runnable {

    private Integer port;

    public StartHeartbeatConsoleTask(Integer port) {
        if (port != null) {
            this.port = port;
        } else {
            this.port = Config.HEARTBEAT_CONSOLE_PORT;
        }
    }

    @Override
    public void run() {

        if (port == null) {
            port = 9001;
        }
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("The Heartbeat Console is start in port:" + port);
            while (true) {
                socket = serverSocket.accept();
                ThreadPoolHolder.taskExecutor.submit(new HeartbeatConsoleTask(socket));
            }
        } catch (Exception ignored) {
            //ignored
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                    //ignored
                }
            }
        }
    }
}

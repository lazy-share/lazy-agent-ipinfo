package com.lazy.agent.ipinfo.server.hearbeat;

import com.lazy.agent.ipinfo.base.Probe;
import com.lazy.agent.ipinfo.server.repository.HeartbeatRepository;
import com.lazy.agent.ipinfo.server.repository.HeartbeatRepositoryFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 接收健康类
 */
public class ReceivedHeartbeat implements Runnable {
    //套接字
    private Socket socket;
    public ReceivedHeartbeat(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream is = null;
        PrintWriter out = null;
        ObjectInputStream ois = null;
        try {
            is = this.socket.getInputStream();
            ois = new ObjectInputStream(is);
            Probe server = (Probe) ois.readObject();

            out = new PrintWriter(this.socket.getOutputStream(), true);
            HeartbeatRepository heartbeatRepository = HeartbeatRepositoryFactory.of();
            heartbeatRepository.update(server);
            out.println("ok");
        } catch (Exception ignored) {
        } finally {
            //关闭套接字
            this.closeSocket(is, out, ois);
        }
    }

    private void closeSocket(InputStream is, PrintWriter out, ObjectInputStream ois){
        if (is != null) {
            try {
                is.close();
            } catch (IOException ignored) {
            }
        }
        if (ois != null) {
            try {
                ois.close();
            } catch (IOException ignored) {
            }
        }
        if (out != null) {
            out.close();
        }
        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException ignored) {
            }
            this.socket = null;
        }
    }
}

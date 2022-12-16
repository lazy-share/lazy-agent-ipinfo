package com.lazy.agent.ipinfo.server.console;

import com.lazy.agent.ipinfo.base.Config;
import com.lazy.agent.ipinfo.base.ExceptionUtils;
import com.lazy.agent.ipinfo.base.Probe;
import com.lazy.agent.ipinfo.server.Boostrap;
import com.lazy.agent.ipinfo.server.http.*;
import com.lazy.agent.ipinfo.server.repository.HeartbeatRepository;
import com.lazy.agent.ipinfo.server.repository.HeartbeatRepositoryFactory;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class HeartbeatConsoleTask implements Runnable {


    private Socket socket;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public HeartbeatConsoleTask(Socket socket) {
        this.socket = socket;
    }

    private void response401(HttpRequest httpRequest, PrintWriter out) {
        HttpResponse response = HttpResponseParser.ofResponse(httpRequest, null);
        HttpDigest digest = HttpBasicUtil.ofDigestResponse(httpRequest);
        response.setCode(401);
        response.setStatus("Unauthorized");
        response.setHeader("WWW-Authenticate", digest.ofString());
        //响应HttpResponse
        out.println(HttpResponseParser.ofResponseStr(response));
    }

    @Override
    public void run() {
        PrintWriter out = null;
        //解析HttpRequest
        HttpRequest httpRequest = null;
        try {
            httpRequest = HttpRequestParser.parse2request(socket.getInputStream());
            //创建响应流对象
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            //创建http digest
            HttpDigest digestRequest = HttpBasicUtil.ofDigestRequest(httpRequest);
            if (digestRequest == null) {
                this.response401(httpRequest, out);
                return;
            }
            //校验http basic 超时时间
            String decodeNonce = HttpBasicUtil.base64DecodeNonce(digestRequest.getNonce());
            long nonce = Long.parseLong(decodeNonce.split("::")[0]);
            long currentTime = System.currentTimeMillis();
            if (currentTime - nonce > Config.CONSOLE_HTTP_SESSION_TIME) {
                this.response401(httpRequest, out);
                return;
            }
            //检查用户名称是否正确
            if (!Config.CONSOLE_HTTP_BASIC_USERNAME.equals(digestRequest.getUsername())) {
                this.response401(httpRequest, out);
                return;
            }
            //检查密码是否匹配
            if (!digestRequest.getResponse().equals(digestRequest.md5())) {
                this.response401(httpRequest, out);
                return;
            }

            //Authentication-Info
            HttpResponse response = HttpResponseParser.ofResponse(httpRequest, this.createHtmlByTemplate());
            //响应HttpResponse
            out.println(HttpResponseParser.ofResponseStr(response));
        } catch (Exception e) {
            if (httpRequest != null && out != null) {
                HttpResponse response = HttpResponseParser.ofResponse(httpRequest, "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\">" +
                        "<title>Title</title></head><body><h3>Internal Server Error</h3><p style='white-space: pre-line;'>" + ExceptionUtils.getStackTrace(e) + "</p></body></html>");
                out.println(HttpResponseParser.ofResponseStr(response));
            }
        } finally {
            if (out != null) {
                out.close();
            }
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.socket = null;
            }
        }
    }


    private String createHtmlByTemplate() {
        HeartbeatRepository heartbeatRepository = HeartbeatRepositoryFactory.of();
        Map<String, List<Probe>> heartbeatDataMap = heartbeatRepository.list();
        List<Probe> allServer = new ArrayList<>();

        Set<String> serverCount = new HashSet<>();
        Set<String> ipCount = new HashSet<>();
        Set<String> allCount = new HashSet<>();
        if (heartbeatDataMap != null && !heartbeatDataMap.isEmpty()) {
            for (Map.Entry<String, List<Probe>> entry : heartbeatDataMap.entrySet()) {
                if (entry.getValue() == null || entry.getValue().isEmpty()) {
                    continue;
                }
                for (Probe probe : entry.getValue()) {
                    serverCount.add(probe.getServerName());
                    ipCount.add(probe.getIp());
                    allCount.add(probe.getId());
                }
                allServer.addAll(entry.getValue());
            }
        }

        allServer.sort(Comparator.comparing(Probe::getServerName));

        Map<String, Object> ftlVarDataMap = new HashMap<>();
        ftlVarDataMap.put("title", Config.CONSOLE_TITLE);
        ftlVarDataMap.put("serverCount", serverCount.size());
        ftlVarDataMap.put("ipCount", ipCount.size());
        ftlVarDataMap.put("allCount", allCount.size());
        ftlVarDataMap.put("consoleServerStartTime", formatter.format(new Date(Boostrap.startTime)));
        ftlVarDataMap.put("allServer", allServer);

        byte[] bytes = FreemarkerTemplateEngine.doGenerator("index.html", ftlVarDataMap);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}

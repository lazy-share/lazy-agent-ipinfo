package com.lazy.agent.ipinfo.base;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IpAddress {

    /**
     * 获取ip
     *
     * @return
     */
    public static String getIp() {
        Enumeration<NetworkInterface> allNetInterfaces;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }
        InetAddress ip = null;
        String ipStr = null;
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = allNetInterfaces.nextElement();
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                ip = addresses.nextElement();
                if (ip instanceof Inet4Address) {
                    ipStr = ip.getHostAddress();
                    if (ipStr != null && ipStr.startsWith(Config.IP_PRE)) {
                        return ipStr;
                    }
                }
            }
        }
        return null;
    }

}

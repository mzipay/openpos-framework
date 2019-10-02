package org.jumpmind.pos.util;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class NetworkUtils {

    /**
     * Reports whether or not the given TCP port is already in use.
     * @param port The port to check
     * @return {@code true} if the port is available, {@code false} if it is not.
     */
    public static boolean isTcpPortAvailable(int port) {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.setReuseAddress(false);
            serverSocket.bind(new InetSocketAddress(InetAddress.getByName("localhost"), port), 1);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}

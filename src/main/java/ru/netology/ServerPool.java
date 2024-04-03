package ru.netology;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServerPool {
    private final ExecutorService threadPool;
    boolean work = false;

    private final Map<String, Handler> hanlderMap = new HashMap<>();

    public ServerPool() {
        this.threadPool = Executors.newFixedThreadPool(64);
    }

    public void addHandler(String method, String path, Handler handler) {
        this.hanlderMap.put(method + "#" + path, handler);
    }

    public void start() {
        work = true;
        processRequest();
    }


    public void shutdown() {
        work = false;
    }

    public void processRequest() {

        try (final var serverSocket = new ServerSocket(9999)) {
            while (work) {
                threadPool.submit(new SocketHandler(serverSocket.accept(), this.hanlderMap));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

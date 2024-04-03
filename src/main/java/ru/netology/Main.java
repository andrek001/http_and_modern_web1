package ru.netology;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        ServerPool server = new ServerPool();
        server.addHandler("GET", "/classic1.html", Main::processingGet);
        server.addHandler("GET", "/classic2.html", Main::processingGet2);
        server.addHandler("POST", "/classic2.html", Main::processingPost);
        server.addHandler("GET", "/messages", Main::processingGetMessages);
        server.addHandler("POST", "/messages", Main::processingGetMessages);
        server.start();


    }



    private static void processingGetMessages(Request request, OutputStream out) throws IOException {
        String mimeType = "text/html";
        byte[] content = request.getQueryParams().stream().map(kv->kv.getName()+":"+kv.getValue()).collect(Collectors.joining("#")).getBytes();
        out.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + mimeType + "\r\n" +
                        "Content-Length: " + content.length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.write(content);
    }
    private static void processingPost(Request request, OutputStream out) throws IOException {
        String mimeType = "text/html";
        byte[] content = "Im from post request".getBytes();
        out.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + mimeType + "\r\n" +
                        "Content-Length: " + content.length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.write(content);
    }
        private static void processingGet(Request request, OutputStream out) throws IOException {
        String mimeType = "text/html";
        byte[] content = new Date().toString().getBytes();
        out.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + mimeType + "\r\n" +
                        "Content-Length: " + content.length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.write(content);
        out.flush();
    }
    private static void processingGet2(Request request, OutputStream out) throws IOException {
        String mimeType = "text/html";
        byte[] content = ("BBBBB"+new Date().toString()).getBytes();
        out.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + mimeType + "\r\n" +
                        "Content-Length: " + content.length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.write(content);
        out.flush();
    }
}

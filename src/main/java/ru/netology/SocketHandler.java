package ru.netology;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URLEncodedUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;


public class SocketHandler implements Runnable {

    private static final Handler ERROR_404_HANDLER = (r, out) -> {
        out.write((
                "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.flush();
    };
    private final Socket socket;
    private Map<String, Handler> handlerMap = new HashMap<>();


    public SocketHandler(Socket socket, Map<String, Handler> handlerMap) {
        this.socket = socket;
        this.handlerMap = handlerMap;
    }

    @Override
    public void run() {
        try (
                final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final var out = new BufferedOutputStream(socket.getOutputStream())
        ) {
            // read only request line for simplicity
            // must be in form GET /path HTTP/1.1
            List<String> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = in.readLine()) != null) {

                if ( currentLine.isEmpty()) {
                    break;
                }
                lines.add(currentLine);
            }
            if (lines.isEmpty()) {
                return;
            }
            final var requestLine = lines.get(0);

            Optional<Request> requestOptional = parserRequest(requestLine);
            if (requestOptional.isEmpty()) {
                return;
            }
            Request request = requestOptional.get();
            Handler handler = this.handlerMap.get(request.getMethod() + "#" + request.getPath());
            if (handler != null) {
                handler.process(request, out);
            } else {
                ERROR_404_HANDLER.process(request, out);


            }

/*

            String path = request.getPath();


            if (!VALID_PATH.contains(path)) {
                out.write((
                        "HTTP/1.1 404 Not Found\r\n" +
                                "Content-Length: 0\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                out.flush();
                return;
            }

            final var filePath = Path.of(".", "public", path);
            final var mimeType = Files.probeContentType(filePath);

            // special case for classic
            if (path.equals("/classic.html")) {
                final var template = Files.readString(filePath);
                final var content = template.replace(
                        "{time}",
                        LocalDateTime.now().toString()
                ).getBytes();
                out.write((
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: " + mimeType + "\r\n" +
                                "Content-Length: " + content.length + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                out.write(content);
                out.flush();
                //return;
            }

            final var length = Files.size(filePath);
            out.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + mimeType + "\r\n" +
                            "Content-Length: " + length + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            Files.copy(filePath, out);
            out.flush();
*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Optional<Request> parserRequest(String requestLine) {
        if (requestLine == null) {
            return Optional.empty();
        }

        final var parts = requestLine.split(" ");

        if (parts.length != 3) {
            return Optional.empty();
        }
        final var method = parts[0];
        final var path = parts[1];

        Request request = new Request();
        URI uri = URI.create(path);
        request.setPath(uri.getPath());
        request.setMethod(method);
        List<NameValuePair> params = URLEncodedUtils.parse(uri, Charset.defaultCharset());
        request.setQueryParams(params);
        return Optional.of(request);
    }

}


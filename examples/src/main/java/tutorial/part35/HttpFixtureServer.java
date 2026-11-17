package tutorial.part35;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/** 학습·통합 테스트 전용 HTTP 서버. 운영 서버 구현이 아니다. */
public class HttpFixtureServer {
    public static HttpServer start(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);
        server.createContext("/", exchange -> {
            try {
                String path = exchange.getRequestURI().getPath();
                boolean found = path.equals("/products/P001");
                int status = found ? 200 : 404;
                String body = found
                        ? """
                          {"id":"P001","name":"노트","price":1200.00}
                          """
                        : "{\"error\":\"NOT_FOUND\"}";
                byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                exchange.sendResponseHeaders(status, bytes.length);
                exchange.getResponseBody().write(bytes);
            } finally {
                exchange.close();
            }
        });
        server.start();
        return server;
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = start(18080);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> server.stop(0)));
        System.out.println("HTTP 서버: http://127.0.0.1:18080 (종료: Ctrl+C)");
    }
}

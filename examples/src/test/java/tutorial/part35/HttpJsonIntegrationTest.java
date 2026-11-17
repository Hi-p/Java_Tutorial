package tutorial.part35;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.*;

@Tag("network")
class HttpJsonIntegrationTest {
    private HttpServer server;
    private URI base;
    @BeforeEach
    void start() throws IOException {
        server = HttpFixtureServer.start(0); // 충돌을 피하기 위해 빈 포트를 자동 배정
        base = URI.create("http://127.0.0.1:" + server.getAddress().getPort());
    }
    @AfterEach
    void stop() { if (server != null) server.stop(0); }
    @Test
    void HTTP와_JSON을_연결해서_상품을_조회한다() throws Exception {
        ProductData product = HttpJsonExample.fetchProduct(base.resolve("/products/P001"));
        assertEquals("P001", product.id());
        assertEquals("노트", product.name());
    }
    @Test
    void HTTP_404를_성공_상품으로_해석하지_않는다() {
        IOException e = assertThrows(IOException.class,
                () -> HttpJsonExample.fetchProduct(base.resolve("/missing")));
        assertEquals("HTTP 오류: 404", e.getMessage());
    }
}

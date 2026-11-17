package tutorial.part35;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class HttpJsonExample {
    public static ProductData fetchProduct(URI uri) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();
        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(Duration.ofSeconds(3))
                .header("Accept", "application/json")
                .GET().build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("HTTP 오류: " + response.statusCode());
        }
        String type = response.headers().firstValue("Content-Type").orElse("");
        if (!type.split(";", 2)[0].trim().equalsIgnoreCase("application/json")) {
            throw new IOException("JSON 응답이 아닙니다.");
        }
        return JsonCodec.readProduct(response.body());
    }

    public static void main(String[] args) throws Exception {
        URI uri = URI.create(args.length == 0
                ? "http://127.0.0.1:18080/products/P001" : args[0]);
        ProductData product = fetchProduct(uri);
        System.out.println("HTTP 조회: " + product.id() + " / " + product.name());
        System.out.println("가격: " + product.price().toPlainString());
    }
}

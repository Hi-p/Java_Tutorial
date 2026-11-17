package tutorial.part35;

public class JsonExample {
    public static void main(String[] args) throws Exception {
        String json = """
                {"id":"P001","name":"노트","price":1200.00}
                """;
        ProductData product = JsonCodec.readProduct(json);
        System.out.println("상품: " + product.id() + " / " + product.name());
        System.out.println("가격: " + product.price().toPlainString());
        String encoded = JsonCodec.writeProduct(product);
        // JSON 필드 순서는 의미가 없으므로 문자열 순서 대신 왕복 변환 결과 확인.
        System.out.println("JSON 왕복 변환: " + product.equals(JsonCodec.readProduct(encoded)));
    }
}

package src.auth;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.HashMap;

public class JWTAuth {
    // YOU STORE IN A SECRET
    private static final String SECRET = "my_super_secret"; 
    private static final String HEADER = base64Encode("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");

    public static String createToken(String userId, long ttlMillis) {
        long exp = System.currentTimeMillis() + ttlMillis;
        String payload = base64Encode("{\"userId\":\"" + userId + "\",\"exp\":" + exp + "}");
        String signature = sign(HEADER + "." + payload);
        return HEADER + "." + payload + "." + signature;
    }

    public static boolean verifyToken(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3)
            return false;

        String header = parts[0];
        String payload = parts[1];
        String signature = parts[2];

        String expectedSignature = sign(header + "." + payload);
        if (!expectedSignature.equals(signature))
            return false;

        String payloadJson = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
        Map<String, Object> claims = parseJson(payloadJson);
        long exp = ((Number) claims.get("exp")).longValue();

        return System.currentTimeMillis() < exp;
    }

    public static String extractUserId(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3)
            return null;
        String payload = parts[1];
        String payloadJson = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
        Map<String, Object> claims = parseJson(payloadJson);
        return (String) claims.get("userId");
    }

    private static String base64Encode(String input) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

    private static String sign(String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secretKey);
            byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error while signing token", e);
        }
    }

    private static Map<String, Object> parseJson(String json) {
        Map<String, Object> map = new HashMap<>();
        json = json.trim().replaceAll("[{}\"]", "");
        String[] parts = json.split(",");
        for (String part : parts) {
            String[] kv = part.split(":");
            if (kv.length == 2) {
                String key = kv[0].trim();
                String value = kv[1].trim();
                if (value.matches("\\d+")) {
                    map.put(key, Long.parseLong(value));
                } else {
                    map.put(key, value);
                }
            }
        }
        return map;
    }

    public static void main(String[] args) {
        String token = createToken("sidharth", 10000);
        System.out.println("JWT Token: " + token);
        System.out.println("Is Valid: " + verifyToken(token));
        System.out.println("User ID: " + extractUserId(token));
    }
}

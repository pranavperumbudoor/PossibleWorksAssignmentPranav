import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class Main {

    public static void main(String[] args) throws IOException {
        String testCase1 = new String(Files.readAllBytes(Paths.get("testcase1.json")));
        String testCase2 = new String(Files.readAllBytes(Paths.get("testcase2.json")));

        BigInteger secret1 = findSecret(testCase1);
        BigInteger secret2 = findSecret(testCase2);

        System.out.println("First secret from testcase1:" + secret1);
        System.out.println("Second secret from testcase2:" + secret2);
    }

    public static BigInteger findSecret(String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject keys = jsonObject.getJSONObject("keys");
        int k = keys.getInt("k");

        Map<Integer, BigInteger> points = new HashMap<>();
        for (String key : jsonObject.keySet()) {
            if (!key.equals("keys")) {
                JSONObject pointData = jsonObject.getJSONObject(key);
                int x = Integer.parseInt(key);
                String value = pointData.getString("value");
                int base = Integer.parseInt(pointData.getString("base"));
                BigInteger y = new BigInteger(value, base);
                points.put(x, y);
            }
        }

        return lagrangeInterpolation(points, k);
    }

    public static BigInteger lagrangeInterpolation(Map<Integer, BigInteger> points, int k) {
        BigInteger secret = BigInteger.ZERO;
        for (int i = 1; i <= k; i++) {
            BigInteger numerator = points.get(i);
            BigInteger denominator = BigInteger.ONE;
            for (int j = 1; j <= k; j++) {
                if (i != j) {
                    numerator = numerator.multiply(BigInteger.valueOf(-j));
                    denominator = denominator.multiply(BigInteger.valueOf(i - j));
                }
            }
            secret = secret.add(numerator.divide(denominator));
        }
        return secret;
    }
}
package org.example;

import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.source.tree.WhileLoopTree;
import kotlin.collections.ArrayDeque;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Requests {

    private static final String API_KEY = loadApiKey();
    private static final String BASE_URL = "https://rest.coinapi.io/v1/exchangerate";
    private String formattedNumber;

    private static String loadApiKey() {
        try (InputStream in = Requests.class.getResourceAsStream("/config.properties")) {
            Properties props = new Properties();
            props.load(in);
            return props.getProperty("coinapi.key");
        } catch (Exception e) {
            throw new RuntimeException("Could not load coinapi.key from config.properties", e);
        }
    }

    public String request(String c1,String c2,Double amount){
        String baseCurrency = c1;
        String quoteCurrency = c2;

        String url = BASE_URL + "/" + baseCurrency + "/" + quoteCurrency;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-CoinAPI-Key", API_KEY)
                .build();



        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Request failed: " + response);
                JOptionPane.showMessageDialog(null, "Please insert valid currency code.", "Error", JOptionPane.ERROR_MESSAGE);
                return " ";
            }

            String responseData = response.body().string();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseData);

            double rateD = jsonNode.get("rate").asDouble();
            if(amount!=null){
                rateD*=amount;//MULTIPLYING BY AMOUNT
                BigDecimal rate = new BigDecimal(rateD);
                formattedNumber = null;
                if(rateD<1){
                    DecimalFormat df = new DecimalFormat("#.######");
                    formattedNumber = df.format(rate);
                }else{
                    DecimalFormat df = new DecimalFormat("#.##");
                    formattedNumber = df.format(rate);
                }
            }


            //System.out.println("Exchange Rate: " + amount + " " + baseCurrency + " = " + formattedNumber + " " + quoteCurrency);



        } catch (IOException e) {
            e.printStackTrace();
        }

        return formattedNumber;

    }
    public static List<String> getAllCoinSymbols() {
        List<String> coins = new ArrayList<>();
        try {
            URL url = new URL("https://rest.coinapi.io/v1/assets");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-CoinAPI-Key", API_KEY);

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream())
                );
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONArray jsonArray = new JSONArray(response.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String assetId = obj.getString("asset_id");
                    coins.add(assetId);
                }

            } else {
                System.out.println("Error: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return coins;
    }

}

package net.thesieutoc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.stream.Collectors;

public class TheSieuTocAPI {
    private static final String API_SERVER = "https://thesieutoc.net";
    private static final String TRANSACTION = "API/transaction";
    private static final String CARD_CHARGING= "card_charging_api/check-status.html";

    public static JsonObject sendCard(String APIKey, String APISecret, String pin, String serial, CardType type, CardAmount amount) throws IOException {
        final String url = MessageFormat.format(
                "{0}/{1}?APIkey={2}&APIsecret={3}&mathe={4}&seri={5}&type={6}&menhgia={7}",
                API_SERVER, TRANSACTION, APIKey, APISecret, pin, serial, type, amount.getCode());
        return sendRequest(url);
    }

    public static JsonObject checkCard(String APIKey, String APISecret, String transactionID) throws IOException {
        final String url = MessageFormat.format("{0}/{1}?APIkey={2}&APIsecret={3}&transaction_id={4}",
                API_SERVER, CARD_CHARGING, APIKey, APISecret, transactionID);
        return sendRequest(url);
    }

    private static JsonObject sendRequest(String url) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setReadTimeout(36*10^5);
        connection.setDoInput(true);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        final String response = reader.lines().collect(Collectors.joining());
        return new JsonParser().parse(response).getAsJsonObject();
    }
}
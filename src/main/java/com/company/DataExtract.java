package com.company;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataExtract {

    String readData(HttpURLConnection httpURLConnection) throws IOException {
        StringBuilder content = new StringBuilder();
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            while ((inputLine = input.readLine()) != null)
            {
                content.append(inputLine);
            }
            input.close();

        return content.toString();
    }

    ArrayList<String> getLinks(String data) {

        String initialURL = "http://localhost:5000";
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(data).getAsJsonObject().get("link");
        ArrayList<String> links = new ArrayList<>();
        Gson gson = new Gson();
        String json;
        if (jsonElement != null) {
            json = jsonElement.toString();
        } else return null;
        Map<String, Object> map = new HashMap<>();
        map = (Map<String, Object>) gson.fromJson(json, map.getClass());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            links.add(initialURL + entry.getValue());
        }
        return links;
    }

    String getData(String data) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(data).getAsJsonObject().get("data");
        if (jsonElement != null) {
            return jsonElement.getAsString();
        } else return null;
    }

    String getType(String data) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(data).getAsJsonObject().get("mime_type");
        if (jsonElement == null) {
            return "json";
        }
        return jsonElement.getAsString();
    }
}

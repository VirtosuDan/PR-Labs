package com.company;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class AccessTokenGetRequest {
    private static String accessToken = "";
    private static final String RegisterURL = "http://localhost:5000/register";

    public String getAccessToken() {
        return accessToken;
    }
    void requestAccessToken() throws IOException {
        DataExtract requestData = new DataExtract();
            String data;
            URL url = new URL(RegisterURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            int statuscode = httpURLConnection.getResponseCode();
            if (statuscode != 200)
                throw new RuntimeException("httpURLConnection: " + statuscode);
            else {
                data = requestData.readData(httpURLConnection);
                JsonParser parser = new JsonParser();
                accessToken = parser.parse(data).getAsJsonObject().get("access_token").getAsString();
            }
    }

}

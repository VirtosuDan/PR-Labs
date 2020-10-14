package com.company;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MultithreadingRequest {

    private static final ArrayList<String> links = new ArrayList<>();
    private static final String initialURl = "http://localhost:5000/home";
    private static int index = 0;
    private static final ArrayList<String> dataList = new ArrayList<>();
    private static volatile int activeThreads = 0;
    private static final int serverPort = 5001;

    public ArrayList<String> getDataList() {    
        return dataList;
    }

    public static void main(String[] args) throws IOException {

        AccessTokenGetRequest requestAccessToken = new AccessTokenGetRequest();
        requestAccessToken.requestAccessToken();
        new ThreadRequest(initialURl).start();
        while (true) {
            if (activeThreads == 0) {
                System.out.println("Enter in telnet port " + serverPort);
                ConcurrentTCPServer server = new ConcurrentTCPServer();
                server.start(serverPort);
                break;
            }
        }
    }

    static class ThreadRequest extends Thread {
        private final String url;

        ThreadRequest(String url) {
            this.url = url;
            activeThreads++;
        }

        @Override
        public void run() {
            DataExtract extractData = new DataExtract();
            AccessTokenGetRequest requestAccessToken = new AccessTokenGetRequest();
            try {
                URL siteURL = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) siteURL.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("X-Access-Token", requestAccessToken.getAccessToken());
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.connect();
                int code = httpURLConnection.getResponseCode();
                if (code == 200) {
                    String data = extractData.readData(httpURLConnection);
                    if (extractData.getLinks(data) != null)
                        links.addAll(extractData.getLinks(data));
                    System.out.println(url + "\t\tStatusCode:" + code + "     type = " + extractData.getType(data));
                    if (extractData.getData(data) != null) {
                        convert(extractData.getType(data), extractData.getData(data));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (links != null) {
                for (int i = index; i < links.size(); i++) {
                    new ThreadRequest(links.get(i)).start();
                    index++;
                }
            }
            activeThreads--;

        }

        void convert(String type, String data) throws IOException {
            DataConvertor convertor = new DataConvertor();
            if(type.equals("application/xml")){
                dataList.add(convertor.convertXMLtoJSON(data));
            }else if(type.equals("text/csv")){
                dataList.add(convertor.convertCSVtoJSON(data));
            }else if(type.equals("application/x-yaml")){
                dataList.add(convertor.convertYamlToJson(data));
            }else if(type.equals("json")){
                dataList.add(data);
            }else
                System.out.println("default type");
        }
    }
}

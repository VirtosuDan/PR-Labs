package com.company;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

class ConcurrentTCPServer {
    private static final String commands = "\nInformation:\n id / email / first_name / last_name ";
    private static final String instructions1 = "\nExample:Enter id   to see all id columns";
    private static final String instructions2 = "\nExample:Enter id 1 to see all json with id 1\n ";

    public void start(int port) throws IOException{
            TraverseJson parseJson = new TraverseJson();
            MultithreadingRequest multithreadingRequest = new MultithreadingRequest();
            ServerSocket serverSocket = new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ArrayList<String> arrayList = multithreadingRequest.getDataList();
            ArrayList<String> output;
            String inputLine;
            boolean stop = true;
            out.println(commands);
            out.println(instructions1);
            out.println(instructions2);
            while ((inputLine = in.readLine()) != null && stop) {
                ArrayList<String> words = new ArrayList<>(Arrays.asList(inputLine.split(" ")));
                switch (words.size()) {
                    case 1: {
                        if ("exit".equals(inputLine)||"stop".equals(inputLine)) {
                            out.println("Good Bye");
                            stop = false;

                        } else {
                            output = parseJson.getValuesForGivenKey(arrayList, inputLine);
                            for (String outputElement : output) {
                                out.println(outputElement);
                            }
                            out.println(commands);
                            out.println(instructions1);
                            out.println(instructions2);
                        }
                        break;
                    }
                    case 2: {
                        output = parseJson.getJsonForGivenValue(arrayList, words.get(0), words.get(1));
                        for (String outputElement : output) {
                            out.println(outputElement);
                        }
                        out.println(commands);
                        out.println(instructions1);
                        out.println(instructions2);
                        break;
                    }
                    default: {
                        out.println("Unknown command!");
                        out.println(commands);
                        out.println(instructions1);
                        out.println(instructions2);
                    }
                }
            }
    }
}
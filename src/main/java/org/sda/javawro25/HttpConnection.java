package org.sda.javawro25;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;

public class HttpConnection {

    public static void main(String[] args) throws IOException {
        URL url = new URL("https://restcountries.eu/rest/v2/all");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int status = con.getResponseCode();
        System.out.println(status);

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder contentBuilder = new StringBuilder();
        while ((inputLine = bufferedReader.readLine()) != null) {
            contentBuilder.append(inputLine);
        }
        bufferedReader.close();
        con.disconnect();
        System.out.println(contentBuilder.toString());

        Gson gson = new Gson();
        Country[] countries = gson.fromJson(contentBuilder.toString(), Country[].class);

        Arrays.stream(countries).forEach(System.out::println);

//        for(Country country: countries){
//            System.out.println(country);
//        }

    }

}

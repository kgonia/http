package org.sda.javawro25;

import com.google.gson.Gson;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CepikHttp {

    public static void main(String[] args) throws IOException {

        // Trust Manager który ufa wszyskim certyfikatom
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Instalacja managera
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }


        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("wojewodztwo", "02");
        parameters.put("data-od", "20190101");
        parameters.put("typ-daty", "1");
        parameters.put("tylko-zarejestrowane", "true");
        parameters.put("pokaz-wszystkie-pola", "false");
        parameters.put("limit", "100");
        parameters.put("page", "1");

        URL url = new URL("https://api.cepik.gov.pl/pojazdy?" + ParameterStringBuilder.getParamsString(parameters));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("accept", "application/json");
        con.setRequestMethod("GET");

        con.connect();

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
        CepikResponse cepikResponse = gson.fromJson(contentBuilder.toString(), CepikResponse.class);

        for(Car car: cepikResponse.getData()){
            System.out.println(car.getId());
            System.out.println(car.getAttributes().getMarka());
        }

    }

}

package com.sourcode.catalogo_borrar01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SourCode on 17/12/2014.
 */
public class HttpManager {

    public static String getData(String uri){
        BufferedReader reader = null;
        try{

            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // con.setRequestMethod("GET");
            // con.connect();
            // int statusCode = con.getResponseCode();

            // if (statusCode == 200) {
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                // Read code one line at a time
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                return sb.toString();
            // }else{
                // return "Error " + statusCode;
            // }

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

    }

}

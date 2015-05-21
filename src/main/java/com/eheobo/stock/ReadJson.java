package com.eheobo.stock;

import com.oracle.javafx.jmx.json.JSONException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by yu on 15-5-19.
 */
public class ReadJson {

    public static String getJson(String url)  {
        try (InputStream is = new URL(url).openStream();
             BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")))
        ) {
            String jsonText = readAll(rd);
            return jsonText;
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }


}

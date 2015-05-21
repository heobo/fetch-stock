package com.eheobo.stock;

import com.eheobo.stock.module.StockDate;
import com.ifunpay.util.jackson.JsonUtil;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yu on 15-5-19.
 */
public class Main {

    static Logger logger = Logger.getLogger(Main.class);
    static MongoClient mongoClient = new MongoClient("localhost", 27017);
    static MongoDatabase db = mongoClient.getDatabase("stock");
    static MongoCollection<Document> collection = db.getCollection("date");
    static MongoCollection<Document> errorCo = db.getCollection("error");

    public static void main(String... args) {
        String listUrl = "http://ctxalgo.com/api/stocks";
        String json = ReadJson.getJson(listUrl);
        Map<String, Object> map = JsonUtil.toObject(json);
        map.keySet().parallelStream().forEach(key -> {
            Object name = map.get(key);
            try {
                String code = genCode(key);
                saveStock(code, name.toString());
            } catch (Exception e) {
                logger.warn(key + " FAILED");
                errorCo.insertOne(new Document().append("time", new Date())
                                .append("code", key)
                                .append("name", name.toString())
                );
            }
        });
    }

    private static String genCode(String key) {
        if (key.startsWith("sz")) {
            return key.substring(2) + ".sz";
        } else if (key.startsWith("sh")) {
            return key.substring(2) + ".ss";
        } else {
            throw new RuntimeException("");
        }
    }


    public static void saveStock(String stockCode, String stockName) {

        String csvFile = "/Users/yu/Downloads/sz000001.csv";
        String line;
        String cvsSplitBy = ",";
        URL url;
        try {
            url = new URL("http://table.finance.yahoo.com/table.csv?s=" + stockCode);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        //BufferedReader br = new BufferedReader(new FileReader(csvFile));
        List<Document> documentList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            br.readLine(); // ignore first line
            while ((line = br.readLine()) != null) {
                StockDate stockDate = new StockDate();
                String[] stock = line.split(cvsSplitBy);
                int i = 0;
                stockDate.id = stockCode + stock[i];
                stockDate.date = simpleDateFormat.parse(stock[i++]);
                stockDate.open = getInt(stock[i++]);
                stockDate.high = getInt(stock[i++]);
                stockDate.low = getInt(stock[i++]);
                stockDate.close = getInt(stock[i++]);
                stockDate.volume = getLong(stock[i++]);
                stockDate.adjclose = getInt(stock[i++]);
                stockDate.code = stockCode;
                stockDate.name = stockName;
                documentList.add(stockDate.toDocument());
            }
            collection.insertMany(documentList);
            System.out.println(stockCode + " Done");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }

    public static long getLong(String cent) {
        return Math.round(Double.valueOf(cent) * 100);
    }

    public static int getInt(String cent) {
        return (int) getLong(cent);
    }
}

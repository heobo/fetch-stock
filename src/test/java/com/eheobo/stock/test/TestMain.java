package com.eheobo.stock.test;

import com.ifunpay.util.common.DateUtil;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.function.Consumer;

/**
 * Created by yu on 15-5-19.
 */
public class TestMain {

    static MongoClient mongoClient = new MongoClient("localhost", 27017);
    static MongoDatabase db = mongoClient.getDatabase("stock");
    static MongoCollection<Document> collection = db.getCollection("date");

    public static void main(String... args) throws Exception{
        collection.find(
                new Document("code", "000100.sz")
                        .append("close", new Document("$gt", 650))
                        .append("date", new Document("$gt", DateUtil.parse("20150101", "yyyyMMdd")))
        ).forEach((Consumer) System.out::println);
    }
}

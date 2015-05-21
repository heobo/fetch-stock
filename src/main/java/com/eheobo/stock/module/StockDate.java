package com.eheobo.stock.module;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * Created by yu on 15-5-19.
 */
public class StockDate {


//    private static JacksonDBCollection<StockDate, String> coll = MongoDB.getCollection("date", StockDate.class, String.class);

    //@Id
    //@ObjectId
    public String id;

    public String code;
    public Date date;
    public int open;
    public int high;
    public int low;
    public int close;
    public long volume;
    public int adjclose;
    public String name;

    public Document toDocument(){
        return new Document().append("code", code)
                .append("date", date)
                .append("open", open)
                .append("high", high)
                .append("low", low)
                .append("close", close)
                .append("volume", volume)
                .append("adjclose", adjclose)
                .append("name", name);
    }

//
//    public static List<StockDate> all() {
//        return coll.find().toArray();
//    }
//
//    public static List<StockDate> find(StockDate query){
//        return coll.find(query).toArray();
//    }
//
//    public static void create(StockDate stockDate) {
//        coll.save(stockDate);
//    }
//
//    public static void delete(String id) {
//        StockDate stockDate = coll.findOneById(id);
//        if (stockDate != null)
//            coll.remove(stockDate);
//    }
//
//    public static void removeAll() {
//        coll.drop();
//    }
}

package com.example.demo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mongodb.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

@RestController
public class RSSDisplay{
        DB dB=(new MongoClient("localhost",27017)).getDB("RSS_TEST");
        @RequestMapping("/show")
        public BasicDBList driver(@RequestParam String choice )throws UnknownHostException  {
            BasicDBList collectionS=null;
            if(choice.equalsIgnoreCase("all")) return displayAll(dB);
            else if(dB.collectionExists(choice)) return  displaySome(choice,dB);
            return collectionS;
        }


        public static BasicDBList displayAll(DB dB) throws UnknownHostException{
            BasicDBList collectionS=new BasicDBList();
            Set<String> collectionNames =dB.getCollectionNames();
            for (String category : collectionNames){
                DBCollection channeldBCollection=dB.getCollection(category);
                DBCursor dbCursor=channeldBCollection.find();
                while(dbCursor.hasNext()) {
                    collectionS.add(dbCursor.next());
                }
            }
            return collectionS;
        }


        public static BasicDBList displaySome(String category,DB dB)throws NullPointerException{
            BasicDBList collectionS=new BasicDBList();
            DBCollection channeldBCollection=dB.getCollection(category);
            DBCursor dbCursor=channeldBCollection.find();
            if(dbCursor.count()>0){
                while(dbCursor.hasNext()) {
                    collectionS.add(dbCursor.next());
                }
            }
            else System.out.println("\nNo Data Exists");
            return collectionS;
        }
    }

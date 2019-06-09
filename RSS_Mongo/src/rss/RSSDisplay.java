package rss;
import com.mongodb.*;
import jdk.jfr.Category;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

public class RSSDisplay {
    public static  void  main(String[]args)throws UnknownHostException,java.io.IOException{
        DB dB=(new MongoClient("localhost",27017)).getDB("RSS_TEST");
        Set<String> collectionNames =dB.getCollectionNames();
        System.out.println("\nCategories are: ");
        for (String name : collectionNames)
            System.out.println("\n"+name);
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nWhich Category do you want to view?/all category ?or exit: ");
        String category=bufferedReader.readLine();
        while(!(category.equals("exit"))){
            if(dB.collectionExists(category))  displaySome(category,dB);
            else if(category.equalsIgnoreCase("all")) displayAll(dB);
            else System.out.println("Invalid Category..");
            System.out.println("\nWhich Category do you want to view?/all category ?or exit: ");
            category=bufferedReader.readLine();
        }
    }

    public static void displayAll(DB dB) throws UnknownHostException{
        Set<String> collectionNames =dB.getCollectionNames();
        for (String name : collectionNames){
            displaySome(name,dB);
        }
    }


    public static void displaySome (String category,DB dB){
        DBCollection channeldBCollection=dB.getCollection(category);
        DBCursor dbCursor=channeldBCollection.find();
        if(dbCursor.count()>0){
            System.out.println("\nData for "+category+" are:");
            while(dbCursor.hasNext()) System.out.println(dbCursor.next());
        }
        else System.out.println("\nNo Data Exists");
    }
}

package rss;

import com.mongodb.*;
import java.io.*;
import java.net.*;
import java.net.UnknownHostException;

public class RSSReader {
    public static void main(String[] args)throws UnknownHostException,MalformedURLException,IOException{
        call();
    }
    public static void call()throws MalformedURLException,IOException{
        readRSS("https://www.economist.com/science-and-technology/rss.xml","Science and Technology");
        readRSS("https://www.economist.com/asia/rss.xml","Asia");
        readRSS("https://www.economist.com/china/rss.xml","China");
        readRSS("https://www.economist.com/business/rss.xml","Business");
        readRSS("https://www.economist.com/leaders/rss.xml","Leaders");
    }

    public static void readRSS(String urlAddress,String Category)throws MalformedURLException,IOException{
        URL rssUrl=new URL(urlAddress);
        BufferedReader in=new BufferedReader(new InputStreamReader(rssUrl.openStream()));
        String line;
        boolean flag=false;
        int count=0;
        int maxcount=5;
        DB dB=(new MongoClient("localhost",27017)).getDB("RSS_TEST");
        DBCollection dBCollection=dB.getCollection(Category);
        dBCollection.drop();
        BasicDBObject basicDBObject=new BasicDBObject();
        while ((line=in.readLine())!=null&&count<maxcount){
            if(line.contains("<item>")){
                basicDBObject=new BasicDBObject();
                flag=true;
            }
            if(line.contains("<title>")&&flag){
                String temp=line;
                temp=temp.replace("<title>","");
                temp=temp.replace("</title>","");
                basicDBObject.put("title",temp);
            }
            if(line.contains("<link>")&&flag){
                String temp=line;
                temp=temp.replace("<link>","");
                temp=temp.replace("</link>","");
                String Genre=getGenre(temp);
                basicDBObject.put("link",temp);
                basicDBObject.put("genre",Genre);

            }
            if(line.contains("<description>")&&flag){
                String temp=line;
                temp=temp.replace("<description>","");
                temp=temp.replace("</description>","");
                basicDBObject.put("description",temp);

            }
            if(line.contains("<pubDate>")&&flag){
                String temp=line;
                temp=temp.replace("<pubDate>","");
                temp=temp.replace("</pubDate>","");
                basicDBObject.put("dop",temp);
                dBCollection.insert(basicDBObject);
                flag=false;
                count++;
            }

        }
        System.out.println("\nData of "+Category+" inserted sucessfully");
        in.close();
    }

    public static String getGenre(String urlAddress)throws MalformedURLException,IOException{
        URL rssUrl=new URL(urlAddress);
        BufferedReader in=new BufferedReader(new InputStreamReader(rssUrl.openStream()));
        String line;
        while ((line=in.readLine())!=null){
            if(line.contains("<title data-react-helmet=\"true\">")){
                String temp=line.replace("<title data-react-helmet=\"true\">","");
                int fpos=temp.lastIndexOf("-");
                int lpos=temp.indexOf("</title>");
                temp=temp.substring(fpos+1,lpos);
                return temp;
            }
        }
        in.close();
        return null;
    }
}

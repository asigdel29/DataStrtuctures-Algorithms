import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Utils
{
    public static String getWebPageBody(String url)
    {
        try
        {
            Document doc = Jsoup.connect(url).get();
            Element body = doc.body();
            return body == null ? null : body.text().replaceAll("[^a-zA-Z ]", "").toLowerCase();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static List<String> getSubLinks(String base)
    {
        List<String> linkList = new ArrayList<>();
        try
        {
            Document doc = Jsoup.connect(base).get();
            Elements links = doc.select("a[href]");

            for(Element link : links)
                linkList.add(link.attr("abs:href"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return linkList;
    }
}
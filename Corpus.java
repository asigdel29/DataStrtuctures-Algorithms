import java.util.ArrayList;

import data_structures.btree.CustomBTree;


public class Corpus extends ArrayList<CustomUrl>
{
    private static final long serialVersionUID = -5811919797076472772L;

    private CustomUrl primaryUrl;

    public int getTotalDocsContainingTerm(String term)
    {
        int total = 0;

        for(CustomUrl url : this)
            if(url.getFreqTable().containsKey(term))
                total++;

        return total;
    }

    public void setPrimaryUrl(CustomUrl url)
    {
        primaryUrl = url;
    }

    public CustomUrl getPrimaryUrl()
    {
        return primaryUrl;
    }
}
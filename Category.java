import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import data_structures.btree.CustomBTree;
import data_structures.btree.Value;


public enum Category
{
    DISCRETEMATH(" https://en.wikipedia.org/wiki/Discrete_mathematics"),
    HASHTABLE("https://en.wikipedia.org/wiki/Hash_table"),
    HASH_FUNCTIONS("https://en.wikipedia.org/wiki/Hash_function"),
    BTREE("https://en.wikipedia.org/wiki/B-tree"),
    TFIDF("https://en.wikipedia.org/wiki/Tf%E2%80%93idf,"),
    BITARRAY("https://en.wikipedia.org/wiki/Bit_array"),
    DATA_STRUCTURES("https://en.wikipedia.org/wiki/Data_structure");


    private static final int MAX_SUB_LINKS = 10 ;

    private String parentUrl;
    private Corpus corpus;
    private CustomBTree bTree;

    Category(String parentUrl)
    {
        this.parentUrl = parentUrl;
        this.corpus = new Corpus();
        bTree = new CustomBTree(parentUrl.replaceAll("/", ""));
        if(needsUpdate(bTree.getLastModifiedRaf()))
        {
            bTree.removeRaf();
            loadUrlsIntoCorpus();
            addWordsIntoCorpus();
            calculateTfIdf();
            loadIntoBTree();
        }
    }

    private void loadIntoBTree()
    {
        for(CustomUrl url : corpus)
        {
            for(String word : url.getFreqTable().keySet())
            {
                Value toInsert = new Value(url.getUrl(), url.getFreqTable().get(word).getTfIdf());
                bTree.put(word, toInsert);
            }
        }
    }

    private void calculateTfIdf()
    {
        for(CustomUrl url : corpus)
            url.getFreqTable().calculate();
    }

    private void loadUrlsIntoCorpus()
    {
        corpus.add(new CustomUrl(parentUrl, corpus));
        List<String> links = Utils.getSubLinks(parentUrl);
        Collections.shuffle(links);

        for(String link : links)
        {
            try
            {
                if(link.getBytes("UTF-32BE").length >= 256)
                {
                    continue;
                }

                if(corpus.size() >= MAX_SUB_LINKS)
                    break;

                CustomUrl newUrl = new CustomUrl(link, corpus);

                if(corpus.contains(newUrl))
                {
                    continue;
                }

                System.out.println(link);
                corpus.add(newUrl);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void addWordsIntoCorpus()
    {
        for(CustomUrl url : corpus)
        {
            try
            {
                String body = Utils.getWebPageBody(url.getUrl());
                String[] bodyParts = body != null ? body.split(" ") : null;

                if(bodyParts == null)
                    continue;

                for(String s : bodyParts)
                {
                    if(s.length() == 0)
                        continue;
                    if(s.getBytes("UTF-32BE").length >= 256)
                    {
                        continue;
                    }

                    url.getFreqTable().addWord(s);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public String getParentUrl()
    {
        return parentUrl;
    }

    public CustomBTree getBTree()
    {
        return bTree;
    }

    private boolean needsUpdate(Date d)
    {
        if(d == null)
            return true;

        LocalDateTime ldt = LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now();

        return ldt.plusWeeks(1).isBefore(now);
    }

    public void parseUrl(String url, String words)
    {
        CustomUrl customUrl = new CustomUrl(url, corpus);

        for(String word : words.split(" "))
            customUrl.getFreqTable().addWord(word);

        corpus.add(customUrl);
    }
}
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import data_structures.CustomHashTable;
import data_structures.btree.CustomBTree;
import data_structures.btree.Value;


public class Application
{
    private static final int NUM_SUGGESTIONS = 5;

    private String url;
    private Set<String> words;
    private Category category;
    private String[] suggestions;
    private CustomHashTable<Category, List<Suggestion>> linkOccurences;
    private List<String> exclusions = Arrays.asList("and", "or", "to", "the");

    public Application(String url)
    {
        this.url = url;
        this.words = new HashSet<>();
        this.linkOccurences = new CustomHashTable<>();
        this.suggestions = new String[NUM_SUGGESTIONS];
    }

    public void execute() throws ExceptionInInitializerError
    {
        try
        {
            addWords();
            System.out.println("Added " + words.size() + " words");

            Category mostSimilar = null;
            double highestSum = -1;

            for(Category c : Category.values())
            {
                double toCompare = sumTfIdf(c, c.getBTree());
                if(toCompare > highestSum)
                {
                    mostSimilar = c;
                    highestSum = toCompare;
                }

                System.out.println("Checking category " + c);
                System.out.println("TfIdf for category " + c + ": " + toCompare);
            }

            category = mostSimilar;

            List<Suggestion> suggestionsList = linkOccurences.get(category);
            Collections.sort(suggestionsList);

            for(int i = 0; i < suggestions.length; i++)
                suggestions[i] = suggestionsList.get(i).link + " - " + suggestionsList.get(i).amount;


            System.out.println("Most similar category: " + mostSimilar);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private double sumTfIdf(Category c, CustomBTree bTree)
    {
        double sum = 0;

        for(String word : words)
        {
            Value[] vals = bTree.get(word);

            if(vals == null)
                continue;

            for(Value v : vals)
            {
                if(v == null)
                    continue;

                sum += v.getTfIdf();
                List<Suggestion> currentSuggestions = linkOccurences.get(c);
                if(currentSuggestions == null)
                    currentSuggestions = new ArrayList<>();

                Suggestion s = null;
                for(Suggestion s1 : currentSuggestions)
                {
                    if(s1.link.equals(v.getUrl()))
                    {
                        s = s1;
                        break;
                    }
                }

                if(s == null)
                    currentSuggestions.add(new Suggestion(v.getUrl(), 1));
                else
                    s.amount++;

                linkOccurences.put(c, currentSuggestions);
            }
        }

        return sum;
    }

    private void addWords()
    {
        try
        {
            System.out.println("Adding words from url: " + url);
            String body = Utils.getWebPageBody(url);

            String[] bodyParts = body != null ? body.split(" ") : null;

            if(bodyParts == null)
            {
                System.out.println("ERROR");
                return;
            }

            for(String s : bodyParts)
            {
                if(s.length() == 0)
                    continue;
                if(exclusions.contains(s))
                    continue;

                words.add(s);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public Category getCategory()
    {
        return category;
    }

    public String[] getSuggestions()
    {
        return suggestions;
    }

    private class Suggestion implements Comparable<Suggestion>
    {
        String link;
        int amount;

        public Suggestion(String link, int amount)
        {
            this.link = link;
            this.amount = amount;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((link == null) ? 0 : link.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Suggestion other = (Suggestion) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (link == null)
            {
                if (other.link != null)
                    return false;
            } else if (!link.equals(other.link))
                return false;
            return true;
        }

        @Override
        public int compareTo(Suggestion o)
        {
            return o.amount - amount;
        }

        private Application getOuterType()
        {
            return Application.this;
        }
    }
}
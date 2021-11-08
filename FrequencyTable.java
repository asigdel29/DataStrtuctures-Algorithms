import data_structures.CustomHashTable;

public class FrequencyTable extends CustomHashTable<String, Word>
{
    private int maxRawFrequency;
    private Corpus corpus;

    public FrequencyTable(Corpus corpus)
    {
        this.corpus = corpus;
    }

    public void addWord(String word)
    {
        Word wordEntry = get(word);

        if(wordEntry == null)
            put(word, new Word());
        else
            wordEntry.incrementRawFrequency();
    }

    public void calculate()
    {
        for(String key : keySet())
        {
            Word word = get(key);
            if(word != null)
                word.setTfIdf(calculateTfIdf(key));
        }
    }

    private double calculateTfIdf(String word)
    {
        return calculateTermFreq(word) * calculateInverseDocFreq(word);
    }

    private double calculateInverseDocFreq(String word)
    {
        return Math.log((double)corpus.size() / (1 + corpus.getTotalDocsContainingTerm(word)));
    }

    private double calculateTermFreq(String word)
    {
        Word wordEntry = get(word);

        int rawFreq = wordEntry == null ? 0 : wordEntry.getRawFrequency();

        if(rawFreq > maxRawFrequency)
            maxRawFrequency = rawFreq;

        return 0.5 + (0.5 * (rawFreq / maxRawFrequency));
    }
}
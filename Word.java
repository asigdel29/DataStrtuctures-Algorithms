public class Word
{
    private int rawFrequency;
    private double tfIdf;

    public Word()
    {
        rawFrequency = 1;
    }

    public int getRawFrequency()
    {
        return rawFrequency;
    }

    public void incrementRawFrequency()
    {
        rawFrequency++;
    }

    public double getTfIdf()
    {
        return tfIdf;
    }

    public void setTfIdf(double d)
    {
        tfIdf = d;
    }
}
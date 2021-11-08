public class CustomUrl
{
    private String url;
    private FrequencyTable freqTable;

    public CustomUrl(String url, Corpus corpus)
    {
        this.url = fixInput(url);
        this.freqTable = new FrequencyTable(corpus);
    }

    public static String fixInput(String url)
    {
        final boolean CONTAINS_HTTP = url.contains("http");

        if(!url.contains("www.") && !CONTAINS_HTTP)
            url = "www." + url;
        if(!CONTAINS_HTTP)
            url = "http://" + url;

        return url;
    }

    public String getUrl()
    {
        return url;
    }

    public FrequencyTable getFreqTable()
    {
        return freqTable;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
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
        CustomUrl other = (CustomUrl) obj;
        if (url == null)
        {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }


}
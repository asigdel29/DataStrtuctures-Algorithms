package data_structures.btree;

import java.io.UnsupportedEncodingException;

public class Value
{
    private String url;
    private double tfIdf;

    public Value(String url, double tfIdf)
    {
        this.url = url;
        this.tfIdf = tfIdf;
    }

    public String getUrl()
    {
        return url;
    }

    public double getTfIdf()
    {
        return tfIdf;
    }

    public int getUtfLength()
    {
        try
        {
            return url.getBytes("UTF-32BE").length;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return 256;
    }

    public byte[] getUrlUtf()
    {
        try
        {
            byte[] bytes = new byte[256];
            byte[] keyBytes = url.getBytes("UTF-32BE");

            for(int i = 0; i < keyBytes.length; i++)
                bytes[i] = keyBytes[i];

            return bytes;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
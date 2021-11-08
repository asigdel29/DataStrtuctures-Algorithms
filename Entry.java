package data_structures.btree;

import java.io.UnsupportedEncodingException;


public class Entry implements Comparable<Entry>
{
    private static final int KEY_BLOCK_SIZE = 256; //256 bytes = 64 characters UTF-32
    public static final int MAX_CHILDREN = 10;
    private static final int VALUE_BLOCK_SIZE = (MAX_CHILDREN * 256) + (MAX_CHILDREN * 8) + (MAX_CHILDREN * 4); //vals contain both URL and 8 byte double value for TF-IDF
    public static final int TOTAL_BLOCK_SIZE = KEY_BLOCK_SIZE + VALUE_BLOCK_SIZE;

    private String key;
    private Value[] values = new Value[MAX_CHILDREN];

    public Entry(String key, Value value)
    {
        this.key = key;
        this.values[0] = value;
    }

    public Entry(String key, Value[] vals)
    {
        this.key = key;
        this.values = vals;
    }

    public Entry(Entry e)
    {
        this.key = e.getKey();
        this.values = e.getValues();
    }

    @Override
    public int compareTo(Entry o)
    {
        return key.compareTo(o.getKey());
    }

    public String getKey()
    {
        return key;
    }

    public void setValues(Value[] values)
    {
        this.values = values;
    }

    public int getKeyUtfLength()
    {
        try
        {
            return key.getBytes("UTF-32BE").length;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return 128;
    }

    public byte[] getKeyUtf()
    {
        try
        {
            byte[] bytes = new byte[256];
            byte[] keyBytes = key.getBytes("UTF-32BE");

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

    public Value[] getValues()
    {
        return values;
    }

    public void setKey(String key)
    {
        this.key = key;
    }
}
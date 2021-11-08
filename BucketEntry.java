package data_structures;

public class BucketEntry<K, V>
{
    private K key;
    private V value;

    public BucketEntry(K key, V value)
    {
        this.key = key;
        this.value = value;
    }

    public K getKey()
    {
        return key;
    }

    public V getValue()
    {
        return value;
    }

    public void setValue(V val)
    {
        value = val;
    }
}
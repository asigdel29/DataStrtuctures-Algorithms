package data_structures;

import java.util.LinkedList;

public class Bucket<K, V>
{
    private LinkedList<BucketEntry<K, V>> entries;

    public Bucket(BucketEntry<K, V> entry)
    {
        entries = new LinkedList<>();
        entries.add(entry);
    }

    public void add(BucketEntry<K, V> entry)
    {
        entries.add(entry);
    }

    public V get(K key)
    {
        for(BucketEntry<K, V> entry : entries)
            if(entry.getKey().equals(key))
                return entry.getValue();

        return null;
    }

    public BucketEntry<K, V> findEntry(K key)
    {
        for(BucketEntry<K, V> entry : entries)
            if(entry.getKey().equals(key))
                return entry;

        return null;
    }

    public LinkedList<BucketEntry<K, V>> getEntries()
    {
        return entries;
    }
}
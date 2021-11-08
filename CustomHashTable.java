package data_structures;

import java.util.ArrayList;
import java.util.List;


public class CustomHashTable<K, V>
{
    private BucketList<K, V> buckets;

    public CustomHashTable()
    {
        buckets = new BucketList<K, V>();
    }

    public V get(K key)
    {
        int index = computeIndex(key);

        if(index >= buckets.size())
            return null;

        Bucket<K, V> bucket = buckets.get(index);
        return bucket == null ? null : bucket.get(key);
    }

    public void put(K key, V value)
    {
        int index = computeIndex(key);
        buckets.add(index, new BucketEntry<K, V>(key, value));
    }

    public boolean containsKey(K key)
    {
        return get(key) != null;
    }

    public List<K> keySet()
    {
        List<K> keys = new ArrayList<>();

        for(Bucket<K, V> bucket : buckets)
        {
            if(bucket == null)
                continue;

            for(BucketEntry<K, V> entry : bucket.getEntries())
            {
                if(entry == null)
                    continue;

                keys.add(entry.getKey());
            }
        }

        return keys;
    }


    private int computeIndex(K key)
    {
        return buckets.size() == 0 ? 0 : Math.abs(computeHash(key) % buckets.size());
    }

    private int computeHash(K key)
    {
        return key.hashCode();
    }

}
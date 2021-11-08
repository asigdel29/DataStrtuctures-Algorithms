package data_structures;

import java.util.LinkedList;

public class BucketList<K, V> extends LinkedList<Bucket<K, V>>
{
    private static final long serialVersionUID = 795688579905882920L;

    private final int NUM_BUCKETS = 997;

    public BucketList()
    {
        initializeBuckets();
    }

    public void add(int index, BucketEntry<K, V> entry)
    {
        Bucket<K, V> bucket = get(index);

        if(bucket == null)
            set(index, new Bucket<>(entry));
        else
        {
            BucketEntry<K, V> current = bucket.findEntry(entry.getKey());

            if(current != null)
                current.setValue(entry.getValue());
            else
                bucket.add(entry);
        }
    }

    private void initializeBuckets()
    {
        for(int i = 0; i < NUM_BUCKETS; i++)
            add(null);
    }
}
package data_structures.btree;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

import data_structures.CustomHashTable;


public class Node
{
    private static final int LINKS_BLOCK_SIZE = CustomBTree.DEGREE * 4;
    public static final int BLOCK_SIZE = LINKS_BLOCK_SIZE + ((CustomBTree.DEGREE - 1) * 8) +
            ((CustomBTree.DEGREE - 1) * Entry.TOTAL_BLOCK_SIZE);

    private Entry[] keys;
    private int[] links;
    private CustomHashTable<Integer, Node> loadedLinks;
    private int nodeNum;
    private boolean needsSave;
    private CustomBTree tree;

    public Node(CustomBTree tree, boolean increment)
    {
        keys = new Entry[CustomBTree.DEGREE - 1];
        links = new int[CustomBTree.DEGREE];
        loadedLinks = new CustomHashTable<>();
        this.tree = tree;
        this.nodeNum = tree.getNumNodes();
        if(increment)
            tree.incrementNumNodes();
    }

    public boolean isFull()
    {
        return getNumKeys() == keys.length;
    }

    public boolean isLeaf()
    {
        return links[0] == 0;
    }

    public Node getLink(int i)
    {
        Node inRam = loadedLinks.get(links[i]);
        return inRam != null ? inRam : read(links[i]);
    }

    public Node read(int blockNum)
    {
        RandomAccessFile raf = tree.getRaf();
        int[] tempLinks = new int[CustomBTree.DEGREE];
        Entry[] tempKeys = new Entry[CustomBTree.DEGREE - 1];
        Node newNode = new Node(tree, false);
        newNode.setKeys(tempKeys);
        newNode.setLinks(tempLinks);
        newNode.setNodeNum(blockNum);

        try
        {
            raf.seek((BLOCK_SIZE + 1) * blockNum);
            for(int i = 0; i < CustomBTree.DEGREE; i++)
                tempLinks[i] = raf.readInt();
            for(int i = 0; i < CustomBTree.DEGREE - 1; i++)
            {
                int numBytes = raf.readInt();

                if(numBytes == 0)
                    continue;

                byte[] keyBytes = new byte[numBytes];
                raf.readFully(keyBytes);
                raf.skipBytes(256 - numBytes);
                String key = new String(keyBytes, Charset.forName("UTF-32BE"));

                Value[] vals = new Value[Entry.MAX_CHILDREN];
                Entry tempEntry = new Entry(key, vals);
                for(int valueNum = 0; valueNum < vals.length; valueNum++)
                {
                    int valKeyLength = raf.readInt();

                    if(valKeyLength == 0)
                    {
                        raf.skipBytes(256+8);
                        continue;
                    }

                    byte[] valKeyBytes = new byte[valKeyLength];
                    raf.readFully(valKeyBytes);
                    raf.skipBytes(256 - valKeyLength);
                    String valKey = new String(valKeyBytes, Charset.forName("UTF-32BE"));
                    double valTfIdf = raf.readDouble();
                    vals[valueNum] = new Value(valKey, valTfIdf);
                    tempEntry.setValues(vals);
                    tempKeys[i] = tempEntry;
                }
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        loadedLinks.put(blockNum, newNode);
        return newNode;
    }

    private void save()
    {
        RandomAccessFile raf = tree.getRaf();
        loadedLinks.put(nodeNum, this);

        try
        {
            raf.seek((BLOCK_SIZE + 1) * nodeNum);
            for(int i : links)
                raf.writeInt(i);
            for(Entry e : keys)
            {
                if(e == null)
                {
                    raf.writeInt(0);
                    raf.write(new byte[Entry.TOTAL_BLOCK_SIZE]);
                    continue;
                }

                raf.writeInt(e.getKeyUtfLength());
                raf.write(e.getKeyUtf());

                for(Value v : e.getValues())
                {
                    if(v == null)
                    {
                        raf.writeInt(0);
                        raf.write(new byte[256]);
                        raf.write(new byte[8]);
                        continue;
                    }
                    raf.writeInt(v.getUtfLength());
                    raf.write(v.getUrlUtf());
                    raf.writeDouble(v.getTfIdf());
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public int getNodeNum()
    {
        return nodeNum;
    }

    public Entry[] getKeys()
    {
        return keys;
    }

    public void setKeys(Entry[] entries)
    {
        keys = entries;
    }

    public void setLinks(int[] links)
    {
        this.links = links;
    }

    public int getNumKeys()
    {
        int c = 0;
        for(Entry e : keys)
            if(e != null)
                c++;

        return c;
    }
    public boolean splitRoot(Entry e)
    {
        Node l = new Node(tree, true);
        Node r = new Node(tree, true);
        for(int x = 0; x < (CustomBTree.DEGREE-1)/2; x++)
        {
            l.keys[x] = this.keys[x];
            r.keys[x] = this.keys[x+(CustomBTree.DEGREE/2)];
        }

        for(int x = 0; x < CustomBTree.DEGREE/2; x++)
        {
            l.links[x] = this.links[x];
            r.links[x] = this.links[x+(CustomBTree.DEGREE/2)];
        }

        keys[0] = keys[(CustomBTree.DEGREE-1)/2];
        for(int x = 1; x < keys.length; x++)
        {
            keys[x] = null;
        }
        int lnum = l.getNodeNum();
        int rnum = r.getNodeNum();

        links[0] = lnum;
        links[1] = rnum;
        l.save();
        r.save();
        for(int x = 2; x < links.length; x++)
        {
            links[x] = 0;
        }
        needsSave = true;
        save();
        return insert(e);
    }

    public boolean insert(Entry e)
    {
        for(int x = 0; x < keys.length; x++)
        {
            if(keys[x] == null)
            {
                if(links[x] == 0)
                {
                    needsSave = true;
                    keys[x] = e;
                    return true;
                }
                else
                {
                    Node linkx = getLink(x);
                    if(linkx.isFull() == false)
                    {
                        boolean ret = linkx.insert(e);
                        if(linkx.needsSave)
                        {
                            linkx.needsSave = false;
                            linkx.save();
                        }
                        return ret;
                    }
                    else
                    {
                        splitFullNode(linkx);
                        needsSave = true;
                        return insert(e);
                    }
                }
            }
            else if(e.compareTo(keys[x]) == 0)
            {
                for(int i = 0; i < keys[x].getValues().length; i++)
                {
                    if(keys[x].getValues()[i] == null)
                    {
                        keys[x].getValues()[i] = e.getValues()[0];
                        save();
                        return true;
                    }
                }

                return false;
            }
            else if(e.compareTo(keys[x]) < 0)
            {
                if(links[x] != 0)
                {
                    Node linkx = getLink(x);
                    if(linkx.isFull() == false)
                    {
                        boolean ret = linkx.insert(e);
                        if(linkx.needsSave)
                        {
                            linkx.needsSave = false;
                            linkx.save();
                        }
                        return ret;
                    }
                    else
                    {
                        splitFullNode(linkx);
                        needsSave = true;
                        return insert(e);
                    }
                }
                else//add to this node
                {
                    for(int i = 0; i >= keys.length; i++)
                    {
                        if(e.compareTo(keys[i]) == 0)
                            return false;
                    }
                    for(int i = keys.length-2; i >= x; i--)
                        keys[i+1] = keys[i];

                    keys[x] = e;
                    needsSave = true;
                    return true;
                }
            }
            else if(x == CustomBTree.DEGREE - 2)
            {
                Node linkx1 = getLink(x+1);
                boolean ret = linkx1.insert(e);
                if(linkx1.needsSave)
                {
                    linkx1.needsSave = false;
                    linkx1.save();
                }
                return ret;
            }
        }
        return true;
    }

    public void splitFullNode(Node fNode)
    {
        int midIndex = 0;
        Entry mid = fNode.keys[(CustomBTree.DEGREE-1)/2];
        for(int x = 0; x < keys.length; x++)
        {
            if(keys[x] == null)
            {
                keys[x] = mid;
                midIndex = x;
                break;
            }
            else if(mid.compareTo(keys[x]) < 0)
            {
                for(int i = keys.length-1; i > x; i--)
                {
                    keys[i] = keys[i-1];
                    links[i+1] = links[i];
                }
                keys[x] = mid;
                midIndex = x;
                break;
            }
        }
        Node right = new Node(tree, true);
        for(int x = 0; x < (CustomBTree.DEGREE/2)-1; x++)
        {
            right.keys[x] = fNode.keys[x+(CustomBTree.DEGREE/2)];
            right.links[x] = fNode.links[x+(CustomBTree.DEGREE/2)];
        }
        right.links[CustomBTree.DEGREE/2-1] = fNode.links[CustomBTree.DEGREE-1];
        int rnum = right.getNodeNum();
        links[midIndex+1] = rnum;
        right.save();
        for(int x = (CustomBTree.DEGREE/2)-1; x < CustomBTree.DEGREE-1; x++)
        {
            fNode.keys[x] = null;
            fNode.links[x+1] = 0;
        }
        links[midIndex] = fNode.getNodeNum();
        fNode.save();
    }

    public Value[] search(String key)
    {
        for(int x = 0; x < keys.length; x++)
        {
            if(keys[x] == null)
            {
                if(isLeaf())
                {
                    return null;
                }

                return getLink(x).search(key);
            }
            else if(key.compareTo(keys[x].getKey()) == 0)
                return keys[x].getValues();
            else if(key.compareTo(keys[x].getKey()) < 0)
                return isLeaf() ? null : getLink(x).search(key);
            else if(x == keys.length - 1)
                return isLeaf() ? null : getLink(x + 1).search(key);
        }

        return null;
    }

    private void setNodeNum(int i)
    {
        nodeNum = i;
    }
}
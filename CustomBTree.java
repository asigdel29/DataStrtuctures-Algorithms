package data_structures.btree;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Date;


public class CustomBTree
{
    public static final int DEGREE = 4;

    private Node root;
    private int height;
    private int numNodes;
    private String parentUrl;
    private RandomAccessFile raf;

    public CustomBTree(String parentUrl)
    {
        try
        {
            this.parentUrl = parentUrl;

            File f = new File("C:\\Users\\sigde\\Assignment2\\src\\data_structures\\btree"+parentUrl+".ser");
            raf = new RandomAccessFile(f, "rw");
            root = new Node(this, true);
            if(raf.length() > 0)
                root = root.read(0);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public Value[] get(String key)
    {
        return root.search(key);
    }

    public boolean put(String key, Value value)
    {
        System.out.println("Inserting entry with " + key + value.getUrl() + ", " + value.getTfIdf());
        return insert(new Entry(key, value));
    }

    private boolean insert(Entry e)
    {
        System.out.println("Inserting entry with" + e.getKey() + e.getValues()[0].getUrl() + " - " + e.getValues()[0].getTfIdf());
        if(root.isFull())
        {
            if(root.splitRoot(e))
            {
                height++;
                return true;
            }
        }
        else if(root.insert(e))
            return true;

        return false;
    }

    public void removeRaf()
    {
        try
        {
            raf.close();
            File f = new File("C:\\Users\\sigde\\Assignment2\\src\\data_structures\\btree"+parentUrl+".ser");

            if(f.exists())
            {
                System.out.println("Delete: " + f.delete());
            }
            raf = new RandomAccessFile(f, "rw");
            numNodes = 0;
            root = new Node(this, true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public int getHeight()
    {
        return height;
    }

    public int getNumNodes()
    {
        return numNodes;
    }

    public RandomAccessFile getRaf()
    {
        return raf;
    }

    public Date getLastModifiedRaf()
    {
        File f = new File("C:\\Users\\sigde\\Assignment2\\src\\data_structures\\btree" +parentUrl+".ser");

        return f.exists() && f.length() > 0 ? new Date(f.lastModified()) : null;
    }

    public void incrementNumNodes()
    {
        numNodes++;
    }

    public Node getRoot()
    {
        return root;
    }
}
package data_structures;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class WebPageCache extends data_structures.CustomHashTable<String, String>
{

    public void save(String filePath)
    {
        try(RandomAccessFile raf = new RandomAccessFile(filePath, "rw"))
        {
            for(String key : keySet())
            {
                raf.writeUTF(key);
                raf.writeUTF(get(key));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void load(String filePath)
    {
        try(RandomAccessFile raf = new RandomAccessFile(filePath, "r"))
        {
            while(true)
            {
                String key = raf.readUTF();
                String val = raf.readUTF();

                if(key != null && val != null)
                {
                    System.out.println("loaded key: " + key + ", val: " + val);
                    this.put(key, val);
                }
            }
        }
        catch(EOFException e)
        {
            System.out.println("End of file reached");
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Web page cache file not found");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
}
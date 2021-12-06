import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Merkle_tree implements Serializable
{
    public byte [] hash_value;
    public Merkle_tree left_tree;
    public Merkle_tree right_tree;
    public int beginning_index;
    public int ending_index;
    public String eventString;

    private MessageDigest digest;

    public Merkle_tree(String event)
    {
        init();
        eventString = event;
        hash_value = digest.digest(event.getBytes(StandardCharsets.UTF_8));
    }

    public Merkle_tree(Merkle_tree left, Merkle_tree right)
    {
        init();
        left_tree = left;
        right_tree = right;
        updateTreeHash();
        updateIndex();
    }

    public String getHashString()
    {
        return new String(HexBin.encode(hash_value));
    }

    private void init()
    {
        try
        {
            digest = MessageDigest.getInstance("SHA256");
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }

    private String getLeftHashString()
    {
        if (left_tree == null)
        {
            return "";
        }
        return left_tree.getHashString();
    }

    private String getRightHashString()
    {
        if (right_tree == null)
        {
            return "";
        }
        return right_tree.getHashString();
    }

    private void updateTreeHash()
    {
        String str = getLeftHashString() + getRightHashString();
        hash_value = digest.digest(str.getBytes(StandardCharsets.UTF_8));
    }

    private void updateIndex()
    {
        if (left_tree != null)
        {
            beginning_index = left_tree.beginning_index;
        }
        if (right_tree != null)
        {
            ending_index = right_tree.ending_index;
        }
    }
}

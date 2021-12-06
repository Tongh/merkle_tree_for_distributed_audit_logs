import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

public class Merkle_tree implements Serializable
{
    public byte [] hash_value;
    public Merkle_tree left_tree;
    public Merkle_tree right_tree;
    public int beginning_index;
    public int ending_index;
    public String eventString;
    public boolean bIsLeaf;
    public Merkle_tree parent;

    private MessageDigest digest;

    public Merkle_tree()
    {
        init();
        bIsLeaf = false;
    }

    public Merkle_tree(String event)
    {
        init();
        bIsLeaf = true;
        eventString = event;
        hash_value = digest.digest(event.getBytes(StandardCharsets.UTF_8));
    }

    public Merkle_tree(Merkle_tree left, Merkle_tree right)
    {
        init();
        bIsLeaf = false;
        setLeft_tree(left);
        setRight_tree(right);
    }

    public String getHashString()
    {
        return new String(HexBin.encode(hash_value));
    }

    public Merkle_tree addNode(Merkle_tree node)
    {
        LinkedList<Merkle_tree> quee = new LinkedList<Merkle_tree>();
        quee.add(this);
        Merkle_tree temp;
        boolean bIsDepthFound = false;
        while (!quee.isEmpty())
        {
            temp = quee.remove();

            if (temp.left_tree != null)
            {
                if (!temp.left_tree.bIsLeaf)
                {
                    quee.add(temp.left_tree);
                    bIsDepthFound = true;
                }
                else
                {
                    break;
                }
            }
            else
            {
                temp.setLeft_tree(node);
            }
            if (temp.right_tree != null)
            {
                if (!temp.right_tree.bIsLeaf)
                {
                    quee.add(temp.right_tree);
                }
                else
                {
                    break;
                }
            }
            else if(bIsDepthFound)
            {
                temp.setRight_tree(node);
            }

            bIsDepthFound = false;
        }

        return null;
    }

    public void setLeft_tree(Merkle_tree node)
    {
        left_tree = node;
        left_tree.parent = this;
        updateTreeHash();
        updateIndex();
    }

    public void setRight_tree(Merkle_tree node)
    {
        right_tree = node;
        right_tree.parent = this;
        updateTreeHash();
        updateIndex();
    }

    public void updateTreeHash()
    {
        String str = getLeftHashString() + getRightHashString();
        hash_value = digest.digest(str.getBytes(StandardCharsets.UTF_8));
        if (parent != null)
        {
            parent.updateTreeHash();
        }
    }

    public void updateIndex()
    {
        if (left_tree != null)
        {
            beginning_index = left_tree.beginning_index;
        }
        if (right_tree != null)
        {
            ending_index = right_tree.ending_index;
        }
        else
        {
            ending_index = left_tree.ending_index;
        }
        if (parent != null)
        {
            parent.updateIndex();
        }
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
}

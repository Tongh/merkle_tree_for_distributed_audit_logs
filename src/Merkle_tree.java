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
        if (hash_value == null)
        {
            return "";
        }
        return new String(HexBin.encode(hash_value));
    }

    public Merkle_tree addNode(Merkle_tree node)
    {
        LinkedList<Merkle_tree> quee = new LinkedList<Merkle_tree>();
        quee.add(this);
        Merkle_tree temp;
        Merkle_tree lastNode = null;
        boolean bIsDepthFound = false;
        while (!quee.isEmpty())
        {
            temp = quee.remove();

            if (temp.left_tree != null)
            {
                if (!temp.left_tree.bIsLeaf)
                {
                    quee.add(temp.left_tree);
                }
                else
                {
                    bIsDepthFound = true;
                }
            }
            else
            {
                temp.setLeft_tree(node);
                return this;
            }
            if (temp.right_tree != null)
            {
                if (!temp.right_tree.bIsLeaf)
                {
                    quee.add(temp.right_tree);
                }
            }
            else if(bIsDepthFound)
            {
                temp.setRight_tree(node);
                return this;
            }
            else
            {
                lastNode = temp;
            }

            bIsDepthFound = false;
        }

        if (lastNode != null)
        {
            int depth = computeTreeDepth(getRoot());
            int heigtToRoot = lastNode.getHeigthToRoot() + 1;
            for (int i = heigtToRoot; i < depth; i++)
            {
                if (i == depth - 1)
                {
                    lastNode.setLeft_tree(node);
                    return getRoot();
                }
                else if (i == heigtToRoot)
                {
                    Merkle_tree newNode = new Merkle_tree();
                    lastNode.setRight_tree(newNode);
                    lastNode = newNode;
                }
                else
                {
                    Merkle_tree newNode = new Merkle_tree();
                    lastNode.setLeft_tree(newNode);
                    lastNode = newNode;
                }
            };
        }

        Merkle_tree newRoot = null;
        Merkle_tree newParent = null;
        for (int i = 0; i < computeTreeDepth(this); i++)
        {
            Merkle_tree newNode = new Merkle_tree();
            if (i == 0)
            {
                newRoot = newNode;
                newRoot.setLeft_tree(this);
            }
            else if (i == 1)
            {
                newParent.setRight_tree(newNode);
            }
            else
            {
                newParent.setLeft_tree(newNode);
            }
            newParent = newNode;
        };
        newParent.setLeft_tree(node);
        return newRoot;
    }

    public void displayTree()
    {
        LinkedList<Merkle_tree> quee = new LinkedList<Merkle_tree>();
        quee.add(this);
        Merkle_tree temp;
        int depth = 0;
        System.out.println("-----------------depth :" + depth + "----------------------");
        while (!quee.isEmpty())
        {
            temp = quee.remove();

            if (temp.getHeigthToRoot() != depth)
            {
                System.out.println("-----------------depth :" + (depth + 1) + "----------------------");
                depth = temp.getHeigthToRoot();
            }

            if (temp.bIsLeaf)
            {
                System.out.println(temp.eventString + "\t" + temp.getHashString());
            }
            else
            {
                System.out.println("Node:" + temp.beginning_index + " - " +
                        temp.ending_index + "\t" + temp.getHashString());
            }

            if (temp.left_tree != null)
            {
                quee.add(temp.left_tree);
            }
            if (temp.right_tree != null)
            {
                quee.add(temp.right_tree);
            }
        }
        System.out.println("-----------------FINISHED----------------------");
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

    public boolean isMember(String event)
    {
        if (bIsLeaf)
        {
            if (eventString.equals(event))
            {
                return true;
            }
            return false;
        }
        boolean left = left_tree == null ? false : left_tree.isMember(event);
        boolean right = right_tree == null ? false : right_tree.isMember(event);
        return left | right;
    }

    public int getHeigthToRoot()
    {
        if (parent == null)
        {
            return 0;
        }
        return 1 + parent.getHeigthToRoot();
    }

    public Merkle_tree getRoot()
    {
        if (parent == null)
        {
            return this;
        }
        return parent.getRoot();
    }

    public static int computeTreeDepth(Merkle_tree root)
    {
        if (root == null)
        {
            return 0;
        }
        int lDepth = computeTreeDepth(root.left_tree);
        int rDepth = computeTreeDepth(root.right_tree);
        return lDepth >= rDepth ? lDepth + 1 : rDepth + 1;
    }

    private void init()
    {
        try
        {
            digest = MessageDigest.getInstance("SHA-256");
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

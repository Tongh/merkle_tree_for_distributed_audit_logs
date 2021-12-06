import java.io.Serializable;

public class Merkle_tree implements Serializable
{
    public byte [] hash_value;
    public Merkle_tree left_tree;
    public Merkle_tree right_tree;
    public int beginning_index;
    public int ending_index;

    public Merkle_tree(String event)
    {

    }

    public Merkle_tree(Merkle_tree left, Merkle_tree right)
    {

    }
}

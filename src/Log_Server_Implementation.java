import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Log_Server_Implementation extends UnicastRemoteObject implements Log_Server_Interface
{
    private Merkle_tree root;

    protected Log_Server_Implementation(Merkle_tree node) throws RemoteException
    {
        root = node;
    }

    @Override
    public Merkle_tree getRoot() throws RemoteException
    {
        return root;
    }

    @Override
    public void Log_Server(String text_file) throws RemoteException
    {
        try
        {
            System.out.println("Open: " + text_file);
            File file = new File(text_file);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            List<String> events = new ArrayList<>();
            int index = 0;
            while (true)
            {
                line = br.readLine();
                if (line == null)
                {
                    break;
                }
                events.add(line);
            }

            addEvents(events);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void addEvent(String event) throws RemoteException
    {
        Merkle_tree node = new Merkle_tree(event);
        node.beginning_index = node.ending_index = root.ending_index + 1;
        root = getRoot().addNode(node);
    }

    @Override
    public void addEvents(List<String> events) throws RemoteException
    {
        for (String event :events)
        {
            addEvent(event);
        }
    }

    @Override
    public List<Merkle_tree> genPath(int index) throws RemoteException
    {
        List<Merkle_tree> ret = new ArrayList<>();

        Merkle_tree node = root.getNode(index);
        Merkle_tree parent = node.parent;
        while (parent != null)
        {
            ret.add(node.getBrother());
            node = parent;
            parent = node.parent;
        }

        System.out.println("genPath(" + index + "): " + getStringListNode(ret));

        return ret;
    }

    @Override
    public List<Merkle_tree> genProof(int tree_size) throws RemoteException
    {
        List<Merkle_tree> ret = new ArrayList<>();

        Merkle_tree node = root.getNode(tree_size);
        node = node.parent;
        ret.add(node);
        Merkle_tree parent = node.parent;
        while (parent != null)
        {
            ret.add(node.getBrother());
            node = parent;
            parent = node.parent;
        }

        System.out.println("genProof(" + tree_size + "): " + getStringListNode(ret));

        return ret;
    }

    public String getStringListNode(List<Merkle_tree> nodes)
    {
        String ret = "[";
        for (Merkle_tree node : nodes)
        {
            ret += node.getNodeString();
            ret += ", ";
        }
        ret = ret.substring(0, ret.length() - 2);
        ret += "]";
        return ret;
    }
}

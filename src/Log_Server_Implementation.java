import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
            int index = 0;
            while (true)
            {
                line = br.readLine();
                if (line == null)
                {
                    break;
                }
                index++;
                Merkle_tree node = new Merkle_tree(line);
                node.beginning_index = node.ending_index = index;
            }

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

    }

    @Override
    public void addEvents(List<String> event) throws RemoteException
    {

    }

    @Override
    public List<Merkle_tree> genPath(int index) throws RemoteException
    {
        return null;
    }

    @Override
    public List<Merkle_tree> genProof(int tree_size) throws RemoteException
    {
        return null;
    }
}

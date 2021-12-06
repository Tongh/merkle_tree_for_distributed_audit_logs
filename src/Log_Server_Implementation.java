import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Log_Server_Implementation extends UnicastRemoteObject implements Log_Server_Interface
{
    protected Log_Server_Implementation() throws RemoteException
    {
    }

    @Override
    public Merkle_tree getRoot() throws RemoteException
    {
        return null;
    }

    @Override
    public void Log_Server(String text_file) throws RemoteException
    {

    }

    @Override
    public void addEvent(String event) throws RemoteException
    {

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

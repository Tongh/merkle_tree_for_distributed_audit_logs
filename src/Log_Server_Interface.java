import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Log_Server_Interface extends Remote
{
    Merkle_tree getRoot() throws RemoteException;
    void Log_Server(String text_file) throws RemoteException;
    void addEvent(String event) throws RemoteException;
    void addEvents(List<String> event) throws RemoteException;
    List<Merkle_tree> genPath(int index) throws RemoteException;
    List<Merkle_tree> genProof(int tree_size) throws RemoteException;
}

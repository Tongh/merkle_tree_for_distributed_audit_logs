import java.rmi.RemoteException;

public class Auditor
{
    Log_Server_Implementation log;
    Merkle_tree root;
    int size;

    public Auditor(Log_Server_Implementation logServer) throws RemoteException
    {
        log = logServer;
        root = logServer.getRoot();
        size = root.ending_index;
    }

    public boolean isMember(String event) throws Exception
    {
        return false;
    }

    public Merkle_tree getRoot() throws RemoteException {
        root = log.getRoot();
        return root;
    }

    public void displayLogs() throws RemoteException {
        getRoot().displayTree();
    }
}

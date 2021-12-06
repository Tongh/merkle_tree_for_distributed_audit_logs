import java.rmi.RemoteException;

public class Test
{
    public static void main(String[] args) throws RemoteException
    {
        Merkle_tree merkle_tree = new Merkle_tree();
        Log_Server_Implementation server = new Log_Server_Implementation(merkle_tree);
        Auditor auditor = new Auditor(server);

        server.Log_Server("events.test.txt");
    }
}

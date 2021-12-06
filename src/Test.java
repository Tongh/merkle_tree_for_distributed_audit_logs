import java.rmi.RemoteException;

public class Test
{
    public static void main(String[] args) throws Exception
    {
        Merkle_tree merkle_tree = new Merkle_tree();
        Log_Server_Implementation server = new Log_Server_Implementation(merkle_tree);
        server.Log_Server("events.test.txt");

        Auditor auditor = new Auditor(server);
        auditor.displayLogs();

        System.out.println("Log 5 is in Logs: " + auditor.isMember("Log 5"));
        System.out.println("Log 13 is in Logs: " + auditor.isMember("Log 13"));

        server.addEvent("Log 13");
        System.out.println("Running : server.addEvent(\"Log 13\")");
        System.out.println("Log 13 is in Logs: " + auditor.isMember("Log 13"));
        auditor.displayLogs();

        server.genPath(4);
        server.genProof(6);

    }
}

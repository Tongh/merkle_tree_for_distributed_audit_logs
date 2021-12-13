import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

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
        boolean ret = true;
        int index = getRoot().isMember(event);

        if (index == -1)
        {
            return false;
        }
        Merkle_tree node = getRoot().getNode(index);

        // genPath
        List<Merkle_tree> path = log.genPath(index);

        String halfHash = node.getHashString();
        String otherHash, str;
        byte [] hash_value;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        for (Merkle_tree n : path)
        {
            otherHash = n.getHashString();
            str = n.isLeftNode() ? otherHash + halfHash : halfHash + otherHash;
            hash_value = digest.digest(str.getBytes(StandardCharsets.UTF_8));
            if (!Arrays.equals(hash_value, n.parent.hash_value))
            {
                ret = false; break;
            }
            halfHash = new String(HexBin.encode(hash_value));
        }

        // genProof
        List<Merkle_tree> proof = log.genProof(index);
        Merkle_tree n;
        halfHash = proof.get(0).getHashString();
        for (int i = 1; i < proof.size(); i++)
        {
            n = proof.get(i);
            otherHash = n.getHashString();
            str = n.isLeftNode() ? otherHash + halfHash : halfHash + otherHash;
            hash_value = digest.digest(str.getBytes(StandardCharsets.UTF_8));
            if (!Arrays.equals(hash_value, n.parent.hash_value))
            {
                ret = false; break;
            }
            halfHash = new String(HexBin.encode(hash_value));
        }

        return ret;
    }

    public Merkle_tree getRoot() throws RemoteException {
        root = log.getRoot();
        return root;
    }

    public void displayLogs() throws RemoteException {
        getRoot().displayTree();
    }
}

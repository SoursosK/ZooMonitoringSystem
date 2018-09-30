
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {

    public void updateInfo(String name, int fileByteSize, byte[] bytes, String timeStamp) throws RemoteException;
    
}

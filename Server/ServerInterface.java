
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    public void subscribeGuard(ClientInterface ci) throws RemoteException;
    public void unsubscribeGuard(ClientInterface ci) throws RemoteException;
    
}

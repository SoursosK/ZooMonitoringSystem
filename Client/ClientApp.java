
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientApp extends UnicastRemoteObject implements ClientInterface {

    private static ServerInterface si;                                          //Server
    private Graphics UI;
   
    private int flag;                                                           //Determines if the UI has to be updated
    private String timeStamp;                                                   //Timestamp of the last captured image
    private byte[] image;                                                       //The last captured image
    
    private FileOutputStream fos;
    
    public ClientApp() throws RemoteException{
        super();
        
        this.flag = 0;
        this.timeStamp = null;
        this.fos = null;
    
    }
    
    public static void main(String[] args) throws IOException, NotBoundException, InterruptedException {

        si = (ServerInterface)Naming.lookup("//localhost/server");

        ClientApp cApp = new ClientApp();
        cApp.UI = new Graphics(cApp);

        //Subscribe the guard to the server
        si.subscribeGuard(cApp);
        cApp.updateUI();
        
    }//main
    
    public void updateUI() throws InterruptedException, IOException {           

        while(true)

            if(!(flag == 1))
                try {
                    Thread.sleep(1999);
                } catch (InterruptedException ex) { }
            else {
                //Update the pannel
                UI.updateUIInfo(image, timeStamp);
                flag = 0;
            }

    }//updateUI

    //Unsubscribes the guard before the program gets terminated
    public void unsubscribe() throws RemoteException{
        si.unsubscribeGuard(this);
    }

    //Updates the info in the client's app
    @Override
    public void updateInfo(String name, int fileByteSize, byte[] bytes, String timeStamp) throws RemoteException {

        System.out.println("tile name " +  name);
       
        this.timeStamp = "Movement event caught at " + timeStamp;
        System.out.println("Size: " + fileByteSize);                        
        
        this.image = bytes;
        this.flag=1;

    }//updateInfo


}//ClientApp

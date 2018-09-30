
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ServerApp extends UnicastRemoteObject implements ServerInterface {

    private static ServerApp sApp;
    private static ArrayList<ClientInterface> guards;                           //List of guards subscribed
    
    
    public ServerApp() throws RemoteException {
        super();
    }
    
    public static void main(String[] args) throws RemoteException, MalformedURLException, InterruptedException, IOException {

        guards = new ArrayList();

        LocateRegistry.createRegistry(1099);
        sApp = new ServerApp();
        
        Naming.rebind("//localhost/server", sApp);

        //Begin monitoring
        sApp.initiateMonitoringSystem();
        
        System.out.println("Server up and running");
        
    }//main

    @Override
    public void subscribeGuard(ClientInterface ci) throws RemoteException{

        System.out.println("A new Guard has just started his shift!");
        guards.add(ci);

    }
    
    @Override
    public void unsubscribeGuard(ClientInterface ci) throws RemoteException{

        System.out.println("A Guard just finished his shift!");
        guards.remove(ci);

    }


    void initiateMonitoringSystem() throws InterruptedException, RemoteException, IOException {

        //Location of the new photos
        File dirName = new File( System.getProperty("user.home") + "\\Desktop\\MonkeyPhotos");
        File[] dirFiles;

        Calendar cal;
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

        byte[] bytes = null;                                                   
        int count = 0;
        InputStream in = null;
        
        while(true) {
            //Check every 5-8 seconds for new photos
            Thread.sleep((int)(Math.random()*3000 + 5000));

            //Return a list of the photos found
            dirFiles = dirName.listFiles();
            
            if(dirFiles.length == 0) {

                System.out.println("No new file was found");
                continue;

            } else {

                System.out.println("sending " + dirFiles[0].getName() + (int)dirFiles[0].length()); 

                //Open a stream to get the photo
                in = new FileInputStream(dirFiles[0]);
                
                cal = Calendar.getInstance();

                //File size
                count = (int)dirFiles[0].length();
                bytes = new byte[count];

                in.read(bytes);
                in.close();

                //Update all the clients with the new info
                for(ClientInterface client: guards) {

                    System.out.println("Sending to client");
                    client.updateInfo(dirFiles[0].getName(), (int)dirFiles[0].length(), bytes, format.format(cal.getTime()));
                }

            }//else

            dirFiles[0].delete();                                               //Delete the photo

        }//while
        
    }//initiateMonitoringSystem


}//ServerApp

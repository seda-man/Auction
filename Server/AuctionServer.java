package Server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class AuctionServer
{
    private ControllerImpl cImpl;
    public static void main (String[] args) //throws NamingException
    {
        new AuctionServer();
    }

    public AuctionServer()
    {
        System.out.println("Starting Auction Server");
        System.setProperty("java.rmi.server.hostname","127.0.0.1"); //"192.168.1.1");
        try{
            cImpl = new ControllerImpl();
            if (cImpl != null)
                System.out.println("Made the remote object");
            Naming.rebind("controller",  cImpl);
            System.out.println("Server started and registered");
        }
        catch(MalformedURLException e)
        {
            System.out.println("Incorrect server name");
            e.printStackTrace();
        }
        catch (RemoteException e)
        {
            System.out.println("Could not create Server.AuctionServer");
            e.printStackTrace();
        }
    }
}
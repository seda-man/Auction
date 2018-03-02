package Server;

import Client.ClientInterface;
import Server.Catalogs.ObjectCatalog;
import Server.Models.User;

import java.rmi.*;
import java.util.ArrayList;

public interface  ControllerInterface extends Remote{
    void signUp(String username) throws RemoteException;
    User signIn(String username) throws RemoteException;
    void setClient(ClientInterface c, int userId) throws RemoteException;
    void makeBid(int userId, int objectId, float amount) throws RemoteException;
    ArrayList<Integer> getObjectIds() throws RemoteException;
    ObjectCatalog getObjectCatalog() throws RemoteException;
    int findWinner(int objectId) throws RemoteException;
    String display(int objectId) throws RemoteException;
    Thread autoBid(int userId, int objectId) throws RemoteException;
    void stopAutoBid(int userId) throws RemoteException;

    String history(int objectId, int userId) throws RemoteException;
    void stopShowingHistory(int objectId, int userId) throws RemoteException;
}

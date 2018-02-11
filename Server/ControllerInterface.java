package Server;

import Server.Catalogs.ObjectCatalog;
import Server.Models.User;

import java.rmi.*;
import java.util.ArrayList;

public interface  ControllerInterface extends Remote{
    void signUp(String username) throws RemoteException;
    User signIn(String username) throws RemoteException;
    void makeBid(int userId, int objectId, float amount) throws RemoteException;
    ArrayList<Integer> getObjectIds() throws RemoteException;
    ObjectCatalog getObjectCatalog() throws RemoteException;
    int findWinner(int objectId) throws RemoteException;
}

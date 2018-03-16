package Client;

import com.sun.org.apache.regexp.internal.RE;

import javax.swing.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    void notifyWin() throws RemoteException;
    void notifyLoose() throws RemoteException;
    void setHistory(String s) throws RemoteException;
    void setTimer(String s) throws RemoteException;
    void endAuction(String s) throws RemoteException;

}

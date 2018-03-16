package Server.Models;

import Client.Client;
import Client.ClientInterface;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class User implements Serializable{
    private String username;
    private ArrayList<Integer> bidIds;
    private int userId;
    private ClientInterface client;
    private Thread autobidThread;
    private Thread historyThread;

    public void setHistoryThread(Thread historyThread) {
        this.historyThread = historyThread;
    }

    public Thread getHistoryThread() {
        return historyThread;
    }

    public User(String username, int userId) {
        this.username = username;
        this.userId = userId;
        this.bidIds = new ArrayList<>();

    }

    public void setAutobidThread(Thread autobidThread) {
        this.autobidThread = autobidThread;
    }

    public Thread getAutobidThread() {
        return autobidThread;
    }
    public ClientInterface getClient() {
        return client;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setClient(ClientInterface client){
        this.client = client;
    }

    public void setBidId(int bidId) {
        this.bidIds.add(bidId);
    }

    public ArrayList<Integer> getBidIds() {
        return bidIds;
    }

    public int getUserId() {
        return userId;
    }

    public void winnerIs(int winnerId){
        if(this.client == null){
            System.out.println("null");
        }
        else {
            try {
                if (winnerId == userId) {
                    client.endAuction("Congratulations you have won!");
//                    client.notifyWin();
                } else {
                    client.endAuction("Unfortunately you have lost :(");
//                    client.notifyLoose();
                }
            }
            catch(RemoteException e){
                System.out.println("!!!!!!!!!!!!!!!!");
                throw new IllegalStateException();
            }
        }
    }
}
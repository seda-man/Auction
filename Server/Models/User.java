package Server.Models;

import Client.Client;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{
    private String username;
    private ArrayList<Integer> bidIds;
    private int userId;
    private Client client;

    public User(String username, int userId) {
        this.username = username;
        this.userId = userId;
        this.bidIds = new ArrayList<>();
    }

    public Client getClient() {
        return client;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setClient(Client client){
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
            System.out.println(winnerId);
            if(winnerId == userId){
                client.notifyWin();
            }
            else {
                client.notifyLoose();
            }
        }
    }
}
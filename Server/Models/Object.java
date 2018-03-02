package Server.Models;

import Server.Models.ObjectDescription;

import java.io.Serializable;
import java.util.ArrayList;

public class Object implements Serializable{
    private String name;
    private int objectId;
    private String status;
    private ObjectDescription objectDescription;
    private ArrayList<Bid> bids = new ArrayList<>();
    private ArrayList<String> historyList = new ArrayList<>();

    public Object(String name, int id, String status) {
        this.name = name;
        this.objectId = id;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setId(int id) {
        this.objectId = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ObjectDescription getObjectDescription() {
        return objectDescription;
    }

    public void setObjectDescription(ObjectDescription objectDescription) {
        this.objectDescription = objectDescription;
    }

    public ArrayList<Bid> getBids() {
        return bids;
    }

    public void setBids(ArrayList<Bid> bids) {
        this.bids = bids;
    }

    public void addBid(Bid newBid) {

        this.bids.add(newBid);
        this.historyList.add(newBid.getDesc());
    }

    public ArrayList<String> getHistoryList() {
        return historyList;
    }

    public String display(){

        String description = "The object is " + name +"\n" + "Starting bid: "
                + objectDescription.getStartingBid()+ "\n"+ "Estimated price: "
                + objectDescription.getEstimatedPrice() +"\n"+  "The description: "
                + objectDescription.getDescription();
        return description;
    }
}
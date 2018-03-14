package Server.Models;
import java.io.Serializable;
import java.util.ArrayList;


public class Object implements Serializable{
    private String name;
    private int objectId;
    private String status;
    public int minutes;
    private ObjectDescription objectDescription;
    private ArrayList<Bid> bids = new ArrayList<>();
    private ArrayList<String> historyList = new ArrayList<>();

    public Object(String name, int id, String status, int minutes) {
        this.name = name;
        this.objectId = id;
        this.status = status;

    }

    public String getName() {
        return name;
    }

    public int getMinutes() {
        return minutes;
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

        String description = "<html>The object is " + name +"<br>" + "Starting bid: "
                + objectDescription.getStartingBid()+ "<br>"+ "Estimated price: "
                + objectDescription.getEstimatedPrice() +"<br>"+  "The description: "
                + objectDescription.getDescription() + "</html>";
        return description;
    }
}
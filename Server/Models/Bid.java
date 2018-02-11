package Server.Models;

import java.io.Serializable;

public class Bid implements Serializable{
    private double amount;
    private int bidId;
    private int userId;
    private int objectId;

    public int getBidId() {
        return bidId;
    }

    public int getObjectId() {
        return objectId;
    }

    public int getUserId() {
        return userId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public Bid(int userId, int objectId, Double amount, int id) {
        this.amount = amount;
        this.userId = userId;
        this.objectId = objectId;
        this.bidId = id;
    }

    public double getAmount() {
        return amount;
    }


}
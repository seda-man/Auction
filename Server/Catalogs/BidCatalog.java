package Server.Catalogs;

import Server.Models.Bid;

import java.io.Serializable;
import java.util.ArrayList;
public class BidCatalog implements Serializable{
    private ArrayList<Bid> bids = new ArrayList<>();

    public Bid createBid(int userId, int objectId, double amount){
        int id = 0;
        if (bids.size() > 0){
            id = bids.get(bids.size() - 1).getBidId() +1;
        }
        Bid bid = new Bid(userId, objectId, amount, id);
        bids.add(bid);
        return bid;
    }
}

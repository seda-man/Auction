package Catalogs;

import Model.Bid;

import java.util.ArrayList;

public class BidCatalog {
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

import java.util.ArrayList;

public class BidCatalog {
    private ArrayList<Bid> bids = new ArrayList<>();

    public void createBid(int userId, int objectId, double amount){
        int id = 0;
        if (bids.size() > 0){
            id = bids.get(bids.size()).getBidId() +1;
        }
        Bid bid = new Bid(userId, objectId, amount, id);
        bids.add(bid);
    }
}

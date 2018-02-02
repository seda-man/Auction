import java.io.PushbackInputStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Controller {
    private ObjectCatalog objectCatalog;
    private UserCatalog userCatalog;
    private BidCatalog bidCatalog;
    private ArrayList<Integer> objectIds = new ArrayList<Integer>();

    public Controller(){
        this.objectCatalog = new ObjectCatalog();
        this.userCatalog = new UserCatalog();
        this.bidCatalog = new BidCatalog();
    }

    public ObjectCatalog getObjectCatalog() {
        return objectCatalog;
    }

    public void setObjectCatalog(ObjectCatalog objectCatalog) {
        this.objectCatalog = objectCatalog;
    }

    public UserCatalog getUserCatalog() {
        return userCatalog;
    }


    public void setUserCatalog(UserCatalog userCatalog) {
        this.userCatalog = userCatalog;
    }

    public ArrayList<Integer> getObjectIds() {
        return objectIds;
    }

    public void addObjectId(int id) {

        this.objectIds.add(id);
    }

    public void makeBid(int userId, int objectId, double amount)
    {

        Bid bid = bidCatalog.createBid(userId, objectId, amount);
        Object current_obj = objectCatalog.getObject(objectId);
        current_obj.addBid(bid);
    }

    public void findWinner(int objectId) {

        Object curr_object = objectCatalog.getObject(objectId);
        ArrayList<Bid> bids = curr_object.getBids();
        Double max = 0.0;
        int index = 0;
        System.out.println(bids.size());
        for (int counter = 0; counter < bids.size(); counter++)
        {
            if (bids.get(counter).getAmount() > max)
            {
                max = bids.get(counter).getAmount();
                index = counter;
            }
        }
        int userId = bids.get(index).getUserId();
        User winning_user = userCatalog.getUser(userId);

        System.out.println("The maximum bid was: " + max);

        System.out.println("Congratulations " + winning_user.getUsername() + " won!");
    }




}
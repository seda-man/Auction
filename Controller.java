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
    public void makeBid(int userId, int objectId, double amount){
        bidCatalog.createBid(userId, objectId, amount);
    }

    public void findWinner(int objectId) {
//        try {
//            BufferedReader in = new BufferedReader(new FileReader("bids.txt"));
//            String str;
//
//
//            while ((str = in.readLine()) != null) {
//                String[] ar=str.split(",");
//                users.add(ar[0]);
//                bids.add(Double.parseDouble(ar[1]));
//            }
//            in.close();
//        } catch (IOException e) {
//            System.out.println("File Read Error");
//        }

        Object curr_object = objectCatalog.getObject(objectId);
        ArrayList<Bid> bids = curr_object.getBids();
        Double max = 0.0;
        int index = 0;
        for (int counter = 0; counter < bids.size(); counter++)
        {
            if (bids.get(0).getAmount() > max)
            {
                max = bids.get(counter).getAmount();
                index = counter;
            }
        }
        int userId = bids.get(index).getUserId();
        User winning_user = userCatalog.getUser(userId);

        System.out.println("The maximum bid was: " + max);
        Integer ind = bids.indexOf(max);
        System.out.println("Congratulations " + winning_user.getUsername() + " won!");
    }




}
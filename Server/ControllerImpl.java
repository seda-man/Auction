package Server;

import Client.ClientInterface;
import Server.Catalogs.ObjectCatalog;

import Server.Catalogs.UserCatalog;
import Server.Catalogs.BidCatalog;
import Server.Models.Bid;
import Server.Models.Object;
import Server.Models.ObjectDescription;
import Server.Models.User;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.rmi.server.UnicastRemoteObject;
public class ControllerImpl  extends UnicastRemoteObject implements ControllerInterface {
    private ObjectCatalog objectCatalog;
    private UserCatalog userCatalog;
    private BidCatalog bidCatalog;
    private ArrayList<Integer> objectIds = new ArrayList<Integer>();

    public ControllerImpl () throws RemoteException{
        this.objectCatalog = new ObjectCatalog();
        this.userCatalog = new UserCatalog();
        this.bidCatalog = new BidCatalog();


        Object obj1 = objectCatalog.makeObject("Piano", "101");

        ObjectDescription desc1 = new ObjectDescription();
        desc1.setStartingBid(100.);
        desc1.setEstimatedPrice(200.);
        desc1.setDescription("A 19th century black wooden piano.");

        obj1.setObjectDescription(desc1);
        addObjectId(obj1.getObjectId());
    }

    public final ObjectCatalog getObjectCatalog() {
        return objectCatalog;
    }

    public void setObjectCatalog(ObjectCatalog objectCatalog) {
        this.objectCatalog = objectCatalog;
    }

    public UserCatalog getUserCatalog() {
        return userCatalog;
    }

    public String display(int objectId){
        String s =  getObjectCatalog().getObject(objectId).display();
        return s;
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
    public Thread autoBid(int userId, int objectId){
        Thread curr = new Thread(new Runnable(){
            @Override
            public void run() {
                System.out.println("run");
                while(true){
                    Object current_obj = objectCatalog.getObject(objectId);
                    ArrayList<Bid> bids = current_obj.getBids();
                    double max = 0;
                    int max_user = 0;
                    for(Bid bid : bids){
                        if(bid.getAmount() > max){
                            max = bid.getAmount();
                            max_user = bid.getUserId();
                        }
                    }
                    if (max_user != userId){
                        Bid bid = bidCatalog.createBid(userId, objectId, max  + 10);
                        current_obj.addBid(bid);
                    }
                    try{
                        Thread.sleep(10000);
                    }
                    catch (InterruptedException e){
                        System.out.println("Sleeping Thread 'zzzz...'");
                    }
                }
            }
        });
        curr.start();
        userCatalog.getUser(userId).setAutobidThread(curr);
        return curr;
    }

    public void stopAutoBid(int userId){
        Thread curr = userCatalog.getUser(userId).getAutobidThread();
        while(curr.isInterrupted()){
        }
        curr.interrupt();
    }

    public BidCatalog getBidCatalog() {
        return bidCatalog;
    }
    @Override
    public void signUp(String username){
        //TODO add exception
        if (userCatalog.existingUsername(username))
        {
            String a = ("Username is already taken, please enter new username.");
        }
        userCatalog.makeUser(username);

    }

    @Override
    public User signIn(String username) {
        //TODO check if valid
        User current_user = userCatalog.getUser(username);
        return current_user;
    }

    public void setClient(ClientInterface c, int userId){
        userCatalog.getUser(userId).setClient(c);
    }
    @Override
    public void makeBid(int userId, int objectId, float amount)
    {
        //TODO check if amount is valid
        Bid bid = bidCatalog.createBid(userId, objectId, amount);
        Object current_obj = objectCatalog.getObject(objectId);
        current_obj.addBid(bid);
        if(current_obj.getBids().size() == 3){
            findWinner(objectId);
        }
    }

    public String history(int objectId, int userId){
        String h = "";
        Thread curr = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("run");
                while (true) {

                    String h = "";
                    ArrayList<String> historyList = objectCatalog.getObject(objectId).getHistoryList();
                    for (int i = 0; i < historyList.size(); i++) {
                        h = h + historyList.get(i) + "   ";
                    }
                    try{
                        Thread.sleep(1000);
                        }
                    catch (InterruptedException e){
                    System.out.println("Sleeping Thread 'zzzz...'");
                    }
                }
            }

        });
        curr.start();
        userCatalog.getUser(userId).setHistoryThread(curr);
        return h;
    }

    public void stopShowingHistory(int objectId, int userId) {
        Thread curr = userCatalog.getUser(userId).getHistoryThread();
        while(curr.isInterrupted()){
        }
        curr.interrupt();
    }
    public int findWinner(int objectId) {
        Object curr_object = objectCatalog.getObject(objectId);
        ArrayList<Bid> bids = curr_object.getBids();
        ArrayList<Integer> userIds = new ArrayList<>();
        Double max = 0.0;
        int index = 0;
        for (int counter = 0; counter < bids.size(); counter++)
        {
            userIds.add(bids.get(counter).getUserId());
            if (bids.get(counter).getAmount() > max)
            {
                max = bids.get(counter).getAmount();
                index = counter;
            }
        }
        int userId = bids.get(index).getUserId();
        for(int id : userIds){
            userCatalog.getUser(id).winnerIs(userId);
        }
        return userId;
    }


}
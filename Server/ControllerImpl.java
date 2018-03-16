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
import java.util.Timer;
import java.util.TimerTask;

public class ControllerImpl  extends UnicastRemoteObject implements ControllerInterface {
    private ObjectCatalog objectCatalog;
    private UserCatalog userCatalog;
    private BidCatalog bidCatalog;
    private ArrayList<Integer> objectIds = new ArrayList<>();

    public ControllerImpl () throws RemoteException{
        this.objectCatalog = new ObjectCatalog();
        this.userCatalog = new UserCatalog();
        this.bidCatalog = new BidCatalog();


        Object obj1 = objectCatalog.makeObject("Piano", "101", 2);

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

    public BidCatalog getBidCatalog() {
        return bidCatalog;
    }

    public UserCatalog getUserCatalog() {
        return userCatalog;
    }


    public void setUserCatalog(UserCatalog userCatalog) {
        this.userCatalog = userCatalog;
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

    public ArrayList<Integer> getObjectIds() {
        return objectIds;
    }

    public void addObjectId(int id) {
//        ArrayList<Bid> bids ;
//        ArrayList<ClientInterface> clients;

        this.objectIds.add(id);
        int seconds = 60;
        Thread curr = new Thread(() -> {
            int i = seconds ;
            while (true) {
                if (i == 0) {
                    ArrayList<Bid> bids = objectCatalog.getObject(id).getBids();
                    ArrayList<ClientInterface> clients = new ArrayList<>();
                    for(Bid bid: bids){
                        clients.add(userCatalog.getUser(bid.getUserId()).getClient());
                    }
                    for(ClientInterface client : clients){
                        try {
                            client.setTimer("The auction is over");
                        }
                        catch (RemoteException e){
                            System.out.println("RMI remote exception in setting time");
                        }
                    }
                    findWinner(id);
                    Thread.currentThread().interrupt();
                    break;
                }
                ArrayList<Bid> bids = objectCatalog.getObject(id).getBids();
                ArrayList<ClientInterface> clients = new ArrayList<>();
                for(Bid bid: bids){
                    clients.add(userCatalog.getUser(bid.getUserId()).getClient());
                }

                for(ClientInterface client : clients){
                    try {
                        client.setTimer("Time remaining : " + i/360 + " hours "+
                        i/60 + " minutes " + i%60 + " seconds");
                    }
                    catch (RemoteException e){
                        System.out.println("RMI remote exception in setting time");
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Sleeping Thread 'zzzz...'");
                }
                i--;
            }
        });
        curr.start();
    }

    public String display(int objectId){
        String s =  getObjectCatalog().getObject(objectId).display();
        return s;
    }

    public Thread autoBid(int userId, int objectId){
        Thread curr = new Thread(() -> {
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
        });
        curr.start();
        userCatalog.getUser(userId).setAutobidThread(curr);
        return curr;
    }

    public void stopAutoBid(int userId){
        Thread curr = userCatalog.getUser(userId).getAutobidThread();
        if(curr == null){
            return;
        }
        while(curr.isInterrupted()){
        }
        curr.stop();
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
    }
    public int findWinner(int objectId) {
        Object curr_object = objectCatalog.getObject(objectId);
        ArrayList<Bid> bids = curr_object.getBids();
        objectIds.remove(objectId);
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
            stopAutoBid(userId);
            userCatalog.getUser(id).winnerIs(userId);
        }
        return userId;
    }

    public void history(int objectId, int userId, ClientInterface client){
        Thread curr = new Thread(() -> {
            while (true) {

                String h = "<html>";
                ArrayList<String> historyList = objectCatalog.getObject(objectId).getHistoryList();
                for (int i = 0; i < historyList.size(); i++) {
                    if (i == historyList.size() -1){
                        h += historyList.get(i);
                    }
                    else {
                        h += historyList.get(i) + "<br/>";
                    }
                }
                h+= "</html>";
                    try {
                    client.setHistory(h);
                }
                catch (RemoteException e){
                }
                try{
                    Thread.sleep(1000);
                    }
                catch (InterruptedException e){
                System.out.println("Sleeping Thread 'zzzz...'");
                }
            }
        });
        curr.start();
        userCatalog.getUser(userId).setHistoryThread(curr);
    }

    public void stopShowingHistory(int objectId, int userId) {
        Thread curr = userCatalog.getUser(userId).getHistoryThread();
        while(curr.isInterrupted()){
        }
        curr.stop();
    }


}
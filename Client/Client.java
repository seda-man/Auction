package Client;

import Server.Models.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import Server.Models.Object;
import Server.ControllerInterface;

import javax.swing.*;

public class Client extends UnicastRemoteObject implements ClientInterface{
    private String status;
    private Thread autoBidThread;
    private User current_user;
    public void notifyWin(){
        System.out.println("Congratulations you have won!");
        status = "done";

    }

    public void notifyLoose(){
        System.out.println("Unfortunately you have lost :(");
        status = "done";
    }

    public void setUpBidding(ControllerInterface controller, Object object){
        JFrame frame = new JFrame("Bidding on Object" + object.getName());
        frame.setSize(700,300);
        frame.setLayout(null);

        JTextArea amount = new JTextArea();
        amount.setBounds(20,25, 200,20);
        JButton bid = new JButton("Make Bid");
        bid.setBounds(45,65, 150,50);
        JButton autoBid = new JButton("AutoBid");
        autoBid.setBounds(45,135, 150,50);
        JButton stopAutoBid = new JButton("Stop Bidding");
        stopAutoBid.setEnabled(false);
        stopAutoBid.setBounds(45,205, 150,50);
        JButton hide_history = new JButton("Hide History");
        hide_history.setBounds(260,25,200, 30);
        JTextField history = new JTextField();
        history.setEditable(false);
        history.setBounds(480, 25, 200, 250);
        try {
            String hist = controller.history(object.getObjectId(), current_user.getUserId());
            history.setText(hist);
        }
        catch (Exception e){
            System.out.println("history exception");
        }
        JLabel time = new JLabel("0:0:0");
        time.setBounds(380,245,100, 30);
        frame.add(amount);
        frame.add(bid);
        frame.add(autoBid);
        frame.add(stopAutoBid);
        frame.add(hide_history);
        frame.add(history);
        frame.add(time);

        hide_history.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try{
                    controller.stopShowingHistory(object.getObjectId(), current_user.getUserId());
                }
                catch (Exception e){

                }
            }
        });
        bid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    controller.makeBid(current_user.getUserId(), object.getObjectId(), Integer.valueOf(amount.getText()));
                }
                catch(Exception e){

                }
            }
        });

        autoBid.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try{
                    autoBid.setEnabled(false);
                    stopAutoBid.setEnabled(true);
                    autoBidThread = controller.autoBid(current_user.getUserId(), object.getObjectId());

                }
                catch(Exception e) {
                }
            }
        });


        stopAutoBid.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try{
                    controller.stopAutoBid(current_user.getUserId());
                    autoBid.setEnabled(true);
                    stopAutoBid.setEnabled(false);
                }
                catch(Exception e) {
                    System.out.println("Exception");
                }
            }
        });
        frame.setVisible(true);
    }
    public void setupObjects(ControllerInterface controller){
        final JFrame frame = new JFrame("Object List");
        frame.setSize(500,300);
        frame.setVisible(true);
        frame.setLayout(new GridLayout(0,2));
        try {
            ArrayList<Integer> objIds = controller.getObjectIds();
            for (int j = 0; j < objIds.size(); j++) {
                String s = controller.display(objIds.get(j));
                JButton object = new JButton(s);
//                object.setText();
                frame.add(object);
                final int objectId = j;
                object.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        try {
                            Object current_object = controller.getObjectCatalog().getObject(objIds.get(objectId));
                            frame.setVisible(false);
                            setUpBidding(controller, current_object);
                        }
                        catch (Exception e){

                        }
                    }
                });
            }
        }
        catch (Exception e){

        }
    }
    public void setupSignIn(ControllerInterface controller){
        final JFrame frame = new JFrame("Sign-in/Sign-up");
        frame.setSize(500,300);
        frame.setLayout(null);
        JTextArea username = new JTextArea();
        username.setBounds(150,20, 200,20);
        JButton sign_in = new JButton("Sign In");
        sign_in.setBounds(200,50, 100,50);
        JButton sign_up = new JButton("Sign Up");
        sign_up.setBounds(200,120, 100,50);
        frame.add(username);
        frame.add(sign_in);
        frame.add(sign_up);
        frame.setVisible(true);
        sign_in.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try{
                    current_user = controller.signIn(username.getText());
                    frame.setVisible(false);
                    controller.setClient(Client.this, current_user.getUserId());
                    setupObjects(controller);
                }
                catch (Exception e){

                }
            }
        });
        sign_up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try{
                    frame.setVisible(false);
                    controller.signUp(username.getText());
                    current_user = controller.signIn(username.getText());
                    controller.setClient(Client.this, current_user.getUserId());
                    setupObjects(controller);
                }
                catch (Exception e){

                }
            }
        });
    }

    public Client(ControllerInterface controller) throws Exception{
        if (controller == null){
            return;
        }

        setupSignIn(controller);
    }
    public static void main(String[] args) throws Exception {
        try {
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            {
                System.out.println("About to call hello");
                ControllerInterface controller = (ControllerInterface) Naming.lookup("rmi://localhost/controller");
                new Client(controller);

            }
        }
        catch (NotBoundException e)
        {
            System.out.println("Cannot locate server");
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            System.out.println("Bad server name");
            e.printStackTrace();
        }
        catch(Exception e)
        {
            System.out.println("Cannot create a server object");
            e.printStackTrace();
        }

    }
}
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
    private JTextPane history = new JTextPane();
    private JTextPane timer = new JTextPane() ;
    private User current_user;
    public void notifyWin(){
        System.out.println("Congratulations you have won!");
        status = "done";
    }

    public void notifyLoose(){
        System.out.println("Unfortunately you have lost :(");
        status = "done";
    }

    public void setHistory(String s) {
        history.setText(s);
    }

    public void setTimer(String s) {
        timer.setText(s);
    }

    public void setUpBidding(ControllerInterface controller, Object object){
        JFrame frame = new JFrame("Bidding on Object" + object.getName());
        frame.setSize(700,300);
        frame.setLayout(null);

        JTextArea amount = new JTextArea();
        amount.setBounds(20,25, 200,20);
        frame.add(amount);

        JButton bid = new JButton("Make Bid");
        bid.setBounds(45,65, 150,50);
        frame.add(bid);

        JButton autoBid = new JButton("AutoBid");
        autoBid.setBounds(45,135, 150,50);
        frame.add(autoBid);

        JButton stopAutoBid = new JButton("Stop Bidding");
        stopAutoBid.setEnabled(false);
        stopAutoBid.setBounds(45,205, 150,50);
        frame.add(stopAutoBid);

        JButton hide_history = new JButton("Hide History");
        hide_history.setBounds(260,25,200, 30);
        frame.add(hide_history);


        JButton show_history = new JButton("Show History");
        show_history.setBounds(260,25,200, 30);
        show_history.setVisible(false);
        frame.add(show_history);


        history.setContentType("text/html");
        history.setEditable(false);
        history.setBounds(480, 25, 200, 250);
        frame.add(history);


        try {
            controller.history(object.getObjectId(), current_user.getUserId(), Client.this);
        }
        catch (Exception e){
            System.out.println("history exception");
        }
        timer.setContentType("text/html");
        timer.setEditable(false);
        timer.setBounds(280,220,200, 50);
        frame.add(timer);

        hide_history.addActionListener(actionEvent -> {
            try{
                controller.stopShowingHistory(object.getObjectId(), current_user.getUserId());
                history.setText("");
                hide_history.setVisible(false);
                show_history.setVisible(true);
            }
            catch (Exception e){

            }
        });

        show_history.addActionListener(actionEvent -> {
            try {
                controller.history(object.getObjectId(), current_user.getUserId(), Client.this);
                hide_history.setVisible(true);
                show_history.setVisible(false);
            }
            catch (Exception e){
                System.out.println("history exception");
            }

        });
        bid.addActionListener(actionEvent -> {
            try {
                controller.makeBid(current_user.getUserId(), object.getObjectId(), Integer.valueOf(amount.getText()));
            }
            catch(Exception e){

            }
        });

        autoBid.addActionListener(actionEvent -> {
            try{
                autoBid.setEnabled(false);
                stopAutoBid.setEnabled(true);
                controller.autoBid(current_user.getUserId(), object.getObjectId());

            }
            catch(Exception e) {
            }
        });


        stopAutoBid.addActionListener(actionEvent -> {
            try{
                controller.stopAutoBid(current_user.getUserId());
                autoBid.setEnabled(true);
                stopAutoBid.setEnabled(false);
            }
            catch(Exception e) {
                System.out.println("Exception");
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
    public static void main(String[] args) {
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
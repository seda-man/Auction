package Client;

import Server.Models.User;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;
import Server.Models.Object;
import Server.ControllerInterface;

public class Client extends UnicastRemoteObject implements ClientInterface{
    private String status;
    public void notifyWin(){
        System.out.println("Congratulations you have won!");
        status = "done";
    }

    public void notifyLoose(){
        System.out.println("Unfortunately you have lost :(");
        status = "done";
    }

    public Client() throws Exception{

    }
    public static void main(String[] args) throws Exception {
        try {
            Client c = new Client();
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            {
                System.out.println("About to call hello");
                ControllerInterface controller = (ControllerInterface) Naming.lookup("rmi://localhost/controller");
                if (controller != null)
                    System.out.println("Looked up remote object");


                Scanner scanner = new Scanner(System.in);
                Object current_object;
                User current_user;
                String choice;

                while(true) {

                    System.out.println("If you wish to register please enter 'sign up', if you already have an account write 'sign in'");
                    choice = scanner.nextLine();
                    if (choice.equals("sign up")) {
                        System.out.println("please enter username");
                        String name = scanner.next();
                        scanner.nextLine();
                        controller.signUp(name);

                    }
                    if (choice.equals("sign in")) {
                        System.out.println("please enter username");
                        String name = scanner.next();
                        current_user = controller.signIn(name);
                        controller.setClient(c, current_user.getUserId());
                        c.status = "waiting";
                        System.out.println("Here is the list of objects, select which object you like to bid on.");
                        ArrayList<Integer> objIds = controller.getObjectIds();
                        for (int j = 0; j < objIds.size(); j++) {
                            System.out.println("Enter  " + j + "  For the following object");
                            controller.getObjectCatalog().getObject(objIds.get(j)).display();
                        }
                        int object = scanner.nextInt();

                        current_object = controller.getObjectCatalog().getObject(objIds.get(object));

                        System.out.println("Please enter your bid");
                        Float amount = scanner.nextFloat();

                        controller.makeBid(current_user.getUserId(), current_object.getObjectId(), amount);
                        break;
                    }
                }
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
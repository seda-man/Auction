import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Test
{

    public static void main(String[] args) {


        Controller controller = new Controller();


        Object obj1 = controller.getObjectCatalog().makeObject("Piano", "101");


        ObjectDescription desc1 = new ObjectDescription();
        desc1.setStartingBid(100.);
        desc1.setEstimatedPrice(200.);
        desc1.setDescription("A 19th century black wooden piano.");

        obj1.setObjectDescription(desc1);
        controller.addObjectId(obj1.getObjectId());

        Scanner scanner = new Scanner(System.in);
        Object current_object = obj1;
        User current_user;
        String choice;

        while(true){
            System.out.println("If you wish to register please enter 'sign up', if you already have an account write 'sign in'");
            choice = scanner.nextLine();
            if(choice.equals("sign up")){
                controller.signUp();
            }
            else if (choice.equals("sign in")) {
                current_user = controller.signIn();

                System.out.println("Here is the list of objects, select which object you like to bid on.");
                ArrayList<Integer> objIds = controller.getObjectIds();
                for(int j = 0; j< objIds.size(); j++){
                    System.out.println("Enter  " + j + "  For the following object");
                    controller.getObjectCatalog().getObject(objIds.get(j)).display();
                }
                int object = scanner.nextInt();
                while (object >objIds.size() - 1){
                    System.out.println("This is not a valid choice for object, please choose again");
                    object = scanner.nextInt();
                }
                current_object = controller.getObjectCatalog().getObject(objIds.get(object));

                System.out.println("Do you wish to make a bid?");

                controller.makeBid(current_user.getUserId(), current_object.getObjectId());
            }

            if(current_object.getBids().size() == 3) {
                controller.findWinner(current_object.getObjectId());
                break;
            }

        }
    }
}
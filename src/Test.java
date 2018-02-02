import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Test
{

    public static void main(String[] args) {



        Controller controller = new Controller();

        ObjectCatalog cat1 = new ObjectCatalog();
        Object obj1 = cat1.makeObject("Piano","101","onBid");


        ObjectDescription desc1 = new ObjectDescription();
        desc1.setStartingBid(100.);
        desc1.setEstimatedPrice(200.);
        desc1.setDescription("A 19th century black wooden piano.");

        obj1.setObjectDescription(desc1);


        System.out.println("The current object on bid is the following:");
        System.out.println("Object name: " + obj1.getName());
        ObjectDescription currentDescription = obj1.getObjectDescription();
        System.out.println("The starting bid: " + currentDescription.getStartingBid());
        System.out.println("The estimated price: " + currentDescription.getEstimatedPrice());
        System.out.println("The description: " + currentDescription.getDescription());
        System.out.println();
        for(int i=0; i<3; i++)
        {
            System.out.println("To sign in " + "as user N" + (i+1) + " please enter your username: ");
            Scanner scanner = new Scanner(System.in);
            String username = scanner.nextLine();
            System.out.println("User with username " + username + " was created.");


            System.out.println("Please enter your bid.");
            Double userBid = scanner.nextDouble();
            System.out.println("Your bid " + userBid + " was registered. Thank you.");


            try {
                File file = new File("bids.txt");
                FileWriter fileWriter = new FileWriter(file, true);
                fileWriter.write(username + ", " + userBid);
                fileWriter.write(System.getProperty( "line.separator" ));
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        controller.findWinner(0);


    }
}
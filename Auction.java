import Catalogs.ObjectCatalog;
import Catalogs.UserCatalog;
import Controlling.Controller;

public class Auction {
    private String name;

    private ObjectCatalog objectCatalog;
    private UserCatalog userCatalog;
    private Controller controller;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
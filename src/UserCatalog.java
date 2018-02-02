import java.util.ArrayList;

public class UserCatalog {
    private ArrayList<User> users = new ArrayList<User>();

    public User makeUser(String username) {
        int id = 0;
        if(users.size() > 0){
            id = users.get(users.size()).getUserId() +1;
        }
        User newUser = new User(username, id);
        users.add(newUser);
        return newUser;
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
    public User getUser(int id){
        for(int i = 0; i<users.size(); i++){
            if( users.get(i).getUserId() == id){
                return users.get(i);
            }
        }
        return  null;
    }




}
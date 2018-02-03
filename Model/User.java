package Model;

public class User {
    private String username;
    private int bidId;
    private int userId;

    public User(String username, int userId) {
        this.username = username;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void makeBid(Double bidAmount) {

    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public int getBidId() {
        return bidId;
    }

    public int getUserId() {
        return userId;
    }

}
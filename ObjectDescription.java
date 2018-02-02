public class ObjectDescription {
    private Double startingBid;
    private Double estimatedPrice;
    private String description;

    public void setStartingBid(Double bid) {
        this.startingBid = bid;
    }

    public Double getStartingBid() {
        return startingBid;
    }

    public void setEstimatedPrice(Double price) {
        this.estimatedPrice = price;
    }

    public Double getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
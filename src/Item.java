import java.io.Serializable;

public class Item implements Serializable {
    private String name;
    private String description;
    private String imagePath;
    private int quantity;
    private String status;
    private String client;

    // Constructor
    public Item(String name, String description, String client, String status,
                String imagePath, int quantity) {
        this.name = name;
        this.description = description;
        this.client = client;
        this.status = status;
        this.imagePath = imagePath;
        this.quantity = quantity;
    }

    // Getters
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getImagePath() {
        return imagePath;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getStatus() { return status; }
    public String getClient() { return client; }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public void setQuantity(int amount) {
        this.quantity = quantity;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setClient(String client) {
        this.client = client;
    }

}

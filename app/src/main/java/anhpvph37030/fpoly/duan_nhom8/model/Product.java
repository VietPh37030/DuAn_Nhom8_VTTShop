package anhpvph37030.fpoly.duan_nhom8.model;

public class Product {
    private String id;
    private String image;
    private String name;
    private String price;

    public Product() {
    }

    public Product(String id, String image, String name, String price) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}


package anhpvph37030.fpoly.duan_nhom8.model;

public class Product {
    private String id;
    private String image;
    private String name;
    private String price;
    private int quantity;// them truong du lieu
    private String hang;

    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
        this.hang = hang;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    private boolean isVisible; // Thêm trường đánh dấu sản phẩm có hiển thị hay không

    public Product() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product(String id, String image, String name, String price) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
    }
    public Product(String id, String image, String name, String price, int quantity,boolean isVisible) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.isVisible = isVisible;
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


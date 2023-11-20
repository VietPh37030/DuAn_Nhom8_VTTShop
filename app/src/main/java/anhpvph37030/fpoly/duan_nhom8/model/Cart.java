package anhpvph37030.fpoly.duan_nhom8.model;

public class Cart {
    private Product product;
    private int quantity;

    public Cart() {
        // Cần constructor mặc định để Firebase có thể deserializing dữ liệu
    }

    public Cart(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

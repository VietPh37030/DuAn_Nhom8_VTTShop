package anhpvph37030.fpoly.duan_nhom8.model;

public class ChiTietHoaDon {
    private String orderId;
    private String orderQuantity;
    private String orderSum;
    private String orderName;
    private String orderImage;

    public ChiTietHoaDon(String orderId, String orderQuantity, String orderSum, String orderName, String orderImage) {
        this.orderId = orderId;
        this.orderQuantity = orderQuantity;
        this.orderSum = orderSum;
        this.orderName = orderName;
        this.orderImage = orderImage;
    }

    // Các phương thức getter cho các thuộc tính
    public String getOrderId() {
        return orderId;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public String getOrderSum() {
        return orderSum;
    }

    public String getOrderName() {
        return orderName;
    }

    public String getOrderImage() {
        return orderImage;
    }
}

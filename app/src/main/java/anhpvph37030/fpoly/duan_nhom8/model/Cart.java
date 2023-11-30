package anhpvph37030.fpoly.duan_nhom8.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Cart implements Parcelable {
    private Product product;
    private int quantity;

    public Cart() {
        // Cần constructor mặc định để Firebase có thể deserializing dữ liệu
    }

    public Cart(Product product, int quantity1) {
        this.product = product;
        this.quantity = quantity1;
    }

    protected Cart(Parcel in) {
        product = in.readParcelable(Product.class.getClassLoader());
        quantity = in.readInt();
    }

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(product, flags);
        dest.writeInt(quantity);
    };

}

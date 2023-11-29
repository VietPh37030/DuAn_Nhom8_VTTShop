    package anhpvph37030.fpoly.duan_nhom8.model;

    import android.os.Parcel;
    import android.os.Parcelable;

    import androidx.annotation.NonNull;

    public class Product implements Parcelable {
        private String id;
        private String image;
        private String name;
        private String price;
        private int quantity1;// them truong du lieu
        private String hang;

        private int maDanhMuc;
        private String description;


        // Các phương thức khác...

        protected Product(Parcel in) {
            id = in.readString();
            image = in.readString();
            name = in.readString();
            price = in.readString();
            quantity1 = in.readInt();
            hang = in.readString();
            maDanhMuc = in.readInt();
            description = in.readString();
            isVisible = in.readByte() != 0;
        }

        public static final Creator<Product> CREATOR = new Creator<Product>() {
            @Override
            public Product createFromParcel(Parcel in) {
                return new Product(in);
            }

            @Override
            public Product[] newArray(int size) {
                return new Product[size];
            }
        };

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getMaDanhMuc() {
            return maDanhMuc;
        }

        public void setMaDanhMuc(int maDanhMuc) {
            this.maDanhMuc = maDanhMuc;
        }
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

        public int getQuantity1() {
            return quantity1;
        }

        public void setQuantity1(int quantity1) {
            this.quantity1 = quantity1;
        }

        public Product(String id, String image, String name, String price) {
            this.id = id;
            this.image = image;
            this.name = name;
            this.price = price;
        }

        public Product(String id, String image, String name, String price, int quantity1, int maDanhMuc, String description) {
            this.id = id;
            this.image = image;
            this.name = name;
            this.price = price;
            this.quantity1 = quantity1;
            this.maDanhMuc = maDanhMuc;
            this.description = description;
            this.isVisible = false;
        }


    //    public Product(String id, String image, String name, String price, int quantity1, int maDanhMuc,String description) {
    //        this.id = id;
    //        this.image = image;
    //        this.name = name;
    //        this.price = price;
    //        this.quantity1 = quantity1;
    //        this.maDanhMuc = maDanhMuc;
    //        this.description = description;
    //    }
    //    public Product(String id, String image, String name, String price, int quantity, String hang) {
    //        this.id = id;
    //        this.image = image;
    //        this.name = name;
    //        this.price = price;
    //        this.quantity = quantity;
    //        this.hang = hang;
    //    }

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(image);
            dest.writeString(name);
            dest.writeString(price);
            dest.writeInt(quantity1);
            dest.writeString(hang);
            dest.writeInt(maDanhMuc);
            dest.writeString(description);
            dest.writeByte((byte) (isVisible ? 1 : 0));
        }
    }


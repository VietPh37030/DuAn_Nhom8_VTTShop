package anhpvph37030.fpoly.duan_nhom8.model;

public class ThongTinDiaChi {
    private String id;
    private String hoTen;
    private String soDienThoai;
    private String diaChi;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public ThongTinDiaChi() {
        // Empty constructor needed for Firebase
    }


    public ThongTinDiaChi(String id,String hoTen, String soDienThoai, String diaChi) {
        this.id = id;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
    }
    public ThongTinDiaChi(String id,String hoTen, String soDienThoai, String diaChi,boolean isSelected) {
        this.id = id;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.isSelected = isSelected;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }
}

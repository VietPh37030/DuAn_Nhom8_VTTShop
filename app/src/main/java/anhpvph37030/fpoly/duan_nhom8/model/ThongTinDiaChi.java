package anhpvph37030.fpoly.duan_nhom8.model;

public class ThongTinDiaChi {
    private String id;
    private String hoTen;
    private String soDienThoai;
    private String diaChi;

    public ThongTinDiaChi() {
        // Empty constructor needed for Firebase
    }


    public ThongTinDiaChi(String id,String hoTen, String soDienThoai, String diaChi) {
        this.id = id;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
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

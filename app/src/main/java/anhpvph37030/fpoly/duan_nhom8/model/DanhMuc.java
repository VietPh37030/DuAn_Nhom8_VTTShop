package anhpvph37030.fpoly.duan_nhom8.model;

public class DanhMuc {
    private String urlSanPham;
    private String tenHang;

    public DanhMuc() {
    }

    public DanhMuc(String urlSanPham, String tenHang) {
        this.urlSanPham = urlSanPham;
        this.tenHang = tenHang;
    }

    public String getUrlSanPham() {
        return urlSanPham;
    }

    public void setUrlSanPham(String urlSanPham) {
        this.urlSanPham = urlSanPham;
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }
}

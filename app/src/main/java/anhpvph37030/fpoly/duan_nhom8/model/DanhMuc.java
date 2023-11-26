package anhpvph37030.fpoly.duan_nhom8.model;

import java.io.Serializable;

public class DanhMuc implements Serializable {
    private int maDanhMuc;
    private String urlSanPham;
    private String tenHang;

    public DanhMuc() {
    }

    public DanhMuc(int maDanhMuc, String urlSanPham, String tenHang) {
        this.maDanhMuc = maDanhMuc;
        this.urlSanPham = urlSanPham;
        this.tenHang = tenHang;
    }

    public int getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(int maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
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

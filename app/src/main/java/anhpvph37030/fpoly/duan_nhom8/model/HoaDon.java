package anhpvph37030.fpoly.duan_nhom8.model;

import java.util.Date;

public class HoaDon {
    private String maHoaDon;
    private String imageUrl;
    private String tenSanPham;
    private int soLuong;
    private int tongTien;
    private String nguoiNhan;
    private String sdt;
    private String diaChi;
    private String ngayDat;
    private int trangThai;

    private  ThongTinDiaChi thongTinDiaChi;

    public ThongTinDiaChi getThongTinDiaChi() {
        return thongTinDiaChi;
    }

    public void setThongTinDiaChi(ThongTinDiaChi thongTinDiaChi) {
        this.thongTinDiaChi = thongTinDiaChi;
    }

    public HoaDon() {
    }

    public HoaDon(String maHoaDon, String imageUrl, String tenSanPham, int soLuong, int tongTien, String nguoiNhan, String sdt, String diaChi, String ngayDat, int trangThai) {
        this.maHoaDon = maHoaDon;
        this.imageUrl = imageUrl;
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.tongTien = tongTien;
        this.nguoiNhan = nguoiNhan;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.ngayDat = ngayDat;
        this.trangThai = trangThai;
    }


    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    public String getNguoiNhan() {
        return nguoiNhan;
    }

    public void setNguoiNhan(String nguoiNhan) {
        this.nguoiNhan = nguoiNhan;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(String ngayDat) {
        this.ngayDat = ngayDat;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}

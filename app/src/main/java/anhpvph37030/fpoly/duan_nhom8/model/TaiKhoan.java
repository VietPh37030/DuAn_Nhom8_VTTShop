package anhpvph37030.fpoly.duan_nhom8.model;

public class TaiKhoan {
    private String email;
    private String password;

    public TaiKhoan() {
    }

    public TaiKhoan(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

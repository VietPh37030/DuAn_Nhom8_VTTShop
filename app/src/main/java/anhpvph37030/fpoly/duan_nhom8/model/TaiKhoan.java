package anhpvph37030.fpoly.duan_nhom8.model;

public class TaiKhoan {
    private String email;
    private String password;
    private boolean locked;

    public TaiKhoan() {
    }

    public TaiKhoan(String email, String password,boolean locked) {
        this.email = email;
        this.password = password;
        this.locked = false; // Mặc định là tài khoản không bị khóa
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

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}

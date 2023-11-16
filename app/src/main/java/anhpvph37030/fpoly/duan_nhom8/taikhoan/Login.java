package anhpvph37030.fpoly.duan_nhom8.taikhoan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import anhpvph37030.fpoly.duan_nhom8.MainActivity;
import anhpvph37030.fpoly.duan_nhom8.R;

public class Login extends AppCompatActivity {
    TextView txtchuacotaikoan;
    TextInputLayout txttk, txtmk;
    Button btndangnhap;
    CheckBox chkluumk;

    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mauth = FirebaseAuth.getInstance();
        anhxa();

        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangnhap();
            }
        });

        txtchuacotaikoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chuyensangsingup();
            }
        });
    }

    private void chuyensangsingup() {
        Intent intent = new Intent(Login.this, SignUp.class);
        startActivity(intent);
    }

    private void anhxa() {
        txtchuacotaikoan = findViewById(R.id.txtchuacotk);
        txttk = findViewById(R.id.edtUsername);
        txtmk = findViewById(R.id.edtPassword);
        btndangnhap = findViewById(R.id.btndangnhap);
        chkluumk = findViewById(R.id.chkLuuTK);

        // Kiểm tra xem có thông tin tài khoản đã lưu trong SharedPreferences không
        SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        boolean isChecked = preferences.getBoolean("isChecked", false);

        if (isChecked) {
            // Lấy thông tin tài khoản từ SharedPreferences và đặt giá trị cho EditText và CheckBox
            String savedEmail = preferences.getString("email", "");
            String savedPassword = preferences.getString("password", "");

            txttk.getEditText().setText(savedEmail);
            txtmk.getEditText().setText(savedPassword);
            chkluumk.setChecked(true);
        }
    }

    private void dangnhap() {
        String email = txttk.getEditText().getText().toString();
        String pass = txtmk.getEditText().getText().toString();

        if ((TextUtils.isEmpty(email) || TextUtils.isEmpty(pass))) {
            Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Đang đăng nhập...");

        // Show the progress dialog
        progressDialog.show();

        mauth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                            // Lưu tài khoản và mật khẩu nếu checkbox được chọn
                            saveCredentials(email, pass, chkluumk.isChecked());

                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Login.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveCredentials(String email, String password, boolean status) {
        // Sử dụng SharedPreferences để lưu tài khoản và mật khẩu
        SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (!status) {
            // xoa trang thai luu truoc do
            editor.clear();
        } else {
            editor.putString("email", email);
            editor.putString("password", password);
            editor.putBoolean("isChecked", status);
        }
        // luu lai toan bo du lieu
        editor.apply();
    }
}

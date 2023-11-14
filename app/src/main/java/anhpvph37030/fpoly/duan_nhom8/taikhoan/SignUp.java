package anhpvph37030.fpoly.duan_nhom8.taikhoan;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import anhpvph37030.fpoly.duan_nhom8.MainActivity;
import anhpvph37030.fpoly.duan_nhom8.R;

public class SignUp extends AppCompatActivity {

    private TextInputLayout edtTenDangNhap, edtHoTen, edtSoDienThoai, edtEmail, edtMatKhau, edtNhapLaiMatKhau;
    private Button btnDangKy;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        anhXa();
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangKy();
            }
        });
    }

    private void anhXa() {
        edtTenDangNhap = findViewById(R.id.edttendn);
        edtHoTen = findViewById(R.id.edthoten);
        edtSoDienThoai = findViewById(R.id.edtsdt);
        edtEmail = findViewById(R.id.edtemail);
        edtMatKhau = findViewById(R.id.edtPassword_login);
        edtNhapLaiMatKhau = findViewById(R.id.edtPassword_nhaplai_login);
        btnDangKy = findViewById(R.id.btndangki);
        progressDialog = new ProgressDialog(this);
    }

    private void dangKy() {
        String email = edtEmail.getEditText().getText().toString().trim();
        String password = edtMatKhau.getEditText().getText().toString().trim();

        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Sẽ mất một lúc vui lòng chờ");
        progressDialog.show();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Kiểm tra xem các trường nhập liệu có hợp lệ hay không
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(edtNhapLaiMatKhau.getEditText().getText().toString().trim())) {
            Toast.makeText(this, "Mật khẩu và nhập lại mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(SignUp.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
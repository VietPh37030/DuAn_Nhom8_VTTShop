package anhpvph37030.fpoly.duan_nhom8.Activities;// ChangePasswordActivity.java

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import anhpvph37030.fpoly.duan_nhom8.MainActivity;
import anhpvph37030.fpoly.duan_nhom8.R;

public class ChangePassActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseUser currentUser;

    private TextInputLayout edtOldPassword, edtNewPassword, edtConfirmPassword;
    private Button btnConfirm, btnChangePassword;


    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        edtOldPassword = findViewById(R.id.edt_changepass_mkc);
        edtNewPassword = findViewById(R.id.edt_changepass_mkm);
        edtConfirmPassword = findViewById(R.id.edt_changepass_nhaplaimk);
        btnConfirm = findViewById(R.id.btn_sendotp);
        btnChangePassword = findViewById(R.id.btnchangepass_thaydoi);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendConfirmationEmail();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = edtOldPassword.getEditText().getText().toString();
                String newPassword = edtNewPassword.getEditText().getText().toString();
                String confirmPassword = edtConfirmPassword.getEditText().getText().toString();

                // Kiểm tra mật khẩu cũ
                AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), oldPassword);
                currentUser.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Nếu xác thực thành công, tiến hành đổi mật khẩu
                                    performPasswordChange(newPassword, confirmPassword);
                                } else {
                                    Toast.makeText(ChangePassActivity.this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });


        Toolbar toolbar = findViewById(R.id.toolbardmk);
        setSupportActionBar(toolbar);

        // Đặt Navigation Icon là icon Exit
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitApp();
            }
        });
    }


    private void exitApp() {
       
        finish();

    }
    private void sendConfirmationEmail() {
        String email = currentUser.getEmail();
        // Gửi email xác nhận đến email người dùng
        currentUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ChangePassActivity.this, "Vui lòng xác nhận email trước khi đổi mật khẩu", Toast.LENGTH_SHORT).show();
                            // Ẩn trường nhập mật khẩu cũ và nút xác nhận
                            btnConfirm.setVisibility(View.GONE);
                            // Hiển thị trường nhập mật khẩu mới và nút thay đổi
                            edtNewPassword.setVisibility(View.VISIBLE);
                            edtConfirmPassword.setVisibility(View.VISIBLE);
                            btnChangePassword.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(ChangePassActivity.this, "Lỗi khi gửi email xác nhận", Toast.LENGTH_SHORT).show();
                            btnConfirm.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void changePassword() {
        String newPassword = edtNewPassword.getEditText().getText().toString();
        String confirmPassword = edtConfirmPassword.getEditText().getText().toString();

        if (newPassword.equals(confirmPassword)) {
            currentUser.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ChangePassActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ChangePassActivity.this, "Lỗi khi đổi mật khẩu", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(ChangePassActivity.this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
        }
    }
    private void performPasswordChange(String newPassword, String confirmPassword) {
        if (newPassword.equals(confirmPassword)) {
            currentUser.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ChangePassActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ChangePassActivity.this, "Lỗi khi đổi mật khẩu", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(ChangePassActivity.this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
        }
    }
}

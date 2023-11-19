package anhpvph37030.fpoly.duan_nhom8.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import anhpvph37030.fpoly.duan_nhom8.R;

public class ChangePassActivity extends AppCompatActivity {
    TextInputLayout txtmk, txtmkm, txtrepass;
    Button btnchangepass;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        init();
        btnchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickChange();
            }
        });
    }


    private void init() {
        progressDialog = new ProgressDialog(ChangePassActivity.this);
        txtmk = findViewById(R.id.edt_changepass_mkc);
        txtmkm = findViewById(R.id.edt_changepass_mkm);
        txtrepass = findViewById(R.id.edt_changepass_nhaplaimk);
        btnchangepass = findViewById(R.id.btnchangepass_thaydoi);
    }

    private void onclickChange() {
        String pass = txtmk.getEditText().getText().toString();
        String mkm = txtmkm.getEditText().getText().toString();
        String repass = txtrepass.getEditText().getText().toString();
        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), pass);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Mật khẩu cũ đúng, kiểm tra mật khẩu mới và nhập lại
                            if (mkm.equals(repass)) {
                                // Thực hiện thay đổi mật khẩu
                                progressDialog.setMessage("Đang thay đổi mật khẩu...");
                                progressDialog.show();

                                user.updatePassword(mkm)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(ChangePassActivity.this, "Thay Đổi Mật Khẩu Thành Công", Toast.LENGTH_SHORT).show();
                                                    finish(); // Kết thúc Activity sau khi thay đổi mật khẩu thành công
                                                } else {
                                                    Toast.makeText(ChangePassActivity.this, "Thay Đổi Mật Khẩu Thất Bại", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                // Mật khẩu mới và nhập lại không khớp
                                Toast.makeText(ChangePassActivity.this, "Mật khẩu mới và nhập lại không khớp", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Mật khẩu cũ không đúng
                            Toast.makeText(ChangePassActivity.this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
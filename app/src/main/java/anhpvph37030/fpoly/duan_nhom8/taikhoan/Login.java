package anhpvph37030.fpoly.duan_nhom8.taikhoan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import anhpvph37030.fpoly.duan_nhom8.MainActivity;
import anhpvph37030.fpoly.duan_nhom8.R;

public class Login extends AppCompatActivity {
    TextView txtchuacotaikoan;
    TextInputLayout txttk, txtmk;
    Button btndangnhap;
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
        Intent intent = new Intent(Login.this,SignUp.class);
        startActivity(intent);
    }

    private void anhxa() {
        txtchuacotaikoan = findViewById(R.id.txtchuacotk);
        txttk = findViewById(R.id.edtUsername);
        txtmk = findViewById(R.id.edtPassword);
        btndangnhap = findViewById(R.id.btndangnhap);
    }

    private void dangnhap() {
        String email = txttk.getEditText().getText().toString();
        String pass = txtmk.getEditText().getText().toString();
        if ((TextUtils.isEmpty(email) || TextUtils.isEmpty(pass))) {
            Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        mauth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(Login.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
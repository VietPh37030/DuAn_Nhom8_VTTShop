package anhpvph37030.fpoly.duan_nhom8.taikhoan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import anhpvph37030.fpoly.duan_nhom8.R;

public class manhinhchao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manhinhchao);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        }, 3000);
    }

    private void nextActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

    }
}
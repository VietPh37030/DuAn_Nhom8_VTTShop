package anhpvph37030.fpoly.duan_nhom8.taikhoan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

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
                Intent intent = new Intent(manhinhchao.this, Login.class);
                startActivity(intent);
            }
        }, 3000);
    }
}
package anhpvph37030.fpoly.duan_nhom8.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import anhpvph37030.fpoly.duan_nhom8.MainActivity;
import anhpvph37030.fpoly.duan_nhom8.R;

public class ChangeInfoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference userRef;
    private String currentUserId;

    Toolbar toolbar;
    ImageView imgavt;
    EditText edthovaten, edtemail;
    Button btnsubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        Toolbar toolbar = findViewById(R.id.toolbardoitt);
        setSupportActionBar(toolbar);
        initUI();

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitApp();
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });

        imgavt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void initUI() {
        imgavt = findViewById(R.id.imgavtinfo);
        edthovaten = findViewById(R.id.edt_change_hvt);
        edtemail = findViewById(R.id.edt_change_diachi);
        btnsubmit = findViewById(R.id.btnchange_thaydoi);

        // Hiển thị thông tin cá nhân hiện tại
        edthovaten.setText(currentUser.getDisplayName());
        edtemail.setText(currentUser.getEmail());
        // Load ảnh đại diện hiện tại (nếu có)
        // Glide.with(this).load(currentUser.getPhotoUrl()).placeholder(R.drawable.default_avatar).into(imgavt);
    }

    private void exitApp() {
        finish();
    }

    private void updateUserInfo() {
        String newDisplayName = edthovaten.getText().toString().trim();
        String newEmail = edtemail.getText().toString().trim();

        if (!newDisplayName.isEmpty()) {
            userRef.child("displayName").setValue(newDisplayName);
            currentUser.updateProfile(new UserProfileChangeRequest.Builder()
                    .setDisplayName(newDisplayName)
                    .build());
        }

        if (!newEmail.isEmpty()) {
            currentUser.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        userRef.child("email").setValue(newEmail);
                    }
                }
            });
        }

        // Hiển thị thông báo cập nhật thành công hoặc xử lý lỗi nếu cần
        Toast.makeText(this, "Thông tin đã được cập nhật", Toast.LENGTH_SHORT).show();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            updateProfileImage(imageUri);
        }
    }

    private void updateProfileImage(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profile_images").child(currentUserId + ".jpg");

        storageRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            userRef.child("profileImage").setValue(imageUrl);
                            currentUser.updateProfile(new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(uri)
                                    .build());
                            // Hiển thị ảnh đại diện mới (nếu muốn)
                            // Glide.with(ChangeInfoActivity.this).load(uri).placeholder(R.drawable.default_avatar).into(imgavt);
                        }
                    });
                }
            }
        });
    }
}

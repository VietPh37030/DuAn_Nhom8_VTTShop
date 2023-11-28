package anhpvph37030.fpoly.duan_nhom8.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import anhpvph37030.fpoly.duan_nhom8.Activities.ChangeInfoActivity;
import anhpvph37030.fpoly.duan_nhom8.Activities.ChangePassActivity;
import anhpvph37030.fpoly.duan_nhom8.Activities.DiaChiActi;
import anhpvph37030.fpoly.duan_nhom8.Activities.HoaDonActivity;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.taikhoan.Login;

public class CaNhanFrg extends Fragment {
    ImageView btneditdiachi;
    ImageView imgavt;
    TextView txtht, txtemailcn;
    TextView btndangxuat;
    TextView cthaydoimk;
    TextView cthaydoithongtin;
    TextView cthayHoadon;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference userRef;

    public CaNhanFrg() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ca_nhan, container, false);

        imgavt = v.findViewById(R.id.imageView2);
        txtht = v.findViewById(R.id.txtttHoten);
        txtemailcn = v.findViewById(R.id.txtttemail);
        cthaydoimk = v.findViewById(R.id.nav_repasswork);
        cthaydoithongtin = v.findViewById(R.id.nav_Bill_ltru);
        btndangxuat = v.findViewById(R.id.btndangxuat);
        btneditdiachi = v.findViewById(R.id.btneditdiachi);
        cthayHoadon = v.findViewById(R.id.btnHoaDonflash);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());

        showUserInfo();

        btndangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Lưu ý");
                builder.setIcon(R.drawable.thongbao);
                builder.setMessage("Bạn có muốn đăng xuất không?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        Toast.makeText(getActivity(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), Login.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });
                builder.show();
            }
        });

        cthaydoimk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePassActivity.class);
                startActivity(intent);
            }
        });

        cthaydoithongtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangeInfoActivity.class);
                startActivity(intent);
            }
        });
        cthayHoadon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HoaDonActivity.class);
                startActivity(intent);
            }
        });
        btneditdiachi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DiaChiActi.class);
                startActivity(intent);
            }
        });

        imgavt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        return v;
    }

    private void showUserInfo() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("displayName").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String profileImage = snapshot.child("profileImage").getValue(String.class);

                    txtht.setText(name);
                    txtemailcn.setText(email);

                    // Sử dụng Glide để tải và hiển thị ảnh đại diện
                    if (profileImage != null && !profileImage.isEmpty()) {
                        Glide.with(CaNhanFrg.this).load(profileImage).placeholder(R.drawable.avt).into(imgavt);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra trong truy vấn dữ liệu
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            updateProfileImage(imageUri);
        }
    }

    private void updateProfileImage(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profile_images").child(currentUser.getUid() + ".jpg");

        storageRef.putFile(imageUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    userRef.child("profileImage").setValue(imageUrl);
                    currentUser.updateProfile(new UserProfileChangeRequest.Builder()
                            .setPhotoUri(uri)
                            .build());

                    Glide.with(this).load(uri).placeholder(R.drawable.avt).into(imgavt);
                    Toast.makeText(getActivity(), "Ảnh đại diện đã được cập nhật", Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(getActivity(), "Có lỗi xảy ra khi cập nhật ảnh", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

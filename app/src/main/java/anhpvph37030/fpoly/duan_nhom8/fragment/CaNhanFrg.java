package anhpvph37030.fpoly.duan_nhom8.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.taikhoan.Login;


public class CaNhanFrg extends Fragment {
    ImageView imgavt;
    TextView txtht, txtemailcn;
    Button btndangxuat;

    public CaNhanFrg() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ca_nhan, container, false);
        imgavt = (ImageView) v.findViewById(R.id.imageView2);
        txtht = (TextView) v.findViewById(R.id.txtttHoten);
        txtemailcn = v.findViewById(R.id.txtttemail);
        btndangxuat = v.findViewById(R.id.btndangxuat);
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
                        Toast.makeText(getActivity(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), Login.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
        return v;

    }


    private void showUserInfo() {
        // Lấy thông tin người dùng hiện tại từ FirebaseAuth
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Nếu không có người dùng hiện tại, hàm sẽ kết thúc
        if (user == null) {
            return;
        }

        // Lấy thông tin người dùng từ đối tượng FirebaseUser, bao gồm tên, email và URL ảnh đại diện
        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        // Nếu tên người dùng là null, ẩn TextView hiển thị tên
        if (name == null) {
            txtht.setVisibility(View.GONE);
        } else {
            // Nếu tên người dùng không phải là null, hiển thị TextView và đặt tên người dùng
            txtht.setVisibility(View.VISIBLE);
            txtht.setText(name);
        }

        // Đặt email người dùng cho TextView txtemailcn
        txtemailcn.setText(email);

        // Sử dụng thư viện Glide để tải ảnh đại diện từ URL và hiển thị nó trong ImageView imgavt
        // Nếu không tải được ảnh, sẽ hiển thị ảnh mặc định (R.drawable.avt)
        Glide.with(this).load(photoUrl).error(R.drawable.avt).into(imgavt);
    }


}
package anhpvph37030.fpoly.duan_nhom8.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import anhpvph37030.fpoly.duan_nhom8.Adapter.DanhMucAdapter;
import anhpvph37030.fpoly.duan_nhom8.Adapter.ProductAdminAdapter;
import anhpvph37030.fpoly.duan_nhom8.DAO.DanhMucDAO;
import anhpvph37030.fpoly.duan_nhom8.R;
import anhpvph37030.fpoly.duan_nhom8.model.DanhMuc;
import anhpvph37030.fpoly.duan_nhom8.model.Product;

public class AdminQL extends Fragment {

    ListView lstadminql;
    ImageButton btnThem;
    SearchView searchView;
    private DatabaseReference productsRef;
    private ProductAdminAdapter productAdminAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_admin, container, false);
        searchView = view.findViewById(R.id.search_view);
        lstadminql = view.findViewById(R.id.lstadmin);
        btnThem = view.findViewById(R.id.btnThem);
        ListView listView = view.findViewById(R.id.lstadmin);
        ImageButton btnThem = view.findViewById(R.id.btnThem);

        productsRef = FirebaseDatabase.getInstance().getReference().child("products");

        // Khởi tạo Adapter
        productAdminAdapter = new ProductAdminAdapter(getContext(), new ArrayList<>());
        listView.setAdapter(productAdminAdapter);
        // Xử lý sự kiện khi thanh tìm kiếm thay đổi
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Xử lý khi người dùng nhấn Enter trên bàn phím
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Xử lý khi người dùng thay đổi nội dung trong thanh tìm kiếm
                return false;
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện ở đây
                openThemSanPhamDialog();
            }
        });
        productAdminAdapter.setEditSanPhamListener(new ProductAdminAdapter.EditSanPhamListener() {
            @Override
            public void onEditSanPham(Product product) {
                openEditSanPhamDialog(product);
            }
        });


        // Lắng nghe sự kiện khi có thay đổi trong dữ liệu
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Product> productList = new ArrayList<>();

                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productList.add(product);
                }

                // Cập nhật dữ liệu mới cho Adapter
                productAdminAdapter.clear();
                productAdminAdapter.addAll(productList);
                productAdminAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi truy cập cơ sở dữ liệu
                Toast.makeText(getContext(), "Lỗi khi truy cập cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện khi người dùng chọn một sản phẩm
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy sản phẩm được chọn từ danh sách
                Product selectedProduct = productAdminAdapter.getItem(position);

                // Xử lý khi người dùng chọn một sản phẩm
                // ...

            }
        });

        return view;
    }

    private void openThemSanPhamDialog() {
        // Inflate layout cho Dialog
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_sanpham, null);

        // Khởi tạo controls trong Dialog
        EditText edIdSp = dialogView.findViewById(R.id.ed_idSp);
        EditText edURL = dialogView.findViewById(R.id.ed_URL_SanPham1);
        EditText edTenTL = dialogView.findViewById(R.id.ed_TenTL);
        EditText edGia = dialogView.findViewById(R.id.ed_Gia);
        EditText edSoLuong = dialogView.findViewById(R.id.ed_soLuong);
        Spinner spnHang = dialogView.findViewById(R.id.spnhang);
        EditText edMoTa = dialogView.findViewById(R.id.ed_MoTa); // Add this line for description
        Button btnThemSanPham = dialogView.findViewById(R.id.btnAdd);

        // Khởi tạo Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Lấy dữ liệu hãng sản phẩm từ Firebase và thiết lập Spinner
        setupSpinnerHang(spnHang);

        // Thiết lập sự kiện khi nhấn nút "Thêm Sản Phẩm"
        btnThemSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin từ các controls
                String idSp = edIdSp.getText().toString().trim();
                String urlSanPham = edURL.getText().toString().trim();
                String tenTL = edTenTL.getText().toString().trim();
                String gia = edGia.getText().toString().trim();
                String soLuongStr = edSoLuong.getText().toString().trim();
                String selectedHang = spnHang.getSelectedItem().toString();
                String moTa = edMoTa.getText().toString().trim(); // Get description

                // Kiểm tra và thêm sản phẩm vào Firebase
                if (!TextUtils.isEmpty(idSp) && !TextUtils.isEmpty(urlSanPham)
                        && !TextUtils.isEmpty(tenTL) && !TextUtils.isEmpty(gia)
                        && !TextUtils.isEmpty(soLuongStr) && !TextUtils.isEmpty(selectedHang)) {

                    try {
                        // Chuyển đổi soLuong từ String sang int
                        int soLuong = Integer.parseInt(soLuongStr);

                        // Kiểm tra xem sản phẩm đã tồn tại hay chưa
                        DatabaseReference productNodeRef = productsRef.child(idSp);
                        productNodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Toast.makeText(getContext(), "Sản phẩm đã tồn tại", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Sử dụng phương thức mới để lấy mã danh mục
                                    getMaDanhMuc(selectedHang, new OnMaDanhMucListener() {
                                        @Override
                                        public void onMaDanhMucSuccess(int maDanhMuc) {
                                            // Sản phẩm chưa tồn tại, thêm vào Firebase
                                            Product newProduct = new Product(idSp, urlSanPham, tenTL, gia, soLuong, maDanhMuc,moTa);
                                            productsRef.child(idSp).setValue(newProduct);

                                            // Đóng Dialog sau khi thêm
                                            alertDialog.dismiss();
                                        }

                                        @Override
                                        public void onMaDanhMucNotFound() {
                                            Toast.makeText(getContext(), "Không tìm thấy mã danh mục", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onMaDanhMucError(String errorMessage) {
                                            Toast.makeText(getContext(), "Lỗi khi lấy mã danh mục: " + errorMessage, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Xử lý lỗi nếu cần
                            }
                        });
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button btnHuy = dialogView.findViewById(R.id.btnHuy);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void openEditSanPhamDialog(Product selectedProduct) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_update_sanpham, null);

        // Khởi tạo controls trong Dialog
        EditText edURL = dialogView.findViewById(R.id.ed_URL_SanPham1);
        EditText edTenTL = dialogView.findViewById(R.id.ed_TenTL);
        EditText edGia = dialogView.findViewById(R.id.ed_Gia);
        EditText edSoLuong = dialogView.findViewById(R.id.ed_soLuong);
        Spinner spnHang = dialogView.findViewById(R.id.spnhang);
        EditText edMoTa = dialogView.findViewById(R.id.ed_MoTa); // Add this line for description
        Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);

        // Khởi tạo Dialog
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Lấy dữ liệu hãng sản phẩm từ Firebase và thiết lập Spinner
        setupSpinnerHang(spnHang);

        // Thiết lập giá trị ban đầu cho các controls từ selectedProduct
        edURL.setText(selectedProduct.getImage());
        edTenTL.setText(selectedProduct.getName());
        edGia.setText(selectedProduct.getPrice());
        edSoLuong.setText(String.valueOf(selectedProduct.getQuantity1()));
        edMoTa.setText(selectedProduct.getDescription()); // Set description

        // Thiết lập sự kiện khi nhấn nút "Cập Nhật"
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin từ các controls
                String idSp = selectedProduct.getId(); // Use the ID from the selectedProduct
                String urlSanPham = edURL.getText().toString().trim();
                String tenTL = edTenTL.getText().toString().trim();
                String gia = edGia.getText().toString().trim();
                String soLuongStr = edSoLuong.getText().toString().trim();
                String selectedHang = spnHang.getSelectedItem().toString();
                String moTa = edMoTa.getText().toString().trim(); // Get description

                // Kiểm tra và cập nhật sản phẩm vào Firebase
                if (!TextUtils.isEmpty(idSp) && !TextUtils.isEmpty(urlSanPham)
                        && !TextUtils.isEmpty(tenTL) && !TextUtils.isEmpty(gia)
                        && !TextUtils.isEmpty(soLuongStr) && !TextUtils.isEmpty(selectedHang)) {

                    try {
                        // Chuyển đổi soLuong từ String sang int
                        int soLuong = Integer.parseInt(soLuongStr);

                        // Sử dụng phương thức mới để lấy mã danh mục
                        getMaDanhMuc(selectedHang, new OnMaDanhMucListener() {
                            @Override
                            public void onMaDanhMucSuccess(int maDanhMuc) {
                                // Cập nhật thông tin sản phẩm vào Firebase
                                Product updatedProduct = new Product(idSp, urlSanPham, tenTL, gia, soLuong, maDanhMuc, moTa);
                                productsRef.child(idSp).setValue(updatedProduct);

                                // Đóng Dialog sau khi cập nhật
                                alertDialog.dismiss();
                            }

                            @Override
                            public void onMaDanhMucNotFound() {
                                Toast.makeText(getContext(), "Không tìm thấy mã danh mục", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onMaDanhMucError(String errorMessage) {
                                Toast.makeText(getContext(), "Lỗi khi lấy mã danh mục: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button btnHuy = dialogView.findViewById(R.id.btnHuyUpdate);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


    private void setupSpinnerHang(Spinner spnHang) {
        // Khởi tạo danh sách hãng sản phẩm từ danh mục
        List<String> danhSachHang = new ArrayList<>();

        // Lấy dữ liệu hãng sản phẩm từ Firebase và thiết lập Spinner
        DanhMucDAO danhMucDAO = new DanhMucDAO();
        DatabaseReference danhMucRef = danhMucDAO.getDanhMucRef();

        danhMucRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                danhSachHang.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DanhMuc danhMuc = snapshot.getValue(DanhMuc.class);
                    if (danhMuc != null) {
                        danhSachHang.add(danhMuc.getTenHang());
                    }
                }

                // Tạo Adapter cho Spinner và thiết lập dữ liệu
                ArrayAdapter<String> hangAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, danhSachHang);
                hangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnHang.setAdapter(hangAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private void getMaDanhMuc(String tenHang, OnMaDanhMucListener listener) {
        DatabaseReference danhMucRef = FirebaseDatabase.getInstance().getReference().child("danhmuc");
        Query query = danhMucRef.orderByChild("tenHang").equalTo(tenHang);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lấy mã danh mục từ dataSnapshot
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        DanhMuc danhMuc = snapshot.getValue(DanhMuc.class);
                        if (danhMuc != null) {
                            int maDanhMuc = danhMuc.getMaDanhMuc();
                            Log.d("DEBUG", "Found maDanhMuc: " + maDanhMuc);

                            // Gọi hàm thêm sản phẩm vào Firebase ở đây với maDanhMuc
                            listener.onMaDanhMucSuccess(maDanhMuc);
                            return;
                        }
                    }
                }

                // Xử lý khi không tìm thấy tên hãng trong danh mục
                Log.d("DEBUG", "No matching danhMuc found for tenHang: " + tenHang);
                listener.onMaDanhMucNotFound();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
                Log.e("ERROR", "Firebase query cancelled with error: " + databaseError.getMessage());
                listener.onMaDanhMucError(databaseError.getMessage());
            }
        });
    }

    // Interface để xử lý sự kiện khi có kết quả từ Firebase
    interface OnMaDanhMucListener {
        void onMaDanhMucSuccess(int maDanhMuc);

        void onMaDanhMucNotFound();

        void onMaDanhMucError(String errorMessage);
    }
}

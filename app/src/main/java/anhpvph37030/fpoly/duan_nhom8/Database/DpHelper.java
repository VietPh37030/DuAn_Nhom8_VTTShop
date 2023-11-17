package anhpvph37030.fpoly.duan_nhom8.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DpHelper extends SQLiteOpenHelper {
    public DpHelper(@Nullable Context context) {
        super(context, "VTT.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableproduct = "CREATE TABLE product(MA_SP INTEGER PRIMARY KEY ," +
                "TENSANPHAM TEXT," +
                "GIA INT," +
                "ANH INT)";
            db.execSQL(tableproduct);
        String tableproductdetail = "CREATE TABLE productdetail(MA_CHITIET_SP INTEGER PRIMARY KEY ," +
                "MAUSAC TEXT," +
                "THONGSO TEXT," +
                "MOTA TEXT," +
                "HANGSANXUAT TEXT)";
        db.execSQL(tableproductdetail);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion){
            db.execSQL("drop table if exists product");
            db.execSQL("drop table if exists productdetail");
            onCreate(db);
        }
    }
}

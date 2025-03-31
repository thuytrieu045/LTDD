package com.sinhvien.doan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TEN_DATABASE = "BakingRecipeApp.db";
    private static final int DATABASE_VERSION = 4; // Tăng version để kích hoạt onUpgrade

    // Bảng Users
    public static final String BANG_USERS = "users";
    public static final String COT_USER_ID = "user_id";
    public static final String COT_FIREBASE_UID = "firebase_uid";
    public static final String COT_USERNAME = "username";

    // Bảng Recipes
    public static final String BANG_RECIPES = "recipes";
    public static final String COT_RECIPE_ID = "recipe_id";
    public static final String COT_TEN_RECIPE = "recipe_name";
    public static final String COT_INGREDIENTS = "ingredients";
    public static final String COT_STEPS = "steps";
    public static final String COT_IMG_URL = "img_src";
    public static final String COT_CATEGORY = "cate";
    public static final String COT_TIME = "time";
    public static final String COT_DOKHO = "difficulty";

    // Bảng Favorite (Lưu công thức yêu thích của user)
    public static final String BANG_FAVORITE = "favorite";
    public static final String COT_FAVORITE_USER_ID = "user_id";
    public static final String COT_FAVORITE_RECIPE_ID = "recipe_id";

    // Chuỗi tạo bảng Users với các cột thanh toán
    private static final String CREATE_BANG_USERS = "CREATE TABLE " + BANG_USERS + " (" +
            COT_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COT_FIREBASE_UID + " TEXT UNIQUE NOT NULL, " +
            COT_USERNAME + " TEXT, " +
            "momo_number TEXT, " +
            "zalopay_number TEXT, " +
            "vietcombank_account TEXT, " +
            "mbbank_account TEXT, " +
            "vietinbank_account TEXT)";

    private static final String CREATE_BANG_RECIPES = "CREATE TABLE " + BANG_RECIPES + " (" +
            COT_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            COT_TEN_RECIPE + " TEXT NOT NULL, " +
            COT_INGREDIENTS + " TEXT NOT NULL, " +
            COT_STEPS + " TEXT NOT NULL, " +
            COT_USER_ID + " INTEGER NOT NULL, " +
            COT_IMG_URL + " TEXT, " +
            COT_CATEGORY + " INTEGER NOT NULL, " +
            COT_TIME + " INTEGER NOT NULL, " +
            COT_DOKHO + " TEXT NOT NULL, " +
            "FOREIGN KEY(" + COT_USER_ID + ") REFERENCES " + BANG_USERS + "(" + COT_USER_ID + "))";

    // Tạo bảng Favorite (Lưu công thức yêu thích)
    private static final String CREATE_BANG_FAVORITE = "CREATE TABLE " + BANG_FAVORITE + " (" +
            COT_FAVORITE_USER_ID + " INTEGER NOT NULL, " +
            COT_FAVORITE_RECIPE_ID + " INTEGER NOT NULL, " +
            "PRIMARY KEY (" + COT_FAVORITE_USER_ID + ", " + COT_FAVORITE_RECIPE_ID + "), " +
            "FOREIGN KEY(" + COT_FAVORITE_USER_ID + ") REFERENCES " + BANG_USERS + "(" + COT_USER_ID + ") ON DELETE CASCADE, " +
            "FOREIGN KEY(" + COT_FAVORITE_RECIPE_ID + ") REFERENCES " + BANG_RECIPES + "(" + COT_RECIPE_ID + ") ON DELETE CASCADE)";

    public DatabaseHelper(Context context) {
        super(context, TEN_DATABASE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BANG_USERS);
        db.execSQL(CREATE_BANG_RECIPES);
        db.execSQL(CREATE_BANG_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Thêm các cột mới vào bảng users
            db.execSQL("ALTER TABLE " + BANG_USERS + " ADD COLUMN momo_number TEXT");
            db.execSQL("ALTER TABLE " + BANG_USERS + " ADD COLUMN zalopay_number TEXT");
            db.execSQL("ALTER TABLE " + BANG_USERS + " ADD COLUMN vietcombank_account TEXT");
            db.execSQL("ALTER TABLE " + BANG_USERS + " ADD COLUMN mbbank_account TEXT");
            db.execSQL("ALTER TABLE " + BANG_USERS + " ADD COLUMN vietinbank_account TEXT");
        }

        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + BANG_USERS + " ADD COLUMN " + COT_USERNAME + " TEXT");

        }

        if (oldVersion < 4) {
            db.execSQL(CREATE_BANG_FAVORITE);
        }
    }

    public void deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("users", "id = ?", new String[]{String.valueOf(userId)});
        db.close();
    }

    public int getUserId(String firebaseUid) {
        SQLiteDatabase db = this.getWritableDatabase();
        int userId = -1;

        Cursor cursor = db.rawQuery("SELECT user_id FROM users WHERE firebase_uid = ?", new String[]{firebaseUid});
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        } else {
            ContentValues values = new ContentValues();
            values.put("firebase_uid", firebaseUid);
            long newUserId = db.insert("users", null, values);
            if (newUserId != -1) {
                userId = (int) newUserId;
            }
        }
        cursor.close();
        return userId;
    }


    public int getRecipeId(String recipeName) {
        SQLiteDatabase db = this.getReadableDatabase();
        int ID = -1;
        Cursor cursor = db.rawQuery("SELECT recipe_id FROM recipes WHERE recipe_name = ?", new String[]{recipeName});
        if (cursor.moveToFirst()) {
            ID = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return ID;
    }


    public void updatePaymentInfo(int userId, String momo, String zalopay, String vietcombank, String mbbank, String vietinbank) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("momo_number", momo);
        values.put("zalopay_number", zalopay);
        values.put("vietcombank_account", vietcombank);
        values.put("mbbank_account", mbbank);
        values.put("vietinbank_account", vietinbank);
        db.update(BANG_USERS, values, COT_USER_ID + " = ?", new String[]{String.valueOf(userId)});
    }

    // Hàm cập nhật username
    public void updateUsername(int userId, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COT_USERNAME, username);
        db.update(BANG_USERS, values, COT_USER_ID + " = ?", new String[]{String.valueOf(userId)});
    }

    // Thêm công thức vào danh sách yêu thích
    public boolean addFavorite(int userId, int recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COT_FAVORITE_USER_ID, userId);
        values.put(COT_FAVORITE_RECIPE_ID, recipeId);

        long result = db.insert(BANG_FAVORITE, null, values);
        return result != -1;
    }

    // Xóa công thức khỏi danh sách yêu thích
    public boolean removeFavorite(int userId, int recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(BANG_FAVORITE, COT_FAVORITE_USER_ID + " = ? AND " + COT_FAVORITE_RECIPE_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(recipeId)});
        return result > 0;
    }

    // Kiểm tra xem công thức có trong danh sách yêu thích không
    public boolean isFavorite(int userId, int recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + BANG_FAVORITE + " WHERE " +
                        COT_FAVORITE_USER_ID + " = ? AND " + COT_FAVORITE_RECIPE_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(recipeId)});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Lấy danh sách công thức yêu thích của một user
    public Cursor getUserFavorites(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + BANG_RECIPES + " INNER JOIN " + BANG_FAVORITE +
                        " ON " + BANG_RECIPES + "." + COT_RECIPE_ID + " = " + BANG_FAVORITE + "." + COT_FAVORITE_RECIPE_ID +
                        " WHERE " + BANG_FAVORITE + "." + COT_FAVORITE_USER_ID + " = ?",
                new String[]{String.valueOf(userId)});
    }
}
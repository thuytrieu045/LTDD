package com.sinhvien.doan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "RecipeDB";
    private static final int DATABASE_VERSION = 1;

    // Bảng Recipe
    public static final String TABLE_RECIPE = "Recipe";
    public static final String COT_RECIPE_ID = "recipe_id";
    public static final String COT_TEN_RECIPE = "ten_recipe";
    public static final String COT_INGREDIENTS = "ingredients";
    public static final String COT_STEPS = "steps";
    public static final String COT_USER_ID = "user_id";
    public static final String COT_IMG_URL = "img_url";
    public static final String COT_CATEGORY = "category";
    public static final String COT_TIME = "time";
    public static final String COT_DOKHO = "dokho";

    // Bảng Favorite Products
    public static final String TABLE_FAVORITE_PRODUCTS = "FavoriteProducts";
    public static final String COT_PRODUCT_ID = "product_id";
    public static final String COT_PRODUCT_NAME = "product_name";
    public static final String COT_PRODUCT_DESC = "product_desc";
    public static final String COT_PRODUCT_IMAGE = "product_image";

    // Bảng Favorite Recipes
    public static final String TABLE_FAVORITE_RECIPES = "FavoriteRecipes";
    public static final String COT_FAV_RECIPE_ID = "recipe_id";
    public static final String COT_FAV_RECIPE_NAME = "recipe_name";
    public static final String COT_FAV_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String COT_FAV_RECIPE_IMAGE = "recipe_image";

    public MyDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Recipe
        String createRecipeTable = "CREATE TABLE " + TABLE_RECIPE + " (" +
                COT_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COT_TEN_RECIPE + " TEXT, " +
                COT_INGREDIENTS + " TEXT, " +
                COT_STEPS + " TEXT, " +
                COT_USER_ID + " INTEGER, " +
                COT_IMG_URL + " TEXT, " +
                COT_CATEGORY + " INTEGER, " +
                COT_TIME + " INTEGER, " +
                COT_DOKHO + " TEXT)";
        db.execSQL(createRecipeTable);

        // Tạo bảng Favorite Products
        String createFavoriteProductsTable = "CREATE TABLE " + TABLE_FAVORITE_PRODUCTS + " (" +
                COT_PRODUCT_ID + " TEXT PRIMARY KEY, " +
                COT_PRODUCT_NAME + " TEXT, " +
                COT_PRODUCT_DESC + " TEXT, " +
                COT_PRODUCT_IMAGE + " INTEGER)";
        db.execSQL(createFavoriteProductsTable);

        // Tạo bảng Favorite Recipes
        String createFavoriteRecipesTable = "CREATE TABLE " + TABLE_FAVORITE_RECIPES + " (" +
                COT_FAV_RECIPE_ID + " INTEGER PRIMARY KEY, " +
                COT_FAV_RECIPE_NAME + " TEXT, " +
                COT_FAV_RECIPE_INGREDIENTS + " TEXT, " +
                COT_FAV_RECIPE_IMAGE + " TEXT)";
        db.execSQL(createFavoriteRecipesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE_RECIPES);
        onCreate(db);
    }

    // Các phương thức xử lý yêu thích sản phẩm
    public void addToFavorites(String productId, String name, String description, int imageResource) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COT_PRODUCT_ID, productId);
        values.put(COT_PRODUCT_NAME, name);
        values.put(COT_PRODUCT_DESC, description);
        values.put(COT_PRODUCT_IMAGE, imageResource);
        db.insert(TABLE_FAVORITE_PRODUCTS, null, values);
    }

    public void removeFromFavorites(String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITE_PRODUCTS, COT_PRODUCT_ID + "=?", new String[]{productId});
    }

    public boolean isProductFavorite(String productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITE_PRODUCTS, null,
                COT_PRODUCT_ID + "=?", new String[]{productId},
                null, null, null);
        boolean isFavorite = cursor.getCount() > 0;
        cursor.close();
        return isFavorite;
    }

    // Các phương thức xử lý yêu thích công thức
    public void addRecipeToFavorites(int recipeId, String name, String ingredients, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COT_FAV_RECIPE_ID, recipeId);
        values.put(COT_FAV_RECIPE_NAME, name);
        values.put(COT_FAV_RECIPE_INGREDIENTS, ingredients);
        values.put(COT_FAV_RECIPE_IMAGE, imageUrl);
        db.insert(TABLE_FAVORITE_RECIPES, null, values);
    }

    public void removeRecipeFromFavorites(int recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITE_RECIPES, COT_FAV_RECIPE_ID + "=?", new String[]{String.valueOf(recipeId)});
    }

    public boolean isRecipeFavorite(int recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITE_RECIPES, null,
                COT_FAV_RECIPE_ID + "=?", new String[]{String.valueOf(recipeId)},
                null, null, null);
        boolean isFavorite = cursor.getCount() > 0;
        cursor.close();
        return isFavorite;
    }

    // Các phương thức khác
    public Cursor getRecipeByCategory(int category) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_RECIPE, null,
                COT_CATEGORY + "=?", new String[]{String.valueOf(category)},
                null, null, null);
    }

    public Cursor layTatCaDuLieu() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_RECIPE, null, null, null, null, null, null);
    }

    public long them(Recipe recipe) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COT_TEN_RECIPE, recipe.getRecipeName());
        values.put(COT_INGREDIENTS, recipe.getIngredients());
        values.put(COT_STEPS, recipe.getSteps());
        values.put(COT_USER_ID, recipe.getUserId());
        values.put(COT_IMG_URL, recipe.getImgSource());
        values.put(COT_CATEGORY, recipe.getCategory_id());
        values.put(COT_TIME, recipe.getTime());
        values.put(COT_DOKHO, recipe.getDifficulty());

        return database.insert(TABLE_RECIPE, null, values);
    }

    public long xoa(int recipeId) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(
                TABLE_RECIPE,
                COT_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)}
        );
    }

    public long sua(Recipe recipe) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COT_TEN_RECIPE, recipe.getRecipeName());
        values.put(COT_INGREDIENTS, recipe.getIngredients());
        values.put(COT_STEPS, recipe.getSteps());
        values.put(COT_IMG_URL, recipe.getImgSource());
        values.put(COT_TIME, recipe.getTime());
        values.put(COT_DOKHO, recipe.getDifficulty());
        values.put(COT_CATEGORY, recipe.getCategory_id());

        return database.update(
                TABLE_RECIPE,
                values,
                COT_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipe.getRecipeId())}
        );
    }

    // Chọn lấy công thức theo recipe_id
    public Recipe getRecipeById(int recipeId) {
        SQLiteDatabase database = this.getReadableDatabase();
        Recipe recipe = null;

        Cursor cursor = database.query(
                TABLE_RECIPE,
                new String[]{
                        COT_RECIPE_ID,
                        COT_TEN_RECIPE,
                        COT_INGREDIENTS,
                        COT_STEPS,
                        COT_USER_ID,
                        COT_IMG_URL,
                        COT_CATEGORY,
                        COT_TIME,
                        COT_DOKHO
                },
                COT_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            recipe = new Recipe(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COT_RECIPE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COT_TEN_RECIPE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COT_INGREDIENTS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COT_STEPS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COT_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COT_IMG_URL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COT_CATEGORY)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COT_TIME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COT_DOKHO))
            );
            cursor.close();
        }
        return recipe;
    }

    // Chọn lấy công thức theo user_id
    public Cursor getRecipeByUserId(int userId) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(
                TABLE_RECIPE,
                null,
                COT_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null, null, null
        );
    }

    public String getUsername(int userId) {
        SQLiteDatabase database = this.getReadableDatabase();
        String username = null;
        Cursor cursor = database.rawQuery("SELECT username FROM users WHERE user_id = ?", new String[]{String.valueOf(userId)});
        if(cursor.moveToFirst()) {
            username = cursor.getString(0); // lấy username ở cột đầu tiên
        }
        cursor.close();
        return username; // trả về null nếu không tìm thấy username
    }
}
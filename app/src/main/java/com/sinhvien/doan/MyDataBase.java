package com.sinhvien.doan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyDataBase {
    private final DatabaseHelper dbHelper;

    public MyDataBase(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Lấy tất cả dữ liệu bằng phương thức layTatCaDuLieu()
    public Cursor layTatCaDuLieu() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] cot = {
                DatabaseHelper.COT_RECIPE_ID,
                DatabaseHelper.COT_TEN_RECIPE,
                DatabaseHelper.COT_INGREDIENTS,
                DatabaseHelper.COT_STEPS,
                DatabaseHelper.COT_USER_ID,
                DatabaseHelper.COT_IMG_URL,
                DatabaseHelper.COT_CATEGORY,
                DatabaseHelper.COT_TIME,
                DatabaseHelper.COT_DOKHO
        };

        return database.query(
                DatabaseHelper.BANG_RECIPES,
                cot,
                null, null, null, null,
                DatabaseHelper.COT_CATEGORY + " DESC"
        );
    }

    public long them(Recipe recipe) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COT_TEN_RECIPE, recipe.getRecipeName());
        values.put(DatabaseHelper.COT_INGREDIENTS, recipe.getIngredients());
        values.put(DatabaseHelper.COT_STEPS, recipe.getSteps());
        values.put(DatabaseHelper.COT_USER_ID, recipe.getUserId());
        values.put(DatabaseHelper.COT_IMG_URL, recipe.getImgSource());
        values.put(DatabaseHelper.COT_CATEGORY, recipe.getCategory_id());
        values.put(DatabaseHelper.COT_TIME, recipe.getTime());
        values.put(DatabaseHelper.COT_DOKHO, recipe.getDifficulty());

        return database.insert(DatabaseHelper.BANG_RECIPES, null, values);
    }

    public long xoa(int recipeId) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        return database.delete(
                DatabaseHelper.BANG_RECIPES,
                DatabaseHelper.COT_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)}
        );
    }

    public long sua(Recipe recipe) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COT_TEN_RECIPE, recipe.getRecipeName());
        values.put(DatabaseHelper.COT_INGREDIENTS, recipe.getIngredients());
        values.put(DatabaseHelper.COT_STEPS, recipe.getSteps());
        values.put(DatabaseHelper.COT_IMG_URL, recipe.getImgSource());
        values.put(DatabaseHelper.COT_TIME, recipe.getTime());
        values.put(DatabaseHelper.COT_DOKHO, recipe.getDifficulty());
        values.put(DatabaseHelper.COT_CATEGORY, recipe.getCategory_id());

        return database.update(
                DatabaseHelper.BANG_RECIPES,
                values,
                DatabaseHelper.COT_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipe.getRecipeId())}
        );
    }

    // Chọn lấy công thức theo recipe_id
    public Recipe getRecipeById(int recipeId) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Recipe recipe = null;

        Cursor cursor = database.query(
                DatabaseHelper.BANG_RECIPES,
                new String[]{
                        DatabaseHelper.COT_RECIPE_ID,
                        DatabaseHelper.COT_TEN_RECIPE,
                        DatabaseHelper.COT_INGREDIENTS,
                        DatabaseHelper.COT_STEPS,
                        DatabaseHelper.COT_USER_ID,
                        DatabaseHelper.COT_IMG_URL,
                        DatabaseHelper.COT_CATEGORY,
                        DatabaseHelper.COT_TIME,
                        DatabaseHelper.COT_DOKHO
                },
                DatabaseHelper.COT_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            recipe = new Recipe(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_RECIPE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_TEN_RECIPE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_INGREDIENTS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_STEPS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_IMG_URL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_CATEGORY)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_TIME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_DOKHO))
            );
            cursor.close();
        }
        return recipe;
    }

    // Chọn lấy công thức theo category
    public Cursor getRecipeByCategory(int categoryId) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        return database.query(
                DatabaseHelper.BANG_RECIPES,
                null,
                DatabaseHelper.COT_CATEGORY + " = ?",
                new String[]{String.valueOf(categoryId)},
                null, null, null
        );
    }

    // Chọn lấy công thức theo user_id
    public Cursor getRecipeByUserId(int userId) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        return database.query(
                DatabaseHelper.BANG_RECIPES,
                null,
                DatabaseHelper.COT_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null, null, null
        );
    }
    public String getUsername(int userId) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String username = null;
        Cursor cursor = database.rawQuery("SELECT username FROM users WHERE user_id = ?", new String[]{String.valueOf(userId)});
        if(cursor.moveToFirst()) {
            username = cursor.getString(0); // lấy username ở cột đầu tiên
        }
        cursor.close();
        return username; // trả về null nếu không tìm thấy username
    }
}
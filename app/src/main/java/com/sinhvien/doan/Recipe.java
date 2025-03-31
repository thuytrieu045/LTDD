package com.sinhvien.doan;

public class Recipe {
    private int recipeId;
    private String recipeName;
    private String ingredients;
    private int userId;
    private String imgSource;
    private int category_id;
    private String steps;
    private int time;
    private String difficulty;

    public Recipe(int recipeId, String recipeName, String ingredients, String steps, int userId, String imgSource, int category_id, int time, String difficulty) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.steps = steps;
        this.userId = userId;
        this.imgSource = imgSource;
        this.category_id = category_id;
        this.time = time;
        this.difficulty = difficulty;
    }

    public Recipe(int recipeId, String name, String ingredients, String imgUrl) {
        this.recipeId = recipeId;
        this.recipeName = name;
        this.ingredients = ingredients;
        this.imgSource = imgUrl;
    }

    public Recipe(String recipeName, String steps, String imgSource, int category_id)
    {
        this.recipeName = recipeName;
        this.steps = steps;
        this.imgSource = imgSource;
        this.category_id = category_id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getImgSource() {
        return imgSource;
    }

    public void setImgSource(String imgSource) {
        this.imgSource = imgSource;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getTime() {return time;}
    public void setTime(int time) {this.time = time;}
    public String getDifficulty() {return difficulty;}
    public void setDifficulty(String difficulty) {this.difficulty = difficulty;}
}
package com.nurul.nutrigo.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "food_entries")
public class FoodEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int recipeId;
    private String title;
    private String imageUrl;
    private double calories;
    private double protein;
    private double carbs;
    private double fat;
    private String mealType;   // Breakfast, Lunch, Dinner, Snack
    private String dateAdded;  // yyyy-MM-dd

    public FoodEntry(int recipeId, String title, String imageUrl,
                     double calories, double protein, double carbs, double fat,
                     String mealType, String dateAdded) {
        this.recipeId = recipeId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.mealType = mealType;
        this.dateAdded = dateAdded;
    }

    // Getters
    public int getId() { return id; }
    public int getRecipeId() { return recipeId; }
    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
    public double getCalories() { return calories; }
    public double getProtein() { return protein; }
    public double getCarbs() { return carbs; }
    public double getFat() { return fat; }
    public String getMealType() { return mealType; }
    public String getDateAdded() { return dateAdded; }

    // Setter for Room's autoGenerate
    public void setId(int id) { this.id = id; }
}

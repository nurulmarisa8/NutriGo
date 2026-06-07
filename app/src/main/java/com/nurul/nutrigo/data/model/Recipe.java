package com.nurul.nutrigo.data.model;

import com.google.gson.annotations.SerializedName;

public class Recipe {
    @SerializedName("name")
    private String title;

    @SerializedName("calories")
    private String caloriesRaw;

    @SerializedName("serving_size_g")
    private double servingSizeG;

    @SerializedName("fat_total_g")
    private double fat;

    @SerializedName("fat_saturated_g")
    private double fatSaturated;

    @SerializedName("protein_g")
    private String proteinRaw;

    @SerializedName("sodium_mg")
    private double sodium;

    @SerializedName("potassium_mg")
    private double potassium;

    @SerializedName("cholesterol_mg")
    private double cholesterol;

    @SerializedName("carbohydrates_total_g")
    private double carbs;

    @SerializedName("fiber_g")
    private double fiber;

    @SerializedName("sugar_g")
    private double sugar;

    private String image;

    // ---- Getters ----
    public int getId() { return (title != null) ? title.hashCode() : 0; }
    public String getTitle() { return title; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getIngredients() { return null; }
    public String getInstructions() { return null; }
    
    public double getCalories() { 
        try {
            return caloriesRaw != null ? Double.parseDouble(caloriesRaw) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public double getProtein() { 
        try {
            return proteinRaw != null ? Double.parseDouble(proteinRaw) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public double getCarbs() { return carbs; }
    public double getFat() { return fat; }
    public double getSodium() { return sodium; }
    public double getPotassium() { return potassium; }
    public double getVitaminAPercent() { return 0; }
    public double getIronPercent() { return 0; }
    public int getServings() { return 1; }
    public int getReadyInMinutes() { return 30; }

    public void setTitle(String title) { this.title = title; }
    public void setCaloriesRaw(String caloriesRaw) { this.caloriesRaw = caloriesRaw; }
    public void setFat(double fat) { this.fat = fat; }
    public void setProteinRaw(String proteinRaw) { this.proteinRaw = proteinRaw; }
    public void setCarbs(double carbs) { this.carbs = carbs; }
    public void setSodium(double sodium) { this.sodium = sodium; }
    public void setPotassium(double potassium) { this.potassium = potassium; }
}

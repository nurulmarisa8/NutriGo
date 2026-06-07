package com.nurul.nutrigo.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class EdamamResponse {

    @SerializedName("hints")
    private List<Hint> hints;

    public List<Hint> getHints() {
        return hints;
    }

    public static class Hint {
        @SerializedName("food")
        private Food food;

        public Food getFood() {
            return food;
        }
    }

    public static class Food {
        @SerializedName("label")
        private String label;

        @SerializedName("image")
        private String image;

        @SerializedName("category")
        private String category;

        @SerializedName("nutrients")
        private Nutrients nutrients;

        public String getLabel() { return label; }
        public String getImage() { return image; }
        public String getCategory() { return category; }
        public Nutrients getNutrients() { return nutrients; }
    }

    public static class Nutrients {
        @SerializedName("ENERC_KCAL")
        private double energyKcal;

        @SerializedName("PROCNT")
        private double protein;

        @SerializedName("FAT")
        private double fat;

        @SerializedName("CHOCDF")
        private double carbs;

        @SerializedName("FIBTG")
        private double fiber;

        public double getEnergyKcal() { return energyKcal; }
        public double getProtein() { return protein; }
        public double getFat() { return fat; }
        public double getCarbs() { return carbs; }
        public double getFiber() { return fiber; }
    }
}

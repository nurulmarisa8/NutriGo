package com.nurul.nutrigo.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NutritionInfo {
    @SerializedName("nutrients")
    private List<Nutrient> nutrients;

    public List<Nutrient> getNutrients() { return nutrients; }
}

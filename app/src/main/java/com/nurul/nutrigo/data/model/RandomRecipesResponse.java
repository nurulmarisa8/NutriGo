package com.nurul.nutrigo.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RandomRecipesResponse {
    @SerializedName("recipes")
    private List<Recipe> recipes;

    public List<Recipe> getRecipes() { return recipes; }
}

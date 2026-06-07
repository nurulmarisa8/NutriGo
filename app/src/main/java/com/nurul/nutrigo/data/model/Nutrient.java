package com.nurul.nutrigo.data.model;

import com.google.gson.annotations.SerializedName;

public class Nutrient {
    @SerializedName("name")
    private String name;

    @SerializedName("amount")
    private double amount;

    @SerializedName("unit")
    private String unit;

    @SerializedName("percentOfDailyNeeds")
    private double percentOfDailyNeeds;

    public String getName() { return name; }
    public double getAmount() { return amount; }
    public String getUnit() { return unit; }
    public double getPercentOfDailyNeeds() { return percentOfDailyNeeds; }
}

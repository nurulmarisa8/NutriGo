package com.nurul.nutrigo.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.nurul.nutrigo.data.model.FoodEntry;

import java.util.List;

@Dao
public interface FoodEntryDao {

    @Insert
    void insertFood(FoodEntry foodEntry);

    @Query("SELECT * FROM food_entries WHERE dateAdded = :date ORDER BY id DESC")
    List<FoodEntry> getFoodsByDate(String date);

    @Query("SELECT COALESCE(SUM(calories), 0) FROM food_entries WHERE dateAdded = :date")
    double getTotalCaloriesByDate(String date);

    @Query("SELECT COALESCE(SUM(protein), 0) FROM food_entries WHERE dateAdded = :date")
    double getTotalProteinByDate(String date);

    @Query("SELECT COALESCE(SUM(carbs), 0) FROM food_entries WHERE dateAdded = :date")
    double getTotalCarbsByDate(String date);

    @Query("SELECT COALESCE(SUM(fat), 0) FROM food_entries WHERE dateAdded = :date")
    double getTotalFatByDate(String date);

    @Delete
    void deleteFood(FoodEntry foodEntry);

    @Query("DELETE FROM food_entries WHERE id = :id")
    void deleteFoodById(int id);
}

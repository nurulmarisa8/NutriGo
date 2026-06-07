package com.nurul.nutrigo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nurul.nutrigo.R;
import com.nurul.nutrigo.data.model.Recipe;
import com.nurul.nutrigo.databinding.ItemRecipeBinding;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    private List<Recipe> recipes;
    private final OnRecipeClickListener listener;

    public RecipeAdapter(List<Recipe> recipes, OnRecipeClickListener listener) {
        this.recipes  = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecipeBinding b = ItemRecipeBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(recipes.get(position));
    }

    @Override
    public int getItemCount() { return recipes.size(); }

    public void updateData(List<Recipe> newData) {
        this.recipes = newData;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRecipeBinding b;

        ViewHolder(ItemRecipeBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        void bind(Recipe recipe) {
            b.tvRecipeName.setText(recipe.getTitle());
            // Show just the number; unit label is in a separate TextView in the layout
            b.tvCalories.setText(String.valueOf((int) recipe.getCalories()));
            b.tvCategory.setText(buildCategory(recipe));
            b.tvReadyTime.setText("per 100g");

            // Tag chip visibility
            double protein = recipe.getProtein();
            double carbs   = recipe.getCarbs();
            double cal     = recipe.getCalories();

            b.tvTag.setVisibility(View.GONE);
            if (protein > 20) {
                b.tvTag.setVisibility(View.VISIBLE);
                b.tvTag.setText("Protein Tinggi");
                b.tvTag.setBackgroundResource(R.drawable.bg_tag_protein);
            } else if (carbs > 50) {
                b.tvTag.setVisibility(View.VISIBLE);
                b.tvTag.setText("Karbo Tinggi");
                b.tvTag.setBackgroundResource(R.drawable.bg_tag_carb);
            } else if (cal > 0 && cal < 100) {
                b.tvTag.setVisibility(View.VISIBLE);
                b.tvTag.setText("Rendah Kalori");
                b.tvTag.setBackgroundResource(R.drawable.bg_tag_default);
            }

            Glide.with(b.getRoot().getContext())
                    .load(recipe.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.ic_food_placeholder)
                    .error(R.drawable.ic_food_placeholder)
                    .into(b.ivRecipe);

            b.getRoot().setOnClickListener(v -> listener.onRecipeClick(recipe));
        }

        private String buildCategory(Recipe recipe) {
            String mealTime;
            double cal = recipe.getCalories();
            if (cal < 250)       mealTime = "Snack";
            else if (cal < 450)  mealTime = "Breakfast";
            else if (cal < 700)  mealTime = "Lunch";
            else                 mealTime = "Dinner";

            double protein = recipe.getProtein();
            String type;
            if (protein > 30) type = "High Protein";
            else if (recipe.getCarbs() > 60) type = "High Carb";
            else type = "Healthy";

            return mealTime + " • " + type;
        }
    }
}

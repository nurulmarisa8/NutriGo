package com.nurul.nutrigo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nurul.nutrigo.R;
import com.nurul.nutrigo.data.model.FoodEntry;
import com.nurul.nutrigo.databinding.ItemFoodDiaryBinding;

import java.util.List;

public class FoodDiaryAdapter extends RecyclerView.Adapter<FoodDiaryAdapter.ViewHolder> {

    public interface OnDeleteClickListener {
        void onDeleteClick(FoodEntry entry);
    }

    private List<FoodEntry> entries;
    private final OnDeleteClickListener listener;

    public FoodDiaryAdapter(List<FoodEntry> entries, OnDeleteClickListener listener) {
        this.entries  = entries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodDiaryBinding b = ItemFoodDiaryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(entries.get(position));
    }

    @Override
    public int getItemCount() { return entries.size(); }

    public void updateData(List<FoodEntry> newData) {
        this.entries = newData;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemFoodDiaryBinding b;

        ViewHolder(ItemFoodDiaryBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        void bind(FoodEntry entry) {
            b.tvFoodName.setText(entry.getTitle());
            b.tvMealType.setText(entry.getMealType());
            b.tvCalories.setText((int) entry.getCalories() + " kcal");

            // Tag
            String tag = getTag(entry);
            b.tvTag.setText(tag);
            if ("High Protein".equals(tag)) {
                b.tvTag.setBackgroundResource(R.drawable.bg_tag_protein);
            } else if ("High Carb".equals(tag)) {
                b.tvTag.setBackgroundResource(R.drawable.bg_tag_carb);
            } else {
                b.tvTag.setBackgroundResource(R.drawable.bg_tag_default);
            }

            b.btnDelete.setOnClickListener(v -> listener.onDeleteClick(entry));
        }

        private String getTag(FoodEntry e) {
            if (e.getProtein() > 25)  return "High Protein";
            if (e.getCarbs()   > 50)  return "High Carb";
            if (e.getCalories() < 200) return "Low Cal";
            return "Balanced";
        }
    }
}

package com.nurul.nutrigo.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nurul.nutrigo.R;
import com.nurul.nutrigo.adapter.FoodDiaryAdapter;
import com.nurul.nutrigo.data.local.AppDatabase;
import com.nurul.nutrigo.data.model.FoodEntry;
import com.nurul.nutrigo.databinding.FragmentDiaryBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.Context;
import android.content.SharedPreferences;

public class DiaryFragment extends Fragment {

    private FragmentDiaryBinding binding;
    private FoodDiaryAdapter adapter;
    private ExecutorService executor;
    private Handler mainHandler;
    private Calendar currentDate;
    private double calorieGoal = 2000.0;

    private static final double PROTEIN_GOAL = 120.0;
    private static final double CARBS_GOAL   = 220.0;
    private static final double FAT_GOAL     = 65.0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDiaryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        executor    = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        currentDate = Calendar.getInstance();

        SharedPreferences prefs = requireActivity().getSharedPreferences("nutrigo_prefs", Context.MODE_PRIVATE);
        String savedDate = prefs.getString("selected_diary_date", null);
        if (savedDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                currentDate.setTime(sdf.parse(savedDate));
            } catch (Exception e) {}
        }

        setupDate();
        setupRecyclerView();
        loadDiaryData();

        binding.btnPrevDate.setOnClickListener(v -> {
            currentDate.add(Calendar.DAY_OF_YEAR, -1);
            setupDate();
            loadDiaryData();
        });

        binding.btnNextDate.setOnClickListener(v -> {
            currentDate.add(Calendar.DAY_OF_YEAR, 1);
            setupDate();
            loadDiaryData();
        });

        binding.tvDate.setOnClickListener(v -> {
            new android.app.DatePickerDialog(requireContext(), (view1, year, month, dayOfMonth) -> {
                currentDate.set(Calendar.YEAR, year);
                currentDate.set(Calendar.MONTH, month);
                currentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setupDate();
                loadDiaryData();
            }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH)).show();
        });

        binding.btnSaveDailyGoal.setOnClickListener(v -> {
            String val = binding.etDailyCalorieGoal.getText().toString().trim();
            if (!val.isEmpty()) {
                try {
                    int newGoal = Integer.parseInt(val);
                    if (newGoal > 0) {
                        String tDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate.getTime());
                        SharedPreferences p = requireActivity().getSharedPreferences("nutrigo_prefs", Context.MODE_PRIVATE);
                        p.edit().putInt("calorie_goal_" + tDate, newGoal).apply();
                        calorieGoal = newGoal;
                        
                        android.widget.Toast.makeText(getContext(), "Target kalori disimpan", android.widget.Toast.LENGTH_SHORT).show();
                        binding.etDailyCalorieGoal.clearFocus();
                        android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) imm.hideSoftInputFromWindow(binding.etDailyCalorieGoal.getWindowToken(), 0);
                        
                        loadDiaryData();
                    }
                } catch (NumberFormatException e) { }
            }
        });
    }

    private void setupDate() {
        SimpleDateFormat fmt = new SimpleDateFormat("EEEE, MMM dd", Locale.getDefault());
        Calendar today = Calendar.getInstance();
        if (currentDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            currentDate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            binding.tvDate.setText("Today");
        } else {
            binding.tvDate.setText(fmt.format(currentDate.getTime()));
        }
    }

    private void setupRecyclerView() {
        adapter = new FoodDiaryAdapter(new ArrayList<>(), this::deleteEntry);
        binding.recyclerViewMeals.setAdapter(adapter);
    }

    /** Load today's diary from Room on a background thread, then update UI on main thread */
    private void loadDiaryData() {
        String targetDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate.getTime());
        
        // Save targetDate to SharedPreferences so DetailActivity can use it when saving new food
        SharedPreferences prefs = requireActivity().getSharedPreferences("nutrigo_prefs", Context.MODE_PRIVATE);
        prefs.edit().putString("selected_diary_date", targetDate).apply();

        calorieGoal = prefs.getInt("calorie_goal_" + targetDate, 2000);
        binding.etDailyCalorieGoal.setText(String.valueOf((int) calorieGoal));

        executor.execute(() -> {
            List<FoodEntry> entries = AppDatabase.getInstance(requireContext())
                    .foodEntryDao().getFoodsByDate(targetDate);

            double totalCal  = 0, totalProt = 0, totalCarbs = 0, totalFat = 0;
            for (FoodEntry e : entries) {
                totalCal   += e.getCalories();
                totalProt  += e.getProtein();
                totalCarbs += e.getCarbs();
                totalFat   += e.getFat();
            }

            final double fCal = totalCal, fProt = totalProt,
                    fCarbs = totalCarbs, fFat = totalFat;
            final List<FoodEntry> fEntries = entries;

            mainHandler.post(() -> {
                if (binding == null) return;
                updateUI(fCal, fProt, fCarbs, fFat, fEntries);
            });
        });
    }

    private void updateUI(double cal, double prot, double carbs, double fat,
                          List<FoodEntry> entries) {
        // Calorie ring
        binding.tvCaloriesEaten.setText(String.valueOf((int) cal));
        binding.tvCaloriesGoal.setText("/ " + (int) calorieGoal);
        binding.progressCalories.setProgress((int) Math.min((cal / calorieGoal) * 100, 100));
        
        double remaining = calorieGoal - cal;
        if (remaining < 0) {
            binding.tvRemainingBudget.setText((int) Math.abs(remaining) + " kcal over");
            binding.tvRemainingBudget.setTextColor(getResources().getColor(R.color.error_red, null));
            binding.progressCalories.setIndicatorColor(getResources().getColor(R.color.error_red, null));
        } else {
            binding.tvRemainingBudget.setText((int) remaining + " kcal");
            binding.tvRemainingBudget.setTextColor(getResources().getColor(R.color.green_primary, null));
            binding.progressCalories.setIndicatorColor(getResources().getColor(R.color.green_primary, null));
        }

        // Macro bars
        binding.tvProteinValue.setText((int) prot + "g / " + (int) PROTEIN_GOAL + "g");
        binding.progressProtein.setProgress((int) Math.min((prot / PROTEIN_GOAL) * 100, 100));

        binding.tvCarbsValue.setText((int) carbs + "g / " + (int) CARBS_GOAL + "g");
        binding.progressCarbs.setProgress((int) Math.min((carbs / CARBS_GOAL) * 100, 100));

        binding.tvFatValue.setText((int) fat + "g / " + (int) FAT_GOAL + "g");
        binding.progressFat.setProgress((int) Math.min((fat / FAT_GOAL) * 100, 100));

        // Meal list
        adapter.updateData(entries);
        binding.tvEmptyDiary.setVisibility(entries.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void deleteEntry(FoodEntry entry) {
        executor.execute(() -> {
            AppDatabase.getInstance(requireContext()).foodEntryDao().deleteFoodById(entry.getId());
            mainHandler.post(this::loadDiaryData);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload whenever user returns to this fragment
        loadDiaryData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (executor != null) executor.shutdown();
    }
}

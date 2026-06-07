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

import com.nurul.nutrigo.adapter.FoodDiaryAdapter;
import com.nurul.nutrigo.data.local.AppDatabase;
import com.nurul.nutrigo.data.model.FoodEntry;
import com.nurul.nutrigo.databinding.FragmentDiaryBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiaryFragment extends Fragment {

    private FragmentDiaryBinding binding;
    private FoodDiaryAdapter adapter;
    private ExecutorService executor;
    private Handler mainHandler;

    private static final double CALORIE_GOAL = 2000.0;
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

        setupDate();
        setupRecyclerView();
        loadDiaryData();
    }

    private void setupDate() {
        SimpleDateFormat fmt = new SimpleDateFormat("EEEE, MMM dd", Locale.getDefault());
        binding.tvDate.setText(fmt.format(new Date()));
    }

    private void setupRecyclerView() {
        adapter = new FoodDiaryAdapter(new ArrayList<>(), this::deleteEntry);
        binding.recyclerViewMeals.setAdapter(adapter);
    }

    /** Load today's diary from Room on a background thread, then update UI on main thread */
    private void loadDiaryData() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        executor.execute(() -> {
            List<FoodEntry> entries = AppDatabase.getInstance(requireContext())
                    .foodEntryDao().getFoodsByDate(today);

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
        binding.tvCaloriesGoal.setText("/ " + (int) CALORIE_GOAL);
        binding.progressCalories.setProgress((int) Math.min((cal / CALORIE_GOAL) * 100, 100));
        double remaining = Math.max(0, CALORIE_GOAL - cal);
        binding.tvRemainingBudget.setText((int) remaining + " kcal");

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

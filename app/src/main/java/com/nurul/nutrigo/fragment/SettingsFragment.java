package com.nurul.nutrigo.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.nurul.nutrigo.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = requireActivity().getSharedPreferences("nutrigo_prefs", Context.MODE_PRIVATE);

        // --- Dark Mode ---
        boolean isDark = prefs.getBoolean("dark_mode", false);
        binding.switchDarkMode.setChecked(isDark);
        updateLabel(isDark);

        binding.switchDarkMode.setOnCheckedChangeListener((btn, isChecked) -> {
            // Persist to SharedPreferences
            prefs.edit().putBoolean("dark_mode", isChecked).apply();

            // Apply night mode (automatically recreates if necessary)
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
            updateLabel(isChecked);
        });

        // --- Calorie Target ---
        int currentCalorieGoal = prefs.getInt("calorie_goal", 2000);
        binding.etCalorieTarget.setText(String.valueOf(currentCalorieGoal));

        binding.btnSaveCalorie.setOnClickListener(v -> {
            String input = binding.etCalorieTarget.getText().toString().trim();
            if (!input.isEmpty()) {
                try {
                    int newGoal = Integer.parseInt(input);
                    if (newGoal > 0) {
                        prefs.edit().putInt("calorie_goal", newGoal).apply();
                        android.widget.Toast.makeText(getContext(), "Target kalori disimpan: " + newGoal + " kcal", android.widget.Toast.LENGTH_SHORT).show();
                        
                        // Clear focus and hide keyboard
                        binding.etCalorieTarget.clearFocus();
                        android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) imm.hideSoftInputFromWindow(binding.etCalorieTarget.getWindowToken(), 0);
                    } else {
                        binding.etCalorieTarget.setError("Masukkan angka yang valid");
                    }
                } catch (NumberFormatException e) {
                    binding.etCalorieTarget.setError("Masukkan angka yang valid");
                }
            } else {
                binding.etCalorieTarget.setError("Tidak boleh kosong");
            }
        });

        // --- Calorie Default Reset ---
        binding.btnResetDefault.setOnClickListener(v -> {
            prefs.edit().putInt("calorie_goal", 2000).apply();
            binding.etCalorieTarget.setText("2000");
            android.widget.Toast.makeText(getContext(), "Target kalori dikembalikan ke default (2000 kcal)", android.widget.Toast.LENGTH_SHORT).show();
            binding.etCalorieTarget.clearFocus();
            android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(binding.etCalorieTarget.getWindowToken(), 0);
        });

        // --- Back to Home (Toolbar) ---
        binding.btnBack.setOnClickListener(v -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });
    }

    private void updateLabel(boolean isDark) {
        binding.tvThemeStatus.setText(isDark ? "Mode Gelap Aktif" : "Mode Terang Aktif");
        binding.tvThemeDesc.setText(isDark
                ? "Tampilan gelap untuk kenyamanan mata di malam hari"
                : "Tampilan terang untuk penggunaan siang hari");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

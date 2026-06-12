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

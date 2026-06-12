package com.nurul.nutrigo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

// SplashActivity adalah halaman pertama yang muncul (layar pemuatan) saat aplikasi dibuka
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Menerapkan tema yang disimpan di SharedPreferences SEBELUM onCreate agar layar tidak berkedip
        // Mengecek apakah pengguna memilih Mode Gelap (Dark Mode) atau tidak
        SharedPreferences prefs = getSharedPreferences("nutrigo_prefs", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Edge-to-edge status bars and navigation bars in Splash Screen
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // Berpindah ke MainActivity setelah jeda waktu (delay) 2 detik menggunakan Intent
        // Handler dan Looper digunakan untuk menjalankan kode di Main Thread setelah delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Menutup SplashActivity agar pengguna tidak bisa kembali menggunakan tombol 'Back'
            // Transisi efek memudar
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, SPLASH_DELAY);
    }
}

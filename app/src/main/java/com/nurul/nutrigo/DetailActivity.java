package com.nurul.nutrigo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.nurul.nutrigo.data.local.AppDatabase;
import com.nurul.nutrigo.data.model.FoodEntry;
import com.nurul.nutrigo.databinding.ActivityDetailBinding;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID   = "recipe_id";
    public static final String EXTRA_TITLE        = "title";
    public static final String EXTRA_IMAGE_URL    = "image_url";
    public static final String EXTRA_CALORIES     = "calories";
    public static final String EXTRA_PROTEIN      = "protein";
    public static final String EXTRA_CARBS        = "carbs";
    public static final String EXTRA_FAT          = "fat";
    public static final String EXTRA_SODIUM       = "sodium";
    public static final String EXTRA_POTASSIUM    = "potassium";
    public static final String EXTRA_VITAMIN_A    = "vitamin_a";
    public static final String EXTRA_IRON         = "iron";
    public static final String EXTRA_SERVINGS     = "servings";

    private ActivityDetailBinding binding;
    private ExecutorService executor;
    private Handler mainHandler;

    private int    recipeId;
    private String title;
    private String imageUrl;
    private double calories, protein, carbs, fat;
    private double sodium, potassium, vitaminA, iron;
    private int    servings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply theme
        SharedPreferences prefs = getSharedPreferences("nutrigo_prefs", MODE_PRIVATE);
        AppCompatDelegate.setDefaultNightMode(
                prefs.getBoolean("dark_mode", false)
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Edge-to-edge: toolbar absorbs status bar inset
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar, (v, insets) -> {
            int statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
            v.setPadding(v.getPaddingLeft(), statusBarHeight, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        executor    = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        readIntent();
        setupUI();
        setupSpinner();
        setupListeners();
    }

    // Menerima data (Intent) yang dikirimkan dari halaman pencarian (SearchFragment)
    private void readIntent() {
        recipeId   = getIntent().getIntExtra(EXTRA_RECIPE_ID, 0);
        title      = getIntent().getStringExtra(EXTRA_TITLE);
        imageUrl   = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        calories   = getIntent().getDoubleExtra(EXTRA_CALORIES, 0);
        protein    = getIntent().getDoubleExtra(EXTRA_PROTEIN, 0);
        carbs      = getIntent().getDoubleExtra(EXTRA_CARBS, 0);
        fat        = getIntent().getDoubleExtra(EXTRA_FAT, 0);
        sodium     = getIntent().getDoubleExtra(EXTRA_SODIUM, 0);
        potassium  = getIntent().getDoubleExtra(EXTRA_POTASSIUM, 0);
        vitaminA   = getIntent().getDoubleExtra(EXTRA_VITAMIN_A, 0);
        iron       = getIntent().getDoubleExtra(EXTRA_IRON, 0);
        servings   = getIntent().getIntExtra(EXTRA_SERVINGS, 1);
    }

    // Mengatur antarmuka pengguna (tampilan, gambar, dan teks kalori)
    private void setupUI() {
        // Mengatur tombol back (kembali) untuk menutup halaman detail ini
        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnBack.setOnLongClickListener(v -> { finish(); return true; });

        // Food image
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_food_placeholder)
                    .into(binding.ivRecipeImage);
        } else {
            binding.ivRecipeImage.setImageResource(R.drawable.ic_food_placeholder);
        }

        // Title & serving
        binding.tvRecipeTitle.setText(title != null ? title : "");
        binding.tvServingSize.setText(servings + " Serving (" + (servings * 150) + "g)");

        // Calorie ring
        binding.tvCaloriesValue.setText(String.valueOf((int) calories));
        int calPct = (int) Math.min((calories / 2000.0) * 100, 100);
        binding.progressCalories.setProgress(calPct);

        // Macros
        binding.tvCarbsValue.setText((int) carbs + "g");
        binding.tvProteinValue.setText((int) protein + "g");
        binding.tvFatValue.setText((int) fat + "g");

        double totalMacros = carbs + protein + fat;
        if (totalMacros > 0) {
            binding.tvCarbsPercent.setText((int) ((carbs / totalMacros) * 100) + "% of total");
            binding.tvProteinPercent.setText((int) ((protein / totalMacros) * 100) + "% of total");
            binding.tvFatPercent.setText((int) ((fat / totalMacros) * 100) + "% of total");
        }

        // Micronutrients
        binding.tvSodiumValue.setText(Math.round(sodium) + " mg");
        binding.tvPotassiumValue.setText(Math.round(potassium) + " mg");
        binding.tvVitaminAValue.setText((int) vitaminA + "% DV");
        binding.tvIronValue.setText((int) iron + "% DV");
    }

    // Mengatur dropdown (Spinner) untuk memilih jenis makan (Breakfast, Lunch, Dinner, Snack)
    private void setupSpinner() {
        List<String> types = Arrays.asList("Breakfast", "Lunch", "Dinner", "Snack");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerMealType.setAdapter(adapter);
    }

    private void setupListeners() {
        binding.btnSaveToDiary.setOnClickListener(v -> {
            String mealType = (String) binding.spinnerMealType.getSelectedItem();
            saveToDiary(mealType);
        });
    }

    // Menyimpan resep ke dalam Jurnal Makanan (Database Lokal Room)
    private void saveToDiary(String mealType) {
        binding.btnSaveToDiary.setEnabled(false);
        binding.btnSaveToDiary.setText(R.string.saving);

        // Mengambil tanggal yang sedang aktif dipilih di halaman Diary
        SharedPreferences prefs = getSharedPreferences("nutrigo_prefs", MODE_PRIVATE);
        String defaultToday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String targetDate = prefs.getString("selected_diary_date", defaultToday);

        FoodEntry entry = new FoodEntry(
                recipeId, title, imageUrl,
                calories, protein, carbs, fat,
                mealType, targetDate
        );

        // Menggunakan ExecutorService untuk menjalankan operasi Database di Background Thread
        // (Wajib dilakukan agar UI / tampilan tidak macet/freeze)
        executor.execute(() -> {
            AppDatabase.getInstance(DetailActivity.this).foodEntryDao().insertFood(entry);
            
            // Kembali ke Main Thread untuk mengupdate tampilan tombol dan memunculkan notifikasi (Toast)
            mainHandler.post(() -> {
                binding.btnSaveToDiary.setText(R.string.saved_check);
                Toast.makeText(DetailActivity.this,
                        title + " ditambahkan ke jurnal!", Toast.LENGTH_SHORT).show();
                mainHandler.postDelayed(() -> {
                    if (!isFinishing()) {
                        binding.btnSaveToDiary.setEnabled(true);
                        binding.btnSaveToDiary.setText("+ Simpan ke Jurnal");
                    }
                }, 2000);
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}

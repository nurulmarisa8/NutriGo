package com.nurul.nutrigo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nurul.nutrigo.DetailActivity;
import com.nurul.nutrigo.adapter.RecipeAdapter;
import com.nurul.nutrigo.data.model.Recipe;
import com.nurul.nutrigo.data.remote.RetrofitClient;
import com.nurul.nutrigo.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// SearchFragment adalah halaman untuk mencari data makanan dari internet menggunakan API
public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private RecipeAdapter adapter;
    private String currentTag = "chicken and rice";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        setupChips();
        setupSearch();
        setupSwipeRefresh();
        binding.btnRefresh.setOnClickListener(v -> loadRecipes(currentTag));
        loadRecipes(currentTag);
    }

    // Konfigurasi RecyclerView (Daftar list makanan)
    private void setupRecyclerView() {
        // Inisialisasi adapter kosong terlebih dahulu
        adapter = new RecipeAdapter(new ArrayList<>(), recipe -> openDetail(recipe));
        binding.recyclerViewRecipes.setAdapter(adapter);
    }

    private void setupChips() {
        binding.chipAll.setOnClickListener(v -> { currentTag = "chicken and rice"; updateChipSelection(binding.chipAll); loadRecipes(currentTag); });
        binding.chipHighProtein.setOnClickListener(v -> { currentTag = "breakfast"; updateChipSelection(binding.chipHighProtein); loadRecipes(currentTag); });
        binding.chipLowCarb.setOnClickListener(v -> { currentTag = "lunch"; updateChipSelection(binding.chipLowCarb); loadRecipes(currentTag); });
        binding.chipVegan.setOnClickListener(v -> { currentTag = "snack"; updateChipSelection(binding.chipVegan); loadRecipes(currentTag); });
    }

    private void updateChipSelection(android.widget.TextView selectedChip) {
        android.widget.TextView[] chips = {binding.chipAll, binding.chipHighProtein, binding.chipLowCarb, binding.chipVegan};
        for (android.widget.TextView chip : chips) {
            if (chip == selectedChip) {
                chip.setBackgroundResource(com.nurul.nutrigo.R.drawable.bg_chip_selected);
                chip.setTextColor(getResources().getColor(com.nurul.nutrigo.R.color.white, null));
            } else {
                chip.setBackgroundResource(com.nurul.nutrigo.R.drawable.bg_chip_unselected);
                chip.setTextColor(getResources().getColor(com.nurul.nutrigo.R.color.green_primary, null));
            }
        }
    }

    private void setupSearch() {
        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String query = binding.etSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    currentTag = query;
                    loadRecipes(currentTag);
                    // hide keyboard
                    InputMethodManager imm = (InputMethodManager) requireContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) imm.hideSoftInputFromWindow(binding.etSearch.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });

        // Menambahkan pendeteksi perubahan teks
        // Jika pencarian dikosongkan (dihapus), maka otomatis kembali ke beranda ("Semua")
        binding.etSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(android.text.Editable s) {
                if (s.toString().trim().isEmpty()) {
                    currentTag = "chicken and rice"; // Default beranda
                    updateChipSelection(binding.chipAll); // Aktifkan chip "Semua"
                    loadRecipes(currentTag);
                }
            }
        });
    }

    private void setupSwipeRefresh() {
        binding.swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_green_dark, android.R.color.holo_green_light);
        binding.swipeRefreshLayout.setOnRefreshListener(() -> loadRecipes(currentTag));
    }

    // Mengambil data dari Internet (Edamam API) menggunakan library Retrofit
    private void loadRecipes(String query) {
        showOffline(false, null, null);
        if (!binding.swipeRefreshLayout.isRefreshing()) {
            binding.progressBar.setVisibility(View.VISIBLE);
        }

        // Panggilan asynchronous ke API (berjalan di background thread secara otomatis oleh Retrofit)
        RetrofitClient.getInstance().getService()
                .getNutrition("312d7695", "d6ede3c1f46f0797ff779ccf038710f5", query)
                .enqueue(new Callback<com.nurul.nutrigo.data.model.EdamamResponse>() {
            @Override
            public void onResponse(@NonNull Call<com.nurul.nutrigo.data.model.EdamamResponse> call,
                                   @NonNull Response<com.nurul.nutrigo.data.model.EdamamResponse> response) {
                if (binding == null) return;
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    com.nurul.nutrigo.data.model.EdamamResponse edamam = response.body();
                    List<Recipe> recipes = new ArrayList<>();
                    
                    if (edamam.getHints() != null) {
                        for (com.nurul.nutrigo.data.model.EdamamResponse.Hint hint : edamam.getHints()) {
                            if (hint.getFood() != null) {
                                com.nurul.nutrigo.data.model.EdamamResponse.Food f = hint.getFood();
                                Recipe recipe = new Recipe();
                                recipe.setTitle(f.getLabel());
                                recipe.setImage(f.getImage());
                                
                                if (f.getNutrients() != null) {
                                    recipe.setCaloriesRaw(String.valueOf(f.getNutrients().getEnergyKcal()));
                                    recipe.setProteinRaw(String.valueOf(f.getNutrients().getProtein()));
                                    recipe.setFat(f.getNutrients().getFat());
                                    recipe.setCarbs(f.getNutrients().getCarbs());
                                }
                                recipes.add(recipe);
                            }
                        }
                    }

                    if (recipes.isEmpty()) {
                        showOffline(true, "Data Not Found", "Tidak ada data makanan untuk pencarian ini.");
                    } else {
                        adapter.updateData(recipes);
                        showOffline(false, null, null);
                    }
                } else {
                    showOffline(true, "Error " + response.code(), "Gagal mengambil data dari Edamam API.");
                }
            }

            // Callback saat koneksi internet gagal atau server error
            @Override
            public void onFailure(@NonNull Call<com.nurul.nutrigo.data.model.EdamamResponse> call,
                                  @NonNull Throwable t) {
                if (binding == null) return;
                hideLoading();
                showOffline(true, "Connection Failed", t.getMessage());
            }
        });
    }

    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    private void showOffline(boolean show, String title, String subtitle) {
        binding.layoutOffline.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.swipeRefreshLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        
        if (show) {
            if (title != null) binding.tvErrorTitle.setText(title);
            if (subtitle != null) binding.tvErrorSubtitle.setText(subtitle);
        }
    }

    private void openDetail(Recipe recipe) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_RECIPE_ID, recipe.getId());
        intent.putExtra(DetailActivity.EXTRA_TITLE,     recipe.getTitle());
        intent.putExtra(DetailActivity.EXTRA_IMAGE_URL, recipe.getImage());
        intent.putExtra(DetailActivity.EXTRA_CALORIES,  recipe.getCalories());
        intent.putExtra(DetailActivity.EXTRA_PROTEIN,   recipe.getProtein());
        intent.putExtra(DetailActivity.EXTRA_CARBS,     recipe.getCarbs());
        intent.putExtra(DetailActivity.EXTRA_FAT,       recipe.getFat());
        intent.putExtra(DetailActivity.EXTRA_SODIUM,    recipe.getSodium());
        intent.putExtra(DetailActivity.EXTRA_POTASSIUM, recipe.getPotassium());
        intent.putExtra(DetailActivity.EXTRA_VITAMIN_A, recipe.getVitaminAPercent());
        intent.putExtra(DetailActivity.EXTRA_IRON,      recipe.getIronPercent());
        intent.putExtra(DetailActivity.EXTRA_SERVINGS,  recipe.getServings());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

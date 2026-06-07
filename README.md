<div align="center">

# 🍏 NutriGo

### Aplikasi Android Pemantau Nutrisi & Jurnal Makanan Harian

[![Android](https://img.shields.io/badge/Platform-Android-green?style=flat-square&logo=android)](https://www.android.com/)
[![Java](https://img.shields.io/badge/Language-Java-orange?style=flat-square&logo=openjdk)](https://www.java.com/)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-26%20(Android%208.0)-blue?style=flat-square)](https://developer.android.com/studio/releases/platforms)
[![Material 3](https://img.shields.io/badge/Design-Material%203-purple?style=flat-square)](https://m3.material.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)](LICENSE)

**NutriGo** adalah aplikasi Android yang membantu pengguna memantau asupan nutrisi harian, mencari informasi gizi makanan secara *real-time*, serta mencatat jurnal makanan dengan mudah dan intuitif.

</div>

---

## 📋 Daftar Isi

- [Tentang Aplikasi](#-tentang-aplikasi)
- [Fitur Utama](#-fitur-utama)
- [Screenshot](#-screenshot)
- [Arsitektur & Struktur Proyek](#-arsitektur--struktur-proyek)
- [Teknologi yang Digunakan](#-teknologi-yang-digunakan)
- [Implementasi Teknis](#-implementasi-teknis)
- [Cara Menjalankan Aplikasi](#-cara-menjalankan-aplikasi)
- [API Reference](#-api-reference)
- [Konvensi Commit](#-konvensi-commit)
- [Informasi Pengembang](#-informasi-pengembang)

---

## 🌟 Tentang Aplikasi

NutriGo hadir sebagai solusi bagi siapa saja yang ingin hidup lebih sehat dengan memonitor asupan kalori dan nutrisi setiap hari. Dengan tampilan yang bersih dan modern berbasis **Material Design 3**, pengguna dapat:

- Mencari database ribuan makanan dari seluruh dunia
- Melihat detail kandungan gizi (kalori, protein, karbo, lemak, dan mikronutrisi)
- Menyimpan makanan ke jurnal harian dan memantau progres kalori
- Menggunakan aplikasi bahkan tanpa koneksi internet (mode offline)

> Aplikasi ini dibuat sebagai **Tugas Final Praktikum Pemrograman Mobile** Semester 4 — 2026.

---

## 🚀 Fitur Utama

| Fitur | Deskripsi |
|-------|-----------|
| 🔍 **Pencarian Makanan** | Cari ribuan makanan dari Edamam Food Database API secara *real-time* |
| 🏷️ **Filter Kategori** | Filter cepat: Protein Tinggi, Karbo Rendah, dan Vegan |
| 📊 **Detail Nutrisi Lengkap** | Kalori, protein, karbohidrat, lemak, sodium, potassium, vitamin & mineral |
| 📔 **Jurnal Makanan Harian** | Catat konsumsi per waktu makan (Breakfast / Lunch / Dinner / Snack) |
| 🎯 **Progress Kalori** | Lingkaran progres kalori dan progress bar makronutrisi harian |
| 📴 **Mode Offline** | Jurnal harian tetap dapat diakses tanpa koneksi internet |
| 🔄 **Tombol Refresh** | Muat ulang data dari API saat koneksi bermasalah |
| 🌙 **Dark / Light Mode** | Pilih tampilan terang atau gelap sesuai kenyamanan |
| 🖼️ **Splash Screen** | Layar pembuka elegan dengan logo aplikasi |

---

## 📸 Screenshot

> *(Tambahkan screenshot aplikasi di sini setelah mengambil dari perangkat)*

| Splash Screen | Pencarian | Detail Makanan |
|:---:|:---:|:---:|
| *Coming Soon* | *Coming Soon* | *Coming Soon* |

| Jurnal Harian | Dark Mode | Pengaturan |
|:---:|:---:|:---:|
| *Coming Soon* | *Coming Soon* | *Coming Soon* |

---

## 🏗️ Arsitektur & Struktur Proyek

```
app/src/main/
├── java/com/nurul/nutrigo/
│   ├── SplashActivity.java          # Splash screen (Launcher Activity)
│   ├── MainActivity.java            # Main activity + Navigation Host
│   ├── DetailActivity.java          # Detail nutrisi makanan
│   │
│   ├── fragment/
│   │   ├── SearchFragment.java      # Fragment pencarian makanan (API)
│   │   ├── DiaryFragment.java       # Fragment jurnal makanan harian
│   │   └── SettingsFragment.java    # Fragment pengaturan tema
│   │
│   ├── adapter/
│   │   ├── RecipeAdapter.java       # Adapter RecyclerView hasil pencarian
│   │   └── FoodDiaryAdapter.java    # Adapter RecyclerView jurnal harian
│   │
│   └── data/
│       ├── model/
│       │   ├── Recipe.java          # Model data makanan
│       │   ├── EdamamResponse.java  # Model response dari Edamam API
│       │   └── FoodEntry.java       # Entity Room Database (jurnal)
│       ├── remote/
│       │   ├── SpoonacularService.java  # Interface Retrofit API
│       │   └── RetrofitClient.java      # Singleton Retrofit client
│       └── local/
│           ├── AppDatabase.java     # Room Database instance
│           └── FoodEntryDao.java    # DAO untuk operasi database
│
└── res/
    ├── layout/          # XML layout file
    ├── navigation/      # nav_graph.xml (Navigation Component)
    ├── drawable/        # Ikon & shape drawable (termasuk logo.png)
    ├── menu/            # Bottom navigation menu
    ├── values/          # Warna, string, tema Light
    └── values-night/    # Tema Dark Mode
```

---

## 🛠️ Teknologi yang Digunakan

| Kategori | Library / Framework | Versi |
|----------|---------------------|-------|
| **Bahasa** | Java | JDK 11 |
| **UI** | Material Components 3 | Latest |
| **Networking** | Retrofit2 + OkHttp | Latest |
| **JSON Parser** | Gson Converter | Latest |
| **Image Loading** | Glide | Latest |
| **Database** | Room (SQLite) | Latest |
| **Navigasi** | Jetpack Navigation Component | Latest |
| **Layout** | ConstraintLayout, ViewBinding | Latest |
| **UX** | SwipeRefreshLayout | Latest |
| **Preferensi** | SharedPreferences | Built-in |

---

## 💻 Implementasi Teknis

Semua spesifikasi teknis Tugas Final telah diimplementasikan sebagai berikut:

### 1. ✅ Activity & Intent

Aplikasi memiliki **4 Activity**:

- **`SplashActivity`** — Launcher Activity yang menampilkan splash screen selama 2 detik lalu berpindah ke `MainActivity` menggunakan *Explicit Intent*.
- **`MainActivity`** — Menjadi host navigasi utama (BottomNavigationView + NavHostFragment).
- **`DetailActivity`** — Menampilkan detail nutrisi lengkap makanan.

**Penggunaan Intent:**
```java
// SearchFragment.java → DetailActivity
Intent intent = new Intent(getActivity(), DetailActivity.class);
intent.putExtra(DetailActivity.EXTRA_TITLE, recipe.getTitle());
intent.putExtra(DetailActivity.EXTRA_CALORIES, recipe.getCalories());
// ... (pass semua data nutrisi via Intent Extras)
startActivity(intent);
```

---

### 2. ✅ RecyclerView

Diimplementasikan di **dua lokasi berbeda**:

**a. Hasil Pencarian Makanan** (`SearchFragment`)
```java
// RecipeAdapter dengan ViewHolder pattern
adapter = new RecipeAdapter(new ArrayList<>(), recipe -> openDetail(recipe));
binding.recyclerViewRecipes.setAdapter(adapter);
```

**b. Jurnal Makanan Harian** (`DiaryFragment`)
```java
// FoodDiaryAdapter dengan delete callback
adapter = new FoodDiaryAdapter(new ArrayList<>(), this::deleteEntry);
binding.recyclerViewMeals.setAdapter(adapter);
```

---

### 3. ✅ Fragment & Navigation Component

Tiga fragment dikelola oleh **Jetpack Navigation Component** melalui `nav_graph.xml`:

```xml
<!-- res/navigation/nav_graph.xml -->
<navigation app:startDestination="@id/nav_search">
    <fragment android:id="@+id/nav_search"
        android:name="...SearchFragment" />
    <fragment android:id="@+id/nav_diary"
        android:name="...DiaryFragment" />
    <fragment android:id="@+id/nav_settings"
        android:name="...SettingsFragment" />
</navigation>
```

Setup di `MainActivity`:
```java
NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
        .findFragmentById(R.id.nav_host_fragment);
navController = navHostFragment.getNavController();
NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
```

---

### 4. ✅ Background Thread (Executor & Handler)

Semua operasi database Room dijalankan di **latar belakang** menggunakan `ExecutorService` dan hasilnya dikirim ke UI Thread melalui `Handler`:

```java
// DetailActivity.java — Simpan makanan ke Room DB di background thread
ExecutorService executor = Executors.newSingleThreadExecutor();
Handler mainHandler = new Handler(Looper.getMainLooper());

executor.execute(() -> {
    // Background thread: operasi database
    AppDatabase.getInstance(this).foodEntryDao().insertFood(entry);
    
    // Kembali ke UI thread
    mainHandler.post(() -> {
        Toast.makeText(this, "Tersimpan!", Toast.LENGTH_SHORT).show();
    });
});
```

---

### 5. ✅ Networking dengan Retrofit

Mengambil data dari **Edamam Food Database API** menggunakan Retrofit2:

```java
// SpoonacularService.java — Interface endpoint API
@GET("api/food-database/v2/parser")
Call<EdamamResponse> getNutrition(
    @Query("app_id")  String appId,
    @Query("app_key") String appKey,
    @Query("ingr")    String query
);
```

```java
// SearchFragment.java — Panggil API dan tampilkan hasil
RetrofitClient.getInstance().getService()
        .getNutrition(APP_ID, APP_KEY, query)
        .enqueue(new Callback<EdamamResponse>() {
            @Override
            public void onResponse(...) {
                // Tampilkan data ke RecyclerView
            }
            @Override
            public void onFailure(...) {
                // Tampilkan layar offline + tombol Refresh
                showOffline(true, "Connection Failed", t.getMessage());
            }
        });
```

**Tombol Refresh** saat koneksi gagal:
```java
binding.btnRefresh.setOnClickListener(v -> loadRecipes(currentTag));
```

---

### 6. ✅ Local Data Persistent

**a. Room Database (SQLite)** — Untuk menyimpan jurnal makanan harian:

```java
// FoodEntryDao.java
@Dao
public interface FoodEntryDao {
    @Insert
    void insertFood(FoodEntry entry);
    
    @Query("SELECT * FROM food_entries WHERE date = :date ORDER BY id DESC")
    List<FoodEntry> getFoodsByDate(String date);
    
    @Query("DELETE FROM food_entries WHERE id = :id")
    void deleteFoodById(int id);
}
```

**b. SharedPreferences** — Untuk menyimpan preferensi tema:

```java
// SettingsFragment.java
SharedPreferences prefs = requireActivity()
        .getSharedPreferences("nutrigo_prefs", Context.MODE_PRIVATE);
prefs.edit().putBoolean("dark_mode", isChecked).apply();
```

Data jurnal tetap dapat **ditampilkan saat offline** karena disimpan lokal di Room Database perangkat.

---

### 7. ✅ Dark Theme & Light Theme

Mendukung dua tema menggunakan `Theme.Material3.DayNight.NoActionBar`:

```java
// SettingsFragment.java — Toggle tema secara dinamis
AppCompatDelegate.setDefaultNightMode(
    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
);
requireActivity().recreate(); // Terapkan tema tanpa restart manual
```

Tema didefinisikan di dua file terpisah:
- `res/values/themes.xml` → Tema **Light** (latar putih, teks gelap)
- `res/values-night/themes.xml` → Tema **Dark** (latar `#121212`, teks putih)

---

## 📦 Cara Menjalankan Aplikasi

### Prasyarat
- **Android Studio** Ladybug (2024.2.1) atau versi lebih baru
- **JDK 11** atau lebih baru
- Perangkat Android dengan API Level **26+** (Android 8.0 Oreo) atau Emulator
- Koneksi internet (untuk fitur pencarian makanan dari API)

### Langkah Instalasi

**1. Clone repositori**
```bash
git clone https://github.com/nurulmarisa/nutrigo.git
cd nutrigo
```

**2. Buka di Android Studio**
```
File → Open → pilih folder proyek
```

**3. Sinkronisasi Gradle**
```
Klik "Sync Now" pada banner notifikasi Gradle, atau:
File → Sync Project with Gradle Files
```

**4. Build dan Jalankan**
```
Klik tombol ▶ Run (Shift + F10)
Pilih perangkat/emulator yang diinginkan
```

Atau melalui terminal:
```bash
./gradlew assembleDebug
```

---

## 🌐 API Reference

Aplikasi ini menggunakan **Edamam Food Database API**:

| Parameter | Nilai |
|-----------|-------|
| **Base URL** | `https://api.edamam.com/` |
| **Endpoint** | `api/food-database/v2/parser` |
| **Method** | `GET` |
| **Auth** | `app_id` + `app_key` (query parameter) |

**Contoh Request:**
```
GET https://api.edamam.com/api/food-database/v2/parser?ingr=chicken&app_id={ID}&app_key={KEY}
```

**Contoh Response:**
```json
{
  "hints": [
    {
      "food": {
        "label": "Chicken",
        "image": "https://...",
        "nutrients": {
          "ENERC_KCAL": 215.0,
          "PROCNT": 18.0,
          "FAT": 15.0,
          "CHOCDF": 0.0
        }
      }
    }
  ]
}
```

---

## 📝 Konvensi Commit

Proyek ini mengikuti **Conventional Commits** untuk menjaga riwayat Git yang bersih dan mudah dibaca:

| Prefix | Kegunaan | Contoh |
|--------|----------|--------|
| `feat` | Fitur baru | `feat: add food diary fragment` |
| `fix` | Perbaikan bug | `fix: resolve status bar overlap in detail screen` |
| `docs` | Dokumentasi | `docs: create readme file` |
| `style` | Perubahan UI/CSS | `style: adjust card padding and typography` |
| `refactor` | Refaktor kode | `refactor: optimize room database query` |
| `chore` | Pembaruan dependency/build | `chore: update retrofit to latest version` |
| `perf` | Peningkatan performa | `perf: lazy load images with glide` |

---

## 👩‍💻 Informasi Pengembang

<div align="center">

**Nurul Marisa**

*Mahasiswa Praktikum Pemrograman Mobile*
*Semester 4 — 2026*

[![GitHub](https://img.shields.io/badge/GitHub-nurulmarisa-181717?style=flat-square&logo=github)](https://github.com/nurulmarisa)

</div>

---

<div align="center">

Dibuat dengan ❤️ menggunakan **Java** & **Android Studio**

*© 2026 NutriGo — Tugas Final Lab Mobile*

</div>

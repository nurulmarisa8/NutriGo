import os
import re

strings_to_add = """
    <string name="detail_nutrisi">Detail Nutrisi</string>
    <string name="add_as">Tambahkan sebagai:</string>
    <string name="save_short">+ Simpan</string>
    <string name="save">Simpan</string>
    <string name="target_kalori">Target Kalori:</string>
    <string name="todays_meals">Today\\'s Meals</string>
    <string name="no_meals_today">Belum ada makanan hari ini</string>
    <string name="search_hint">Cari makanan...</string>
    <string name="no_data">Tidak Ada Data</string>
    <string name="check_internet">Periksa koneksi internet atau coba kata kunci lain.</string>
    <string name="try_again">Coba Lagi</string>
    <string name="theme_setting">Tema Tampilan</string>
    <string name="dark_mode">Mode Gelap (Dark Mode)</string>
    <string name="dark_mode_desc">Aktifkan tema gelap untuk kenyamanan mata di malam hari.</string>
    <string name="chip_all">Semua</string>
    <string name="chip_breakfast">Breakfast</string>
    <string name="chip_lunch">Lunch</string>
    <string name="chip_snack">Snack</string>
    <string name="saving">Menyimpan...</string>
    <string name="saved_check">✓ Tersimpan!</string>
    <string name="saved_success">Berhasil disimpan ke jurnal!</string>
    <string name="target_saved">Target kalori disimpan</string>
    <string name="high_protein">Protein Tinggi</string>
    <string name="high_carb">Karbo Tinggi</string>
    <string name="low_calorie">Rendah Kalori</string>
    <string name="motto">Track Your Nutrition, Own Your Life</string>
    <string name="per_100g">per 100g</string>
    <string name="protein">Protein</string>
    <string name="carbs">Carbs</string>
    <string name="fat">Fat</string>
"""

strings_file = "app/src/main/res/values/strings.xml"

with open(strings_file, "r") as f:
    content = f.read()

if "detail_nutrisi" not in content:
    content = content.replace("</resources>", strings_to_add + "</resources>")
    with open(strings_file, "w") as f:
        f.write(content)


replacements_xml = {
    'android:text="Detail Nutrisi"': 'android:text="@string/detail_nutrisi"',
    'android:text="Tambahkan sebagai:"': 'android:text="@string/add_as"',
    'android:text="+ Simpan"': 'android:text="@string/save_short"',
    'android:text="Simpan"': 'android:text="@string/save"',
    'android:text="Target Kalori:"': 'android:text="@string/target_kalori"',
    'android:text="Today\'s Meals"': 'android:text="@string/todays_meals"',
    'android:text="Belum ada makanan hari ini"': 'android:text="@string/no_meals_today"',
    'android:hint="Cari makanan..."': 'android:hint="@string/search_hint"',
    'android:text="Tidak Ada Data"': 'android:text="@string/no_data"',
    'android:text="Periksa koneksi internet atau coba kata kunci lain."': 'android:text="@string/check_internet"',
    'android:text="Coba Lagi"': 'android:text="@string/try_again"',
    'android:text="Pengaturan"': 'android:text="@string/settings"',
    'android:text="Tema Tampilan"': 'android:text="@string/theme_setting"',
    'android:text="Mode Gelap (Dark Mode)"': 'android:text="@string/dark_mode"',
    'android:text="Aktifkan tema gelap untuk kenyamanan mata di malam hari."': 'android:text="@string/dark_mode_desc"',
    'android:text="Semua"': 'android:text="@string/chip_all"',
    'android:text="Breakfast"': 'android:text="@string/chip_breakfast"',
    'android:text="Lunch"': 'android:text="@string/chip_lunch"',
    'android:text="Snack"': 'android:text="@string/chip_snack"',
    'android:text="Track Your Nutrition, Own Your Life"': 'android:text="@string/motto"',
    'android:text="Protein"': 'android:text="@string/protein"',
    'android:text="Carbs"': 'android:text="@string/carbs"',
    'android:text="Fat"': 'android:text="@string/fat"',
}

for root, _, files in os.walk("app/src/main/res/layout"):
    for file in files:
        if file.endswith(".xml"):
            path = os.path.join(root, file)
            with open(path, "r") as f:
                c = f.read()
            for old, new in replacements_xml.items():
                c = c.replace(old, new)
            with open(path, "w") as f:
                f.write(c)

replacements_java = {
    '.setText("Menyimpan...")': '.setText(R.string.saving)',
    '.setText("✓ Tersimpan!")': '.setText(R.string.saved_check)',
    'Toast.makeText(DetailActivity.this,\n                        "Berhasil disimpan ke jurnal!",\n                        Toast.LENGTH_SHORT).show();': 'Toast.makeText(DetailActivity.this,\n                        getString(R.string.saved_success),\n                        Toast.LENGTH_SHORT).show();',
    'Toast.makeText(getContext(), "Target kalori disimpan", android.widget.Toast.LENGTH_SHORT).show()': 'Toast.makeText(getContext(), getString(R.string.target_saved), android.widget.Toast.LENGTH_SHORT).show()',
    '.setText("Protein Tinggi")': '.setText(R.string.high_protein)',
    '.setText("Karbo Tinggi")': '.setText(R.string.high_carb)',
    '.setText("Rendah Kalori")': '.setText(R.string.low_calorie)',
    '.setText("per 100g")': '.setText(R.string.per_100g)',
}

for root, _, files in os.walk("app/src/main/java"):
    for file in files:
        if file.endswith(".java"):
            path = os.path.join(root, file)
            with open(path, "r") as f:
                c = f.read()
            # Also handle single-line version of toast just in case
            c = c.replace('Toast.makeText(DetailActivity.this, "Berhasil disimpan ke jurnal!", Toast.LENGTH_SHORT).show();', 
                          'Toast.makeText(DetailActivity.this, getString(R.string.saved_success), Toast.LENGTH_SHORT).show();')
            for old, new in replacements_java.items():
                c = c.replace(old, new)
            with open(path, "w") as f:
                f.write(c)


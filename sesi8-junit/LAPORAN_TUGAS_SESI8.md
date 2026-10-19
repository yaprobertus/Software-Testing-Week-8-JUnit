# Tugas Sesi 8 — Unit Testing (JUnit 5)

**Nama:** Jap Robertus Kurniawan Setiabudi (Bert) · **NRP:** 5803024004
**Mata kuliah:** Software Testing · **Dosen:** Philipus Suryo Subandoro

---

## 1. Ringkasan Tugas & Pemenuhan Persyaratan

| # | Persyaratan soal | Pemenuhan | Lokasi |
|---|---|---|---|
| 0 | 1 class Java, minimal 3 method berlogika bersyarat | `StudentEvaluator` berisi 3 method: `classifyGrade` (klasifikasi), `isEligibleForExam` (validasi), `calculateFinalScore` (perhitungan) | `src/main/java/.../StudentEvaluator.java` |
| 1a | Minimal 8 test | **17** method `@Test` + 4 method `@ParameterizedTest` di `StudentEvaluatorTest` (total 49 eksekusi di class ini, ditambah 7 eksekusi di `RegistrationValidatorTest` = 56) | `StudentEvaluatorTest.java` |
| 1b | Minimal 1 `@ParameterizedTest` berdasar BVA | **4**: BVA grade (16 data), BVA di luar rentang (4 data), BVA kehadiran (7 data), kombinasi nilai akhir (5 data) | `classifyGrade_bva`, `classifyGrade_diLuarRentang_ditolak`, `isEligibleForExam_bva`, `calculateFinalScore_kombinasi` |
| 2 | Minimal 2 test `assertThrows` (skenario negatif) | **8** pemakaian `assertThrows` (nilai negatif, >100, kehadiran negatif/>100, tugas/UTS/UAS tidak valid), sebagian juga memeriksa pesan exception | `StudentEvaluatorTest.java` |
| 3 | Memakai `@BeforeEach` dan `@DisplayName` | `@BeforeEach setUp()` (+ `@AfterEach tearDown()`); `@DisplayName` di class dan hampir semua test | kedua class test |
| 4 | Tangkapan layar `mvn test` / IDE, semua lulus | Lihat bagian 4 (harus kamu ambil sendiri dari laptopmu) | — |

## 2. Latihan Kelas: Validasi Umur Pendaftar

Requirement: `isEligible(int umur)` → `true` jika umur 17–60 (inklusif), melempar `IllegalArgumentException` jika umur negatif.

**Soal 1 — Nilai uji BVA (6 nilai batas + 1 negatif)**

| Nilai | Alasan | Hasil diharapkan |
|---|---|---|
| 16 | tepat di bawah batas bawah | false |
| 17 | batas bawah | true |
| 18 | tepat di atas batas bawah | true |
| 59 | tepat di bawah batas atas | true |
| 60 | batas atas | true |
| 61 | tepat di atas batas atas | false |
| -1 | negatif (skenario negatif) | `IllegalArgumentException` |

**Soal 2 — Test class JUnit 5:** `RegistrationValidatorTest.java` (1 `@ParameterizedTest` dengan 6 data BVA + 1 `assertThrows` untuk -1).

**Soal 3 — Jika implementasi salah menulis `umur > 17` alih-alih `umur >= 17`, test mana yang gagal?**
Yang gagal adalah data uji **umur 17** (batas bawah) pada `isEligible_bva`: diharapkan `true`, tetapi implementasi salah mengembalikan `false`. Data 16, 18, 59, 60, 61 tetap lulus karena tidak melewati batas tepat itu. Ini menunjukkan mengapa nilai tepat di batas wajib diuji. (Sudah diverifikasi dengan menyuntikkan bug tersebut: tepat 1 test gagal.)

## 3. Latihan Pemahaman Mandiri

**1. Perbedaan `@BeforeEach` dan `@BeforeAll`?**
`@BeforeEach` dijalankan sebelum *setiap* method test (dipakai membuat objek/keadaan baru agar tiap test bersih). `@BeforeAll` dijalankan *sekali* per class sebelum semua test, dan method-nya harus `static` (dipakai untuk setup mahal, misalnya koneksi/resource bersama).

**2. Mengapa test harus independen satu sama lain?**
Urutan eksekusi test tidak dijamin. Jika test saling bergantung, satu kegagalan memicu kegagalan beruntun yang membingungkan, hasil bisa berubah ketika urutan/paralelisme berubah (tidak *repeatable*), dan sulit menemukan penyebab bug. Independensi membuat tiap test bisa dijalankan sendiri dan hasilnya dapat dipercaya (prinsip **I** pada FIRST).

**3. Kapan `@ParameterizedTest` lebih tepat daripada beberapa `@Test` terpisah?**
Saat logika pengujiannya sama dan hanya data masukan/hasil yang berbeda, misalnya tabel nilai hasil EP/BVA. Hasilnya lebih ringkas (tanpa duplikasi kode), mudah menambah data, dan tiap data dilaporkan sebagai hasil terpisah.

**4. "Coverage 100% sudah menjamin tidak ada bug." Benar atau salah?**
**Salah.** Coverage hanya mengukur seberapa banyak kode yang *dijalankan*, bukan apakah hasilnya *diverifikasi*. Test tanpa assertion bermakna tetap bisa memberi coverage 100%. Coverage juga tidak menjamin semua kombinasi input, nilai batas, atau requirement yang hilang ikut teruji.

## 4. Cara Menjalankan & Mengambil Screenshot

Prasyarat: JDK 17+ dan Maven.

```bash
cd sesi8-junit
mvn test
```

Ambil tangkapan layar bagian akhir output yang menunjukkan:

```
[INFO] Tests run: 56, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Jika memakai IDE (IntelliJ/Eclipse/VS Code): klik kanan folder `src/test/java` → *Run All Tests*, lalu screenshot panel hasil yang semuanya hijau. Sertakan juga nama/jam di layar bila dosen meminta bukti orisinalitas.

## 5. Kepatuhan Prinsip FIRST

- **Fast:** tiap test murni logika, selesai dalam milidetik (seluruh 56 eksekusi < 1 detik).
- **Independent:** objek baru dibuat di `@BeforeEach`; tidak ada state statis bersama.
- **Repeatable:** tanpa database, jaringan, waktu, atau random.
- **Self-validating:** semua pemeriksaan memakai assertion, tanpa cek manual.
- **Timely:** test ditulis bersamaan dengan class-nya, dan data uji berasal dari BVA.
- Nama test deskriptif (bukan `test1`), satu tujuan per test, tanpa mengejar coverage kosong.

## 6. Bukti Kualitas Test (Uji Mutasi Manual)

Bug sengaja disuntikkan ke source, lalu test dijalankan. Semua bug terdeteksi:

| Bug yang disuntikkan | Test yang menangkap |
|---|---|
| `umur >= 17` → `umur > 17` | `isEligible_bva` (data 17) |
| `umur <= 60` → `umur < 60` | `isEligible_bva` (data 60) |
| `score >= 85` → `score > 85` | `classifyGrade_bva` (data 85) |
| `score >= 40` → `score > 40` | `classifyGrade_bva` (data 40) |
| kehadiran `>= 75` → `> 75` | `isEligibleForExam_bva`, `isEligibleForExam_tepat75_boleh` |
| bobot UAS 0.40 → 0.30 | 4 test perhitungan nilai akhir |
| validasi `> 100` → `>= 100` | 9 test |
| validasi `< 0` → `<= 0` | 9 test |

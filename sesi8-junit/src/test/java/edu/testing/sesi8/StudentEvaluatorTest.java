package edu.testing.sesi8;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test untuk StudentEvaluator (Tugas Sesi 8).
 * - Pola Arrange-Act-Assert
 * - Data uji dari Boundary Value Analysis (BVA)
 * - Prinsip FIRST: tiap test independen (objek baru lewat @BeforeEach), tanpa database/jaringan.
 */
@DisplayName("StudentEvaluator")
class StudentEvaluatorTest {

    private static final double DELTA = 0.001;

    private StudentEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new StudentEvaluator();   // Arrange: objek baru sebelum tiap test
    }

    @AfterEach
    void tearDown() {
        evaluator = null;                      // Cleanup setelah tiap test
    }

    // ================= classifyGrade =================

    @Test
    @DisplayName("classifyGrade: nilai maksimum 100 mendapat A")
    void classifyGrade_nilaiMaksimum_mendapatA() {
        String hasil = evaluator.classifyGrade(100);      // Act
        assertEquals("A", hasil);                         // Assert
    }

    @Test
    @DisplayName("classifyGrade: nilai minimum 0 mendapat E")
    void classifyGrade_nilaiMinimum_mendapatE() {
        assertEquals("E", evaluator.classifyGrade(0));
    }

    @Test
    @DisplayName("classifyGrade: satu nilai tipikal tiap kelas (A-E) dengan assertAll")
    void classifyGrade_nilaiTipikalSetiapKelas() {
        assertAll("nilai tipikal",
                () -> assertEquals("A", evaluator.classifyGrade(92)),
                () -> assertEquals("B", evaluator.classifyGrade(77)),
                () -> assertEquals("C", evaluator.classifyGrade(62)),
                () -> assertEquals("D", evaluator.classifyGrade(47)),
                () -> assertEquals("E", evaluator.classifyGrade(20)));
    }

    @Test
    @DisplayName("classifyGrade: hasil tidak pernah null dan berupa satu huruf")
    void classifyGrade_hasilTidakNullDanSatuHuruf() {
        String hasil = evaluator.classifyGrade(50);
        assertNotNull(hasil);
        assertEquals(1, hasil.length());
    }

    @ParameterizedTest(name = "nilai {0} -> grade {1}")
    @DisplayName("classifyGrade: BVA di setiap batas kelas")
    @CsvSource({
            "0, E", "1, E", "39, E",
            "40, D", "41, D", "54, D",
            "55, C", "56, C", "69, C",
            "70, B", "71, B", "84, B",
            "85, A", "86, A", "99, A", "100, A"
    })
    void classifyGrade_bva(int nilai, String gradeDiharapkan) {
        assertEquals(gradeDiharapkan, evaluator.classifyGrade(nilai));
    }

    @Test
    @DisplayName("classifyGrade: nilai negatif ditolak dengan IllegalArgumentException")
    void classifyGrade_nilaiNegatif_melemparException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> evaluator.classifyGrade(-1));
        assertEquals("Nilai harus di antara 0 dan 100", ex.getMessage());
    }

    @Test
    @DisplayName("classifyGrade: nilai di atas 100 ditolak dengan IllegalArgumentException")
    void classifyGrade_nilaiDiAtas100_melemparException() {
        assertThrows(IllegalArgumentException.class, () -> evaluator.classifyGrade(101));
    }

    @ParameterizedTest(name = "nilai {0} tidak valid")
    @DisplayName("classifyGrade: BVA di luar rentang (-1 dan 101) ditolak")
    @ValueSource(ints = {-1, 101, -100, 1000})
    void classifyGrade_diLuarRentang_ditolak(int nilaiTidakValid) {
        assertThrows(IllegalArgumentException.class, () -> evaluator.classifyGrade(nilaiTidakValid));
    }

    // ================= isEligibleForExam =================

    @Test
    @DisplayName("isEligibleForExam: kehadiran tepat 75% boleh ikut ujian")
    void isEligibleForExam_tepat75_boleh() {
        assertTrue(evaluator.isEligibleForExam(75));
    }

    @Test
    @DisplayName("isEligibleForExam: kehadiran 74% tidak boleh ikut ujian")
    void isEligibleForExam_74_tidakBoleh() {
        assertFalse(evaluator.isEligibleForExam(74));
    }

    @Test
    @DisplayName("isEligibleForExam: kehadiran 0% dan 100% (batas rentang) diproses tanpa error")
    void isEligibleForExam_batasRentang() {
        assertAll(
                () -> assertFalse(evaluator.isEligibleForExam(0)),
                () -> assertTrue(evaluator.isEligibleForExam(100)));
    }

    @ParameterizedTest(name = "kehadiran {0}% -> boleh ujian = {1}")
    @DisplayName("isEligibleForExam: BVA di sekitar batas 75%")
    @CsvSource({"0, false", "1, false", "74, false", "75, true", "76, true", "99, true", "100, true"})
    void isEligibleForExam_bva(int kehadiran, boolean diharapkan) {
        assertEquals(diharapkan, evaluator.isEligibleForExam(kehadiran));
    }

    @Test
    @DisplayName("isEligibleForExam: kehadiran negatif ditolak")
    void isEligibleForExam_negatif_melemparException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> evaluator.isEligibleForExam(-1));
        assertEquals("Kehadiran harus di antara 0 dan 100", ex.getMessage());
    }

    @Test
    @DisplayName("isEligibleForExam: kehadiran di atas 100% ditolak")
    void isEligibleForExam_diAtas100_melemparException() {
        assertThrows(IllegalArgumentException.class, () -> evaluator.isEligibleForExam(101));
    }

    // ================= calculateFinalScore =================

    @Test
    @DisplayName("calculateFinalScore: bobot 30/30/40 dihitung benar (80,70,90 -> 81)")
    void calculateFinalScore_perhitunganNormal() {
        double hasil = evaluator.calculateFinalScore(80, 70, 90);
        assertEquals(81.0, hasil, DELTA);
    }

    @Test
    @DisplayName("calculateFinalScore: semua komponen 100 menghasilkan 100")
    void calculateFinalScore_semuaMaksimum() {
        assertEquals(100.0, evaluator.calculateFinalScore(100, 100, 100), DELTA);
    }

    @Test
    @DisplayName("calculateFinalScore: semua komponen 0 menghasilkan 0")
    void calculateFinalScore_semuaMinimum() {
        assertEquals(0.0, evaluator.calculateFinalScore(0, 0, 0), DELTA);
    }

    @ParameterizedTest(name = "tugas {0}, UTS {1}, UAS {2} -> {3}")
    @DisplayName("calculateFinalScore: beberapa kombinasi nilai batas")
    @CsvSource({
            "60, 60, 60, 60.0",
            "50, 100, 0, 45.0",
            "100, 0, 0, 30.0",
            "0, 100, 0, 30.0",
            "0, 0, 100, 40.0"
    })
    void calculateFinalScore_kombinasi(double tugas, double uts, double uas, double diharapkan) {
        assertEquals(diharapkan, evaluator.calculateFinalScore(tugas, uts, uas), DELTA);
    }

    @Test
    @DisplayName("calculateFinalScore: nilai tugas tidak valid ditolak")
    void calculateFinalScore_tugasTidakValid_melemparException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> evaluator.calculateFinalScore(-1, 70, 90));
        assertEquals("Nilai tugas harus di antara 0 dan 100", ex.getMessage());
    }

    @Test
    @DisplayName("calculateFinalScore: nilai UTS tidak valid ditolak")
    void calculateFinalScore_utsTidakValid_melemparException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> evaluator.calculateFinalScore(80, 100.5, 90));
        assertEquals("Nilai UTS harus di antara 0 dan 100", ex.getMessage());
    }

    @Test
    @DisplayName("calculateFinalScore: nilai UAS tidak valid ditolak")
    void calculateFinalScore_uasTidakValid_melemparException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> evaluator.calculateFinalScore(80, 70, 101));
        assertEquals("Nilai UAS harus di antara 0 dan 100", ex.getMessage());
    }
}

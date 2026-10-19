package edu.testing.sesi8;

/**
 * Class yang diuji untuk Tugas Sesi 8.
 * Berisi 3 method dengan logika bersyarat: klasifikasi, validasi, dan perhitungan.
 */
public class StudentEvaluator {

    public static final int MIN_SCORE = 0;
    public static final int MAX_SCORE = 100;
    public static final int MIN_ATTENDANCE_FOR_EXAM = 75;

    /**
     * Klasifikasi: mengubah nilai angka (0-100) menjadi nilai huruf.
     * A >= 85, B >= 70, C >= 55, D >= 40, selain itu E.
     *
     * @throws IllegalArgumentException jika nilai di luar rentang 0-100
     */
    public String classifyGrade(int score) {
        validateRange(score, "Nilai");
        if (score >= 85) return "A";
        if (score >= 70) return "B";
        if (score >= 55) return "C";
        if (score >= 40) return "D";
        return "E";
    }

    /**
     * Validasi: mahasiswa boleh ikut ujian jika kehadiran minimal 75%.
     *
     * @throws IllegalArgumentException jika persentase kehadiran di luar rentang 0-100
     */
    public boolean isEligibleForExam(int attendancePercent) {
        validateRange(attendancePercent, "Kehadiran");
        return attendancePercent >= MIN_ATTENDANCE_FOR_EXAM;
    }

    /**
     * Perhitungan: nilai akhir = 30% tugas + 30% UTS + 40% UAS.
     *
     * @throws IllegalArgumentException jika salah satu komponen di luar rentang 0-100
     */
    public double calculateFinalScore(double assignment, double midterm, double finalExam) {
        validateRange(assignment, "Nilai tugas");
        validateRange(midterm, "Nilai UTS");
        validateRange(finalExam, "Nilai UAS");
        return assignment * 0.30 + midterm * 0.30 + finalExam * 0.40;
    }

    private void validateRange(double value, String label) {
        if (value < MIN_SCORE || value > MAX_SCORE) {
            throw new IllegalArgumentException(label + " harus di antara 0 dan 100");
        }
    }
}

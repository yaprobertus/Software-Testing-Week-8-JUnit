package edu.testing.sesi8;

/**
 * Class untuk Latihan Kelas: Validasi Umur Pendaftar.
 * Requirement: isEligible(int umur) mengembalikan true jika umur 17-60 (inklusif),
 * dan melempar IllegalArgumentException jika umur negatif.
 */
public class RegistrationValidator {

    public boolean isEligible(int umur) {
        if (umur < 0) {
            throw new IllegalArgumentException("Umur negatif");
        }
        return umur >= 17 && umur <= 60;
    }
}

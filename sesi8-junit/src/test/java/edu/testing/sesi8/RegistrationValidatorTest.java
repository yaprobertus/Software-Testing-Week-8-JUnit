package edu.testing.sesi8;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Latihan Kelas Sesi 8: Validasi Umur Pendaftar (umur 17-60 inklusif).
 * 6 nilai BVA: 16, 17, 18, 59, 60, 61 + 1 nilai negatif: -1.
 */
@DisplayName("RegistrationValidator (Latihan Kelas)")
class RegistrationValidatorTest {

    private RegistrationValidator validator;

    @BeforeEach
    void setUp() {
        validator = new RegistrationValidator();
    }

    @ParameterizedTest(name = "umur {0} -> eligible = {1}")
    @DisplayName("isEligible: BVA 6 nilai batas (16,17,18,59,60,61)")
    @CsvSource({"16, false", "17, true", "18, true", "59, true", "60, true", "61, false"})
    void isEligible_bva(int umur, boolean diharapkan) {
        assertEquals(diharapkan, validator.isEligible(umur));
    }

    @Test
    @DisplayName("isEligible: umur negatif melempar IllegalArgumentException")
    void isEligible_umurNegatif_melemparException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> validator.isEligible(-1));
        assertEquals("Umur negatif", ex.getMessage());
    }
}

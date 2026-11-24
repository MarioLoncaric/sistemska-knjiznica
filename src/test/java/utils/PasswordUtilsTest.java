package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilsTest {

    @Test
    void hashMatchesOnlyOriginalPassword() {
        String stored = PasswordUtils.hash("admin123");

        assertTrue(PasswordUtils.matches("admin123", stored));
        assertFalse(PasswordUtils.matches("admin124", stored));
        assertNotEquals(stored, PasswordUtils.hash("admin123"));
    }

    @Test
    void rejectsMalformedStoredValue() {
        assertFalse(PasswordUtils.matches("admin123", "admin123"));
        assertFalse(PasswordUtils.matches("admin123", null));
        assertFalse(PasswordUtils.matches("admin123", "!!!:???"));
    }
}

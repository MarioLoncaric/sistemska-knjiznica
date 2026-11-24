package utils;

import entity.Book;
import exceptions.NegativeValueException;
import exceptions.NonNumericInputException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorUtilsTest {

    @Test
    void acceptsCompoundNames() {
        assertTrue(ValidatorUtils.isNameValid("Ana Marija", "Horvat-Kovač"));
        assertTrue(ValidatorUtils.isNameValid("Đuro", "D'Angelo"));
    }

    @Test
    void rejectsNamesWithDigitsOrLeadingSymbols() {
        assertFalse(ValidatorUtils.isNameValid("Ana2", "Horvat"));
        assertFalse(ValidatorUtils.isNameValid("-Ana", "Horvat"));
        assertFalse(ValidatorUtils.isNameValid("Ana", ""));
    }

    @Test
    void parseNumberThrowsForNonNumericInput() throws NonNumericInputException {
        assertEquals(42, ValidatorUtils.parseNumber(" 42 "));
        assertThrows(NonNumericInputException.class, () -> ValidatorUtils.parseNumber("4a"));
    }

    @Test
    void bookRejectsNegativeValues() {
        Book.Builder builder = new Book.Builder().title("T").numberOfPages(-1).availableCopies(1);
        assertThrows(NegativeValueException.class, builder::build);
    }
}

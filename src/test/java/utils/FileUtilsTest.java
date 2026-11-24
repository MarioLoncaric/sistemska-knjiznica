package utils;

import entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    private static LibraryData sampleData() {
        Book book = new Book("Na Drini ćuprija", "Ivo Andrić", "Svjetlost", Language.HR, 7L, 350, 2, new Genre("Roman"));
        Member member = new Member(3L, "Ana", "Horvat", "ana@example.com", "00000000001", "2001-06-02", "0910000001", true);
        Loan loan = new Loan(11L, member, book, "01.02.2026.", null, LoanStatus.ACTIVE);
        Librarian librarian = new Librarian("Admin", "Admin", 1L);
        return new LibraryData(List.of(member), List.of(book), List.of(librarian), List.of(loan));
    }

    @Test
    void jsonRoundTripKeepsIdsAndText(@TempDir Path dir) throws IOException {
        FileUtils.saveJson(sampleData(), dir);
        LibraryData loaded = FileUtils.loadJson(dir);

        Book book = loaded.getBooks().getFirst();
        assertEquals(7L, book.getBookId());
        assertEquals("Na Drini ćuprija", book.getTitle());
        assertEquals("Roman", book.getGenre().name());
        assertEquals(Language.HR, book.getLanguage());

        assertEquals(3L, loaded.getMembers().getFirst().getIdentifier());

        Loan loan = loaded.getLoans().getFirst();
        assertEquals(11L, loan.getLoanId());
        assertEquals(7L, loan.getBook().getBookId());
        assertEquals(3L, loan.getMember().getIdentifier());
        assertEquals(LoanStatus.ACTIVE, loan.getLoanStatus());
    }

    @Test
    void loadJsonFailsWhenBackupMissing(@TempDir Path dir) {
        assertThrows(IOException.class, () -> FileUtils.loadJson(dir));
    }

    @Test
    void binaryBackupCanBeReadBack(@TempDir Path dir) throws Exception {
        FileUtils.saveBinary(sampleData(), dir);

        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(dir.resolve("backup.bin")))) {
            LibraryData loaded = (LibraryData) in.readObject();
            assertEquals("Ivo Andrić", loaded.getBooks().getFirst().getAuthor());
            assertEquals(11L, loaded.getLoans().getFirst().getLoanId());
        }
    }
}

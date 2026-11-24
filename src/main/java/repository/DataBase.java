package repository;

import entity.Book;
import entity.Loan;
import entity.Member;
import utils.DatabaseUtils;
import utils.LibraryData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {

    private DataBase(){
        throw new UnsupportedOperationException("Utility klasa");
    }

    public static LibraryData loadData(){
        return new LibraryData(
                new MemberRepository().findAll(),
                new BookRepository().findAll(),
                new LibrarianRepository().findAll(),
                new LoanRepository().findAll()
        );
    }

    public static void restoreData(LibraryData data) throws SQLException {
        try (Connection conn = DatabaseUtils.connect()) {
            conn.setAutoCommit(false);
            try (Statement statement = conn.createStatement()) {
                statement.executeUpdate("DELETE FROM LOAN");
                statement.executeUpdate("DELETE FROM BOOK");
                statement.executeUpdate("DELETE FROM MEMBER");

                insertMembers(conn, data);
                insertBooks(conn, data);
                insertLoans(conn, data);

                restartIdentity(statement, "MEMBER");
                restartIdentity(statement, "BOOK");
                restartIdentity(statement, "LOAN");

                conn.commit();
            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    private static void insertMembers(Connection conn, LibraryData data) throws SQLException {
        String query = """
                INSERT INTO MEMBER (ID, FIRST_NAME, LAST_NAME, EMAIL, OIB, BIRTHDAY, PHONE_NUMBER, ACTIVITY)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            for (Member m : data.getMembers()) {
                stmt.setLong(1, m.getIdentifier());
                stmt.setString(2, m.getFirstName());
                stmt.setString(3, m.getLastName());
                stmt.setString(4, m.getEmail());
                stmt.setString(5, m.getOib());
                stmt.setString(6, m.getBirthday());
                stmt.setString(7, m.getPhoneNumber());
                stmt.setBoolean(8, m.isActivity());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private static void insertBooks(Connection conn, LibraryData data) throws SQLException {
        String query = """
                INSERT INTO BOOK (ID, TITLE, AUTHOR, PUBLISHER, NUMBER_OF_PAGES, AVAILABLE_COPIES, LANGUAGE, GENRE)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            for (Book b : data.getBooks()) {
                stmt.setLong(1, b.getBookId());
                stmt.setString(2, b.getTitle());
                stmt.setString(3, b.getAuthor());
                stmt.setString(4, b.getPublisher());
                stmt.setInt(5, b.getNumberOfPages());
                stmt.setInt(6, b.getAvailableCopies());
                stmt.setString(7, b.getLanguage().name());
                stmt.setString(8, b.getGenre().name());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private static void insertLoans(Connection conn, LibraryData data) throws SQLException {
        String query = "INSERT INTO LOAN (ID, BOOK_ID, MEMBER_ID, DATE_FROM, DATE_TO, STATUS) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            for (Loan l : data.getLoans()) {
                stmt.setLong(1, l.getLoanId());
                stmt.setLong(2, l.getBook().getBookId());
                stmt.setLong(3, l.getMember().getIdentifier());
                stmt.setString(4, l.getDateFrom());
                stmt.setString(5, l.getDateTo());
                stmt.setString(6, l.getLoanStatus().name());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private static void restartIdentity(Statement statement, String table) throws SQLException {
        long next = 1;
        try (ResultSet rs = statement.executeQuery("SELECT COALESCE(MAX(ID), 0) + 1 FROM " + table)) {
            if (rs.next()) {
                next = rs.getLong(1);
            }
        }
        statement.executeUpdate("ALTER TABLE " + table + " ALTER COLUMN ID RESTART WITH " + next);
    }
}

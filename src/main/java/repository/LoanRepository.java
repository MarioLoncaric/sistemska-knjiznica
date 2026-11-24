package repository;

import entity.*;
import utils.DatabaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanRepository {

    private static final Logger log = LoggerFactory.getLogger(LoanRepository.class);

    public List<Loan> findAll() {
        List<Loan> loans = new ArrayList<>();
        String query = """
            SELECT
                l.ID as LOAN_ID, l.DATE_FROM, l.DATE_TO, l.STATUS,
                b.ID as BOOK_ID, b.TITLE, b.AUTHOR, b.PUBLISHER, b.NUMBER_OF_PAGES, b.AVAILABLE_COPIES, b.GENRE, b.LANGUAGE,
                m.ID as MEMBER_ID, m.FIRST_NAME, m.LAST_NAME, m.EMAIL, m.OIB, m.BIRTHDAY, m.PHONE_NUMBER, m.ACTIVITY
            FROM LOAN l
            JOIN BOOK b ON l.BOOK_ID = b.ID
            JOIN MEMBER m ON l.MEMBER_ID = m.ID
        """;

        try (Connection connection = DatabaseUtils.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Book book = BookRepository.mapBook(rs, "BOOK_ID");
                Member member = MemberRepository.mapMember(rs, "MEMBER_ID");
                LoanStatus status = LoanStatus.valueOf(rs.getString("STATUS"));

                loans.add(new Loan(
                        rs.getLong("LOAN_ID"), member, book,
                        rs.getString("DATE_FROM"), rs.getString("DATE_TO"), status
                ));
            }
        } catch (SQLException e) {
            log.error("Greška pri dohvatu posudbi", e);
            throw new RuntimeException("Dohvat posudbi nije uspio", e);
        }
        return loans;
    }

    public void save(Loan loan) {
        String decreaseCopies = "UPDATE BOOK SET AVAILABLE_COPIES = AVAILABLE_COPIES - 1 WHERE ID = ? AND AVAILABLE_COPIES > 0";
        String insertLoan = "INSERT INTO LOAN (BOOK_ID, MEMBER_ID, DATE_FROM, STATUS) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtils.connect()) {
            conn.setAutoCommit(false);
            try (PreparedStatement copies = conn.prepareStatement(decreaseCopies);
                 PreparedStatement insert = conn.prepareStatement(insertLoan)) {

                copies.setLong(1, loan.getBook().getBookId());
                if (copies.executeUpdate() == 0) {
                    throw new IllegalStateException("Nema dostupnih primjeraka knjige");
                }

                insert.setLong(1, loan.getBook().getBookId());
                insert.setLong(2, loan.getMember().getIdentifier());
                insert.setString(3, loan.getDateFrom());
                insert.setString(4, loan.getLoanStatus().name());
                insert.executeUpdate();

                conn.commit();
            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            log.error("Greška pri spremanju posudbe", e);
            throw new RuntimeException("Spremanje posudbe nije uspjelo", e);
        }
    }

    public void returnBook(Long loanId, Long bookId, String dateReturned) {
        String closeLoan = "UPDATE LOAN SET DATE_TO = ?, STATUS = 'RETURNED' WHERE ID = ? AND STATUS = 'ACTIVE'";
        String increaseCopies = "UPDATE BOOK SET AVAILABLE_COPIES = AVAILABLE_COPIES + 1 WHERE ID = ?";

        try (Connection conn = DatabaseUtils.connect()) {
            conn.setAutoCommit(false);
            try (PreparedStatement close = conn.prepareStatement(closeLoan);
                 PreparedStatement copies = conn.prepareStatement(increaseCopies)) {

                close.setString(1, dateReturned);
                close.setLong(2, loanId);
                if (close.executeUpdate() == 0) {
                    conn.rollback();
                    return;
                }

                copies.setLong(1, bookId);
                copies.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            log.error("Greška pri vraćanju knjige", e);
            throw new RuntimeException("Povrat knjige nije uspio", e);
        }
    }
}

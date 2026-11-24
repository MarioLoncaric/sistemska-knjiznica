package repository;

import entity.Book;
import entity.Genre;
import entity.Language;
import exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    private static final Logger log = LoggerFactory.getLogger(BookRepository.class);

    public List<Book> findAll(){
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM BOOK";

        try(Connection connection = DatabaseUtils.connect();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery()){

            while(rs.next()){
                books.add(mapBook(rs, "ID"));
            }

        }catch(SQLException ex){
            log.error("Greška pri dohvatu knjiga", ex);
            throw new RuntimeException("Dohvat knjiga nije uspio", ex);
        }
        return books;
    }

    public void save(Book book) {

        String query = """
                INSERT INTO BOOK (TITLE, AUTHOR, PUBLISHER, NUMBER_OF_PAGES, AVAILABLE_COPIES, LANGUAGE, GENRE)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseUtils.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getPublisher());
            statement.setInt(4, book.getNumberOfPages());
            statement.setInt(5, book.getAvailableCopies());
            statement.setString(6, book.getLanguage().name());
            statement.setString(7, book.getGenre().name());

            statement.executeUpdate();

        } catch (SQLException ex) {
            log.error("Greška pri spremanju knjige: {}", book.getTitle(), ex);
            throw new RuntimeException("Neuspješno spremanje u bazu: " + ex.getMessage(), ex);
        }
    }


    public Book findById(Long id) throws NotFoundException {
        String query = "SELECT * FROM BOOK WHERE ID = ?";
        try (Connection conn = DatabaseUtils.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapBook(rs, "ID");
                }
            }
        } catch (SQLException e) {
            log.error("Greška pri traženju knjige ID: {}", id, e);
            throw new RuntimeException("Dohvat knjige nije uspio", e);
        }
        throw new NotFoundException("Knjiga s ID-jem " + id + " ne postoji!");
    }

    public void delete(Long id) {
        try (Connection connection = DatabaseUtils.connect();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM BOOK WHERE ID = ?")) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Greška pri brisanju knjige ID: {}", id, e);
            throw new RuntimeException("Ne mogu obrisati knjigu jer postoje posudbe te knjige.", e);
        }
    }

    public static Book findLast(){
        String query = "SELECT * FROM BOOK ORDER BY ID DESC LIMIT 1";

        try(Connection connection = DatabaseUtils.connect();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery()){
            if(rs.next()){
                return mapBook(rs, "ID");
            }
        }catch(SQLException ex){
            log.error("Greška pri dohvatu zadnje knjige", ex);
        }
        return null;
    }

    static Book mapBook(ResultSet rs, String idColumn) throws SQLException {
        Language language;
        try {
            language = Language.valueOf(rs.getString("LANGUAGE"));
        } catch (IllegalArgumentException | NullPointerException e) {
            language = Language.OSTALO;
        }

        return new Book(
                rs.getString("TITLE"),
                rs.getString("AUTHOR"),
                rs.getString("PUBLISHER"),
                language,
                rs.getLong(idColumn),
                rs.getInt("NUMBER_OF_PAGES"),
                rs.getInt("AVAILABLE_COPIES"),
                new Genre(rs.getString("GENRE"))
        );
    }
}

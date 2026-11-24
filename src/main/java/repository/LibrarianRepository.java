package repository;

import entity.Librarian;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DatabaseUtils;
import utils.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibrarianRepository {

    private static final Logger log = LoggerFactory.getLogger(LibrarianRepository.class);

    public Librarian authenticate(String username, String password) {
        String query = "SELECT * FROM LIBRARIAN WHERE USERNAME = ?";

        try (Connection connection = DatabaseUtils.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next() && PasswordUtils.matches(password, resultSet.getString("PASSWORD"))) {
                    return mapResultSetToLibrarian(resultSet);
                }
            }

        } catch (SQLException e) {
            log.error("Greška pri autentifikaciji korisnika {}: ", username, e);
        }

        return null;
    }

    public List<Librarian> findAll() {
        List<Librarian> librarians = new ArrayList<>();

        try (Connection connection = DatabaseUtils.connect();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM LIBRARIAN");
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                librarians.add(mapResultSetToLibrarian(rs));
            }

        } catch (SQLException e) {
            log.error("Greška pri dohvatu knjižničara", e);
            throw new RuntimeException("Dohvat knjižničara nije uspio", e);
        }
        return librarians;
    }

    private Librarian mapResultSetToLibrarian(ResultSet rs) throws SQLException{
        return new Librarian(rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"), rs.getLong("ID"));
    }

}

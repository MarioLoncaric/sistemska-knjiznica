package repository;

import entity.Member;
import exceptions.NotFoundException;
import utils.DatabaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberRepository {

    private static final Logger log = LoggerFactory.getLogger(MemberRepository.class);

    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM MEMBER";

        try (Connection connection = DatabaseUtils.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                members.add(mapMember(rs, "ID"));
            }

        } catch (SQLException e) {
            log.error("Greška pri dohvatu članova", e);
            throw new RuntimeException("Dohvat članova nije uspio", e);
        }
        return members;
    }

    public void save(Member member) {
        String query = """
            INSERT INTO MEMBER (FIRST_NAME, LAST_NAME, EMAIL, OIB, BIRTHDAY, PHONE_NUMBER, ACTIVITY)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection = DatabaseUtils.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, member.getFirstName());
            statement.setString(2, member.getLastName());
            statement.setString(3, member.getEmail());
            statement.setString(4, member.getOib());
            statement.setString(5, member.getBirthday());
            statement.setString(6, member.getPhoneNumber());
            statement.setBoolean(7, member.isActivity());

            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Greška pri spremanju člana: {}", member.getLastName(), e);
            throw new RuntimeException("Spremanje nije uspjelo (možda OIB već postoji?)", e);
        }
    }

    public void delete(Long id) {

        String query = "DELETE FROM MEMBER WHERE ID = ?";

        try (Connection connection = DatabaseUtils.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Greška pri brisanju člana ID: {}", id, e);
            throw new RuntimeException("Ne mogu obrisati člana (vjerojatno ima posudbe)", e);
        }
    }


    public Member findById(Long id) throws NotFoundException {
        String query = "SELECT * FROM MEMBER WHERE ID = ?";
        try (Connection conn = DatabaseUtils.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapMember(rs, "ID");
                }
            }
        } catch (SQLException e) {
            log.error("Greška pri traženju člana ID: {}", id, e);
            throw new RuntimeException("Dohvat člana nije uspio", e);
        }
        throw new NotFoundException("Član s ID-jem " + id + " ne postoji!");
    }

    static Member mapMember(ResultSet rs, String idColumn) throws SQLException {
        return new Member(
                rs.getLong(idColumn),
                rs.getString("FIRST_NAME"),
                rs.getString("LAST_NAME"),
                rs.getString("EMAIL"),
                rs.getString("OIB"),
                rs.getString("BIRTHDAY"),
                rs.getString("PHONE_NUMBER"),
                rs.getBoolean("ACTIVITY")
        );
    }
}

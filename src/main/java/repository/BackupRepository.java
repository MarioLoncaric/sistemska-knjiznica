package repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BackupRepository {

    private static final Logger log = LoggerFactory.getLogger(BackupRepository.class);

    public void getBookBackup(){
        String deleteQuery = "DROP TABLE IF EXISTS BOOK_BACKUP";

        String createQuery = "CREATE TABLE BOOK_BACKUP AS SELECT * FROM BOOK";

        try(
                Connection connection = DatabaseUtils.connect();
                Statement statement = connection.createStatement()){

            statement.executeUpdate(deleteQuery);

            statement.executeUpdate(createQuery);

        } catch(SQLException ex){
            log.error("Greška prilikom kreiranja backupa", ex);
            throw new RuntimeException("Kreiranje BOOK_BACKUP tablice nije uspjelo", ex);
        }
    }

}

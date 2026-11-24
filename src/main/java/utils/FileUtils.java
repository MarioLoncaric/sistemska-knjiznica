package utils;

import entity.Book;
import entity.Librarian;
import entity.Loan;
import entity.Member;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    private FileUtils(){
        throw new UnsupportedOperationException("Utility klasa");
    }

    public static final Path BACKUP_DIR = Path.of("backup");

    private static final String MEMBERS_FILE = "members.json";
    private static final String BOOKS_FILE = "books.json";
    private static final String LIBRARIANS_FILE = "librarians.json";
    private static final String LOANS_FILE = "loans.json";
    private static final String BINARY_FILE = "backup.bin";

    private static final Type MEMBERS_TYPE = new ArrayList<Member>(){}.getClass().getGenericSuperclass();
    private static final Type BOOKS_TYPE = new ArrayList<Book>(){}.getClass().getGenericSuperclass();
    private static final Type LIBRARIANS_TYPE = new ArrayList<Librarian>(){}.getClass().getGenericSuperclass();
    private static final Type LOANS_TYPE = new ArrayList<Loan>(){}.getClass().getGenericSuperclass();

    private static final JsonbConfig config = new JsonbConfig().withFormatting(true);


    public static <T> void saveDetail(List<T> items, Path file) throws IOException {
        try (Jsonb jsonb = JsonbBuilder.create(config)) {
            Files.writeString(file, jsonb.toJson(items));
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException("Greška pri spremanju datoteke " + file, e);
        }
    }

    public static <T> List<T> loadDetail(Path file, Type type) throws IOException {
        try (Jsonb jsonb = JsonbBuilder.create(config)) {
            return jsonb.fromJson(Files.readString(file), type);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException("Neispravna datoteka " + file, e);
        }
    }

    public static void saveJson(LibraryData data, Path dir) throws IOException {
        Files.createDirectories(dir);
        saveDetail(data.getMembers(), dir.resolve(MEMBERS_FILE));
        saveDetail(data.getBooks(), dir.resolve(BOOKS_FILE));
        saveDetail(data.getLibrarians(), dir.resolve(LIBRARIANS_FILE));
        saveDetail(data.getLoans(), dir.resolve(LOANS_FILE));
    }

    public static LibraryData loadJson(Path dir) throws IOException {
        return new LibraryData(
                loadDetail(dir.resolve(MEMBERS_FILE), MEMBERS_TYPE),
                loadDetail(dir.resolve(BOOKS_FILE), BOOKS_TYPE),
                loadDetail(dir.resolve(LIBRARIANS_FILE), LIBRARIANS_TYPE),
                loadDetail(dir.resolve(LOANS_FILE), LOANS_TYPE)
        );
    }

    public static void saveBinary(LibraryData data, Path dir) throws IOException {
        Files.createDirectories(dir);
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(dir.resolve(BINARY_FILE)))) {
            out.writeObject(data);
        }
    }
}

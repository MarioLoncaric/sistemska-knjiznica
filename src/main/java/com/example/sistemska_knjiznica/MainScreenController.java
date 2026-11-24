package com.example.sistemska_knjiznica;

import entity.Book;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.BackupRepository;
import repository.BookRepository;
import repository.DataBase;
import utils.FileUtils;
import utils.LibraryData;
import utils.MessageUtils;
import utils.XmlLogManager;

import java.io.IOException;

public class MainScreenController {

    private static final Logger log = LoggerFactory.getLogger(MainScreenController.class);

    @FXML
    private BorderPane mainPane;

    private final BackupRepository backupRepository = new BackupRepository();

    private Label lastBookLabel;
    private VBox welcomeBox;

    @FXML
    public void initialize() {
        welcomeBox = new VBox(20);
        welcomeBox.setAlignment(Pos.CENTER);

        Label title = new Label("Dobrodošli u Knjižnica App");
        title.setFont(new Font("Arial", 24));
        title.setStyle("-fx-font-weight: bold;");

        lastBookLabel = new Label("Učitavanje zadnje knjige...");
        lastBookLabel.setFont(new Font("Arial", 16));
        lastBookLabel.setStyle("-fx-font-weight: bold;");

        Button backupButton = new Button("Napravi sigurnosnu kopiju");
        backupButton.setOnAction(e -> runInBackground(backupButton,
                "Sigurnosna kopija je spremljena u bazu (BOOK_BACKUP) i u mapu " + FileUtils.BACKUP_DIR.toAbsolutePath(),
                this::createBackup));

        Button restoreButton = new Button("Vrati iz sigurnosne kopije");
        restoreButton.setOnAction(e -> {
            boolean confirmed = MessageUtils.showConfirmation("Vraćanje podataka",
                    "Svi trenutni članovi, knjige i posudbe bit će zamijenjeni podacima iz sigurnosne kopije. Nastaviti?");
            if (confirmed) {
                runInBackground(restoreButton, "Podaci su vraćeni iz sigurnosne kopije.", this::restoreBackup);
            }
        });

        HBox buttons = new HBox(15, backupButton, restoreButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().forEach(b -> b.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;"));

        welcomeBox.getChildren().addAll(title, lastBookLabel, buttons);
        showHomeScreen();
    }

    @FXML
    public void showHomeScreen() {
        mainPane.setCenter(welcomeBox);
        refreshLastBook();
    }

    private void createBackup() throws IOException {
        LibraryData data = DataBase.loadData();
        backupRepository.getBookBackup();
        FileUtils.saveJson(data, FileUtils.BACKUP_DIR);
        FileUtils.saveBinary(data, FileUtils.BACKUP_DIR);
        XmlLogManager.logAction("Sigurnosna kopija: " + data.getBooks().size() + " knjiga, "
                + data.getMembers().size() + " članova, " + data.getLoans().size() + " posudbi");
    }

    private void restoreBackup() throws Exception {
        LibraryData data = FileUtils.loadJson(FileUtils.BACKUP_DIR);
        DataBase.restoreData(data);
        XmlLogManager.logAction("Vraćanje iz sigurnosne kopije: " + data.getBooks().size() + " knjiga, "
                + data.getMembers().size() + " članova, " + data.getLoans().size() + " posudbi");
        refreshLastBook();
    }

    private void refreshLastBook() {
        Thread.startVirtualThread(() -> {
            Book lastBook = BookRepository.findLast();
            Platform.runLater(() -> lastBookLabel.setText(lastBook != null
                    ? "Posljednja unesena knjiga: " + lastBook.getTitle() + " (" + lastBook.getAuthor() + ")"
                    : "Nema unesenih knjiga."));
        });
    }

    private void runInBackground(Button button, String successMessage, BackgroundTask task) {
        String originalText = button.getText();
        button.setDisable(true);
        button.setText("Radim...");

        Thread.startVirtualThread(() -> {
            try {
                task.run();
                Platform.runLater(() -> MessageUtils.showInfo("Uspjeh", successMessage));
            } catch (Exception ex) {
                log.error("Pozadinski zadatak nije uspio", ex);
                Platform.runLater(() -> MessageUtils.showWarning("Greška", "Operacija nije uspjela: " + ex.getMessage()));
            } finally {
                Platform.runLater(() -> {
                    button.setDisable(false);
                    button.setText(originalText);
                });
            }
        });
    }

    @FunctionalInterface
    private interface BackgroundTask {
        void run() throws Exception;
    }

    private void loadScreen(String fxmlPath){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Pane pane = loader.load();
            mainPane.setCenter(pane);
        }
        catch(IOException ex) {
            log.error("Greška pri učitavanju ekrana ({})", fxmlPath, ex);
            MessageUtils.showWarning("Greška", "Ekran se ne može učitati. Provjerite vezu s bazom.");
        }
    }

   @FXML
    public void showBookSearchScreen() {
       loadScreen("book-search.fxml");
   }

    @FXML
    public void showBookInputScreen(){
        loadScreen("book-input.fxml");
    }

    @FXML
    public void showMemberSearchScreen() {
        loadScreen("member-search.fxml");
    }

    @FXML
    public void showMemberInputScreen(){
        loadScreen("member-input.fxml");
    }

    @FXML
    public void showLoanSearchScreen() {
        loadScreen("loan-search.fxml");
    }

    @FXML
    public void showLoanInputScreen() {
        loadScreen("loan-input.fxml");
    }

    @FXML
    public void showChangeLogScreen() {
        loadScreen("change-log.fxml");
    }
}

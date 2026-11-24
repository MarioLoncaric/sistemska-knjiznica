package com.example.sistemska_knjiznica;

import entity.Librarian;
import exceptions.TimeOutInputException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.LibrarianRepository;
import utils.MessageUtils;
import utils.XmlLogManager;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;

    private static final int MAX_ATTEMPTS = 3;
    private static final int LOCK_SECONDS = 30;

    private final LibrarianRepository librarianRepository = new LibrarianRepository();

    private int failedAttempts = 0;
    private Instant lockedUntil = Instant.MIN;

    @FXML
    public void onLoginButton(){
        String loginText = loginField.getText().trim();
        String passwordText = passwordField.getText();

        if(loginText.isEmpty() || passwordText.isEmpty()){
            MessageUtils.showWarning("Greška", "Unesite korisničko ime i lozinku!");
            return;
        }

        try {
            checkNotLocked();
        } catch (TimeOutInputException e) {
            MessageUtils.showWarning("Prijava zaključana", e.getMessage());
            return;
        }

        Librarian librarian = librarianRepository.authenticate(loginText, passwordText);

        if(librarian != null){
            failedAttempts = 0;
            XmlLogManager.logAction("Prijava: " + loginText);
            openMainScreen();
            return;
        }

        failedAttempts++;
        passwordField.clear();
        if(failedAttempts >= MAX_ATTEMPTS){
            failedAttempts = 0;
            lockedUntil = Instant.now().plusSeconds(LOCK_SECONDS);
            XmlLogManager.logAction("Prijava zaključana nakon " + MAX_ATTEMPTS + " neuspjela pokušaja: " + loginText);
        }
        MessageUtils.showWarning("Greška", "Neispravni podaci za prijavu.");
    }

    private void checkNotLocked() {
        Instant now = Instant.now();
        if (now.isBefore(lockedUntil)) {
            long secondsLeft = Duration.between(now, lockedUntil).toSeconds() + 1;
            throw new TimeOutInputException("Iskoristili ste sve pokušaje. Pokušajte ponovno za " + secondsLeft + " s.");
        }
    }

    private void openMainScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-screen.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 900, 600);

            Stage stage = (Stage) loginField.getScene().getWindow();
            stage.setTitle("Knjižnica - Glavni izbornik");
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            log.error("Ne mogu učitati glavni ekran", e);
            MessageUtils.showWarning("Greška", "Ne mogu učitati glavni ekran.");
        }
    }
}

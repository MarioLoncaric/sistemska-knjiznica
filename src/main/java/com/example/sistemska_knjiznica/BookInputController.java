package com.example.sistemska_knjiznica;

import entity.Book;
import entity.Genre;
import entity.Language;
import exceptions.NegativeValueException;
import exceptions.NonNumericInputException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import repository.BookRepository;
import utils.MessageUtils;
import utils.ValidatorUtils;
import utils.XmlLogManager;

import java.util.ArrayList;
import java.util.List;

import static entity.Language.HR;


public class BookInputController {
    @FXML
    private TextField authorField;

    @FXML
    private TextField copiesField;

    @FXML
    private TextField pagesField;

    @FXML
    private TextField publisherField;

    @FXML
    private TextField titleField;

    @FXML
    private ComboBox<Language> languageComboBox;

    @FXML
    private ComboBox<Genre> genreComboBox;

public static final String HEADER_WARNING = "Neispravan unos";

private final BookRepository bookRepository = new BookRepository();


    @FXML
    public void initialize() {

        if (languageComboBox != null) {
            languageComboBox.setItems(FXCollections.observableArrayList(Language.values()));
            languageComboBox.getSelectionModel().select(HR);
        }

        if (genreComboBox != null) {
            List<Genre> genres = new ArrayList<>();
            genres.add(new Genre("Roman"));
            genres.add(new Genre("Tehnologija"));
            genres.add(new Genre("Fantazija"));
            genres.add(new Genre("Drama"));
            genres.add(new Genre("Dječji"));


            genreComboBox.setItems(FXCollections.observableArrayList(genres));
            genreComboBox.getSelectionModel().selectFirst();


        }
    }


    @FXML
    public void saveBook(){
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String publisher = publisherField.getText().trim();
        String pagesText = pagesField.getText().trim();
        String copiesText = copiesField.getText().trim();

        Language selectedLanguage = languageComboBox.getValue();
        Genre selectedGenre = genreComboBox.getValue();

        if (selectedLanguage == null || selectedGenre == null) {
            MessageUtils.showWarning(HEADER_WARNING, "Molimo odaberite jezik i žanr.");
            return;
        }

        if (title.isEmpty() || author.isEmpty() || publisher.isEmpty() || pagesText.isEmpty() || copiesText.isEmpty()) {
            MessageUtils.showWarning(HEADER_WARNING, "Molimo ispunite sva polja!");
            return;
        }

        Book newBook;
        try{
            int pages = ValidatorUtils.parseNumber(pagesText);
            int copies = ValidatorUtils.parseNumber(copiesText);

            if(copies == 0){
                MessageUtils.showWarning(HEADER_WARNING, "Količina mora biti barem 1.");
                return;
            }

            newBook = new Book.Builder()
                    .title(title)
                    .author(author)
                    .publisher(publisher)
                    .language(selectedLanguage)
                    .genre(selectedGenre)
                    .numberOfPages(pages)
                    .availableCopies(copies)
                    .build();

        } catch(NonNumericInputException | NegativeValueException e){
            MessageUtils.showWarning(HEADER_WARNING, e.getMessage());
            return;
        }

        try {
            bookRepository.save(newBook);
            XmlLogManager.logAction("Unesena knjiga: '" + title + "' (" + author + ")");
            MessageUtils.showInfo("Uspjeh", "Knjiga '" + title + "' uspješno je spremljena u bazu!");
            clearFields();
        } catch (Exception e) {
            MessageUtils.showWarning("Greška", "Spremanje nije uspjelo: " + e.getMessage());
        }

    }

    @FXML
    private void clearFields() {
        titleField.clear();
        authorField.clear();
        publisherField.clear();
        pagesField.clear();
        copiesField.clear();
    }


}


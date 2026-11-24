package com.example.sistemska_knjiznica;

import entity.Book;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import repository.BookRepository;
import utils.MessageUtils;
import utils.XmlLogManager;

public class BookSearchController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, Number> idColumn;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> publisherColumn;

    @FXML
    private TableColumn<Book, Number> copiesColumn;

    private final BookRepository bookRepository = new BookRepository();

    private final ObservableList<Book> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        idColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getBookId()));

        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));

        authorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));

        publisherColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPublisher()));

        copiesColumn.setCellValueFactory(cellData-> new SimpleIntegerProperty(cellData.getValue().getAvailableCopies()));

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().add(getDeleteItem());
        bookTable.setContextMenu(contextMenu);

        masterData.setAll(bookRepository.findAll());
        bookTable.setItems(masterData);
    }

    private MenuItem getDeleteItem() {
        MenuItem deleteItem = new MenuItem("Ukloni knjigu");
        deleteItem.setOnAction(e -> {
            Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
            if(selectedBook == null || !MessageUtils.showConfirmation("Brisanje knjige",
                    "Ukloniti knjigu '" + selectedBook.getTitle() + "'?")){
                return;
            }
            try {
                bookRepository.delete(selectedBook.getBookId());
                XmlLogManager.logAction("Uklonjena knjiga: '" + selectedBook.getTitle() + "' (ID " + selectedBook.getBookId() + ")");
                masterData.remove(selectedBook);
                MessageUtils.showInfo("Obrisano", "Knjiga je uklonjena.");
            } catch (RuntimeException ex) {
                MessageUtils.showWarning("Greška", ex.getMessage());
            }
        });
        return deleteItem;
    }

    @FXML
    public void onSearchButtonClick() {
        String titleQuery = titleField.getText().toLowerCase().trim();
        String authorQuery = authorField.getText().toLowerCase().trim();

        if (titleQuery.isEmpty() && authorQuery.isEmpty()) {
            bookTable.setItems(masterData);
            return;
        }

        FilteredList<Book> filteredData = new FilteredList<>(masterData, book -> {
            boolean t = titleQuery.isEmpty() || book.getTitle().toLowerCase().contains(titleQuery);
            boolean a = authorQuery.isEmpty() || book.getAuthor().toLowerCase().contains(authorQuery);
            return t && a;
        });

        bookTable.setItems(filteredData);
    }

}

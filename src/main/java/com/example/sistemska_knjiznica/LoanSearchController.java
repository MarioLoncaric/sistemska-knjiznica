package com.example.sistemska_knjiznica;

import entity.Loan;
import entity.LoanStatus;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import repository.LoanRepository;
import utils.MessageUtils;
import utils.XmlLogManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LoanSearchController {

    @FXML private TextField memberIdField;
    @FXML private TextField bookIdField;
    @FXML private TableView<Loan> loanTable;

    @FXML private TableColumn<Loan, String> idColumn;
    @FXML private TableColumn<Loan, String> memberFirstNameColumn;
    @FXML private TableColumn<Loan, String> memberLastNameColumn;
    @FXML private TableColumn<Loan, String> bookNameColumn;
    @FXML private TableColumn<Loan, String> dateFromColumn;
    @FXML private TableColumn<Loan, String> dateToColumn;
    @FXML private TableColumn<Loan, String> statusColumn;

    private final ObservableList<Loan> masterData = FXCollections.observableArrayList();
    private final LoanRepository loanRepository = new LoanRepository();

    @FXML
    public void initialize(){
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getLoanId())));
        memberFirstNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMember().getFirstName()));
        memberLastNameColumn.setCellValueFactory(data-> new SimpleStringProperty(data.getValue().getMember().getLastName()));
        bookNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBook().getTitle()));
        dateFromColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateFrom()));

        dateToColumn.setCellValueFactory(data->{
            String datum = data.getValue().getDateTo();
            return new SimpleStringProperty((datum == null || datum.isEmpty()) ? "-" : datum);
        });

        statusColumn.setCellValueFactory(data->{
            String status = (data.getValue().getLoanStatus() == LoanStatus.ACTIVE) ? "Posuđeno" : "Vraćeno";
            return new SimpleStringProperty(status);
        });

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().add(getReturnedItem());
        loanTable.setContextMenu(contextMenu);

        loadData();
    }

    private void loadData() {
        masterData.setAll(loanRepository.findAll());
        loanTable.setItems(masterData);
    }

    private MenuItem getReturnedItem() {
        MenuItem returnItem = new MenuItem("Vrati knjigu");
        returnItem.setOnAction(event -> {
            Loan selectedLoan = loanTable.getSelectionModel().getSelectedItem();
            if(selectedLoan == null || selectedLoan.getLoanStatus() != LoanStatus.ACTIVE){
                return;
            }

            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));
            try {
                loanRepository.returnBook(selectedLoan.getLoanId(), selectedLoan.getBook().getBookId(), today);
                XmlLogManager.logAction("Povrat: posudba ID " + selectedLoan.getLoanId()
                        + ", knjiga '" + selectedLoan.getBook().getTitle() + "'");
                loadData();
                MessageUtils.showInfo("Uspjeh", "Knjiga je vraćena!");
            } catch (RuntimeException e) {
                MessageUtils.showWarning("Greška", e.getMessage());
            }
        });
        return returnItem;
    }

    @FXML
    public void onSearchButtonClick() {
        String memberInput = memberIdField.getText().trim();
        String bookInput = bookIdField.getText().trim();

        if (memberInput.isEmpty() && bookInput.isEmpty()) {
            loanTable.setItems(masterData);
            return;
        }

        List<Loan> filtered = masterData.stream().filter(loan -> {
            boolean matchesBook = bookInput.isEmpty() || String.valueOf(loan.getBook().getBookId()).equals(bookInput);
            boolean matchesMember = memberInput.isEmpty() || String.valueOf(loan.getMember().getIdentifier()).equals(memberInput);
            return matchesBook && matchesMember;
        }).toList();

        loanTable.setItems(FXCollections.observableArrayList(filtered));
    }
}

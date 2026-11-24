package com.example.sistemska_knjiznica;

import entity.Book;
import entity.Loan;
import entity.LoanStatus;
import entity.Member;
import exceptions.NonNumericInputException;
import exceptions.NotFoundException;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import repository.BookRepository;
import repository.LoanRepository;
import repository.MemberRepository;
import utils.MessageUtils;
import utils.ValidatorUtils;
import utils.XmlLogManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LoanInputController {

    private static final String WARNING_HEADER = "Greška";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

    @FXML private TextField bookIdField;
    @FXML private TextField memberIdField;
    @FXML private DatePicker datePicker;

    private final BookRepository bookRepository = new BookRepository();
    private final MemberRepository memberRepository = new MemberRepository();
    private final LoanRepository loanRepository = new LoanRepository();

    @FXML
    public void initialize(){
        datePicker.setValue(LocalDate.now());
    }

    @FXML
    public void saveLoan(){
        String bookIdText = bookIdField.getText().trim();
        String memberIdText = memberIdField.getText().trim();
        LocalDate currentDate = datePicker.getValue();

        if(bookIdText.isEmpty() || memberIdText.isEmpty() || currentDate == null){
            MessageUtils.showWarning(WARNING_HEADER, "Morate unijeti ID knjige, ID člana i datum!");
            return;
        }

        try {
            long bookId = ValidatorUtils.parseNumber(bookIdText);
            long memberId = ValidatorUtils.parseNumber(memberIdText);

            Book foundBook = bookRepository.findById(bookId);
            if(foundBook.getAvailableCopies() <= 0){
                MessageUtils.showWarning("Nedostupno", "Svi primjerci knjige '" + foundBook.getTitle() + "' su posuđeni!");
                return;
            }

            Member foundMember = memberRepository.findById(memberId);

            Loan newLoan = new Loan(
                    foundBook,
                    foundMember,
                    currentDate.format(DATE_FORMAT),
                    null,
                    LoanStatus.ACTIVE
            );

            loanRepository.save(newLoan);
            foundBook.borrow();
            XmlLogManager.logAction("Posudba: knjiga '" + foundBook.getTitle() + "' (ID " + bookId + "), član ID " + memberId);

            String deadlineStr = currentDate.plusWeeks(3).format(DATE_FORMAT);
            MessageUtils.showInfo("Uspjeh", "Posudba uspješno kreirana!\nRok povratka: " + deadlineStr
                    + "\nPreostalo primjeraka: " + foundBook.getAvailableCopies());
            clearFields();

        } catch (NonNumericInputException | NotFoundException e) {
            MessageUtils.showWarning(WARNING_HEADER, e.getMessage());
        } catch (Exception e) {
            MessageUtils.showWarning(WARNING_HEADER, "Greška pri spremanju: " + e.getMessage());
        }
    }

    public void clearFields(){
        bookIdField.clear();
        memberIdField.clear();
        datePicker.setValue(LocalDate.now());
    }
}

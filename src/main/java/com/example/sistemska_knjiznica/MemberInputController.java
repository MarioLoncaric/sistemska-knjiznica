package com.example.sistemska_knjiznica;

import entity.Member;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import repository.MemberRepository;
import utils.MessageUtils;
import utils.ValidatorUtils;
import utils.XmlLogManager;

import java.time.LocalDate;

public class MemberInputController {

    private static final String WARNING_HEADER = "Neispravan unos";

    @FXML private DatePicker birthdayDatePicker;
    @FXML private TextField emailField;
    @FXML private TextField nameField;
    @FXML private TextField oibField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField surnameField;

    private final MemberRepository memberRepository = new MemberRepository();

    @FXML
    public void saveMember() {
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String oib = oibField.getText().trim();
        LocalDate birthday = birthdayDatePicker.getValue();
        String phoneNumber = phoneNumberField.getText().trim();
        String email = emailField.getText().trim();
        boolean activity = true;

        if (name.isEmpty() || surname.isEmpty() || oib.isEmpty() || birthday == null || phoneNumber.isEmpty() || email.isEmpty()) {
            MessageUtils.showWarning(WARNING_HEADER, "Molimo ispunite sva polja!");
            return;
        }

        if(!ValidatorUtils.isNameValid(name, surname)){
            MessageUtils.showWarning(WARNING_HEADER, "Ime ili prezime ne smije imati brojke");
            return;
        }

        if(!ValidatorUtils.isBirthdayValid(birthday)){
            MessageUtils.showWarning(WARNING_HEADER, "Datum nije pravilno unesen (osoba mora biti rođena u prošlosti)");
            return;
        }

        if(!ValidatorUtils.isValidOib(oib) ){
            MessageUtils.showWarning(WARNING_HEADER, "OIB mora imati 11 znamenki.");
            return;
        }

        if(!ValidatorUtils.isValidEmail(email)){
            MessageUtils.showWarning(WARNING_HEADER, "E-mail adresa nije ispravna.");
            return;
        }

        if(!ValidatorUtils.isNumberphoneValid(phoneNumber)){
            MessageUtils.showWarning(WARNING_HEADER, "Broj telefona mora imati 9 do 11 znamenki, bez razmaka.");
            return;
        }

        Member newMember = new Member(
                name,
                surname,
                email,
                oib,
                birthday.toString(),
                phoneNumber,
                activity
        );

        try {
            memberRepository.save(newMember);
            XmlLogManager.logAction("Upisan član: " + name + " " + surname);
            MessageUtils.showInfo("Uspjeh", "Član/ica " + name + " uspješno je upisan/a!");
            clearFields();
        } catch (Exception e) {
            MessageUtils.showWarning("Greška", "Spremanje nije uspjelo: " + e.getMessage());
        }
    }

    public void clearFields(){
        nameField.clear();
        surnameField.clear();
        oibField.clear();
        emailField.clear();
        phoneNumberField.clear();
        birthdayDatePicker.setValue(null);
    }
}
package com.example.sistemska_knjiznica;

import entity.Member;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import repository.MemberRepository;
import utils.MessageUtils;
import utils.XmlLogManager;

import java.util.List;

public class MemberSearchController {

    @FXML private TextField idField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;

    @FXML private TableView<Member> memberTable;
    @FXML private TableColumn<Member, String> idColumn;
    @FXML private TableColumn<Member, String> firstNameColumn;
    @FXML private TableColumn<Member, String> lastNameColumn;
    @FXML private TableColumn<Member, String> emailColumn;
    @FXML private TableColumn<Member, String> phoneColumn;

    private final MemberRepository memberRepository = new MemberRepository();
    private ObservableList<Member> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getIdentifier())));
        firstNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLastName()));
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        phoneColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNumber()));

        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = getDeleteItem();
        contextMenu.getItems().add(deleteItem);
        memberTable.setContextMenu(contextMenu);

        loadData();
    }

    private void loadData() {
        List<Member> members = memberRepository.findAll();
        masterData.setAll(members);
        memberTable.setItems(masterData);
    }

    private MenuItem getDeleteItem() {
        MenuItem deleteItem = new MenuItem("Obriši člana");

        deleteItem.setOnAction(event -> {
            Member selectedMember = memberTable.getSelectionModel().getSelectedItem();
            if(selectedMember == null || !MessageUtils.showConfirmation("Brisanje člana",
                    "Obrisati člana " + selectedMember + "?")){
                return;
            }
            try {
                memberRepository.delete(selectedMember.getIdentifier());
                XmlLogManager.logAction("Obrisan član: " + selectedMember);

                masterData.remove(selectedMember);
                MessageUtils.showInfo("Obrisano", "Član je uspješno obrisan.");

            } catch (Exception e) {
                MessageUtils.showWarning("Greška", "Ne mogu obrisati člana jer ima zabilježene posudbe.");
            }
        });
        return deleteItem;
    }

    @FXML
    public void onSearchButtonClick() {
        String idQuery = idField.getText().trim();
        String nameQuery = firstNameField.getText().toLowerCase().trim();
        String surnameQuery = lastNameField.getText().toLowerCase().trim();

        if (idQuery.isEmpty() && nameQuery.isEmpty() && surnameQuery.isEmpty()) {
            memberTable.setItems(masterData);
            return;
        }

        FilteredList<Member> filteredData = new FilteredList<>(masterData, member -> {
            boolean matchId = true;
            if(!idQuery.isEmpty()){
                matchId = String.valueOf(member.getIdentifier()).equals(idQuery);
            }

            boolean matchName = true;
            if(!nameQuery.isEmpty()){
                matchName = member.getFirstName().toLowerCase().contains(nameQuery);
            }

            boolean matchSurname = true;
            if(!surnameQuery.isEmpty()){
                matchSurname = member.getLastName().toLowerCase().contains(surnameQuery);
            }

            return matchId && matchName && matchSurname;
        });

        memberTable.setItems(filteredData);
    }
}
package com.example.sistemska_knjiznica;

import entity.LogEntry;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import utils.XmlLogManager;

import java.util.List;

public class ChangeLogController {

    @FXML private TableView<LogEntry> logTable;
    @FXML private TableColumn<LogEntry, String> timeColumn;
    @FXML private TableColumn<LogEntry, String> actionColumn;

    @FXML
    public void initialize() {
        timeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTimestamp()));
        actionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAction()));

        List<LogEntry> entries = XmlLogManager.getEntries();
        logTable.setItems(FXCollections.observableArrayList(entries.reversed()));
    }
}

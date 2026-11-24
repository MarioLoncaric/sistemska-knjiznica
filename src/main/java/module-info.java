module com.example.sistemska_knjiznica {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires jakarta.json.bind;
    requires jakarta.xml.bind;
    requires java.sql;
    
    requires com.h2database;

    requires javafx.base;


    opens com.example.sistemska_knjiznica to javafx.fxml, javafx.graphics;
    opens entity;
    exports com.example.sistemska_knjiznica;
    opens utils;
}
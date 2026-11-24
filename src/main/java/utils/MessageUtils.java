package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class MessageUtils {

    private MessageUtils(){
        throw new UnsupportedOperationException("Utility klasa");
    }


    public static void showWarning(String header, String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Upozorenje");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void showInfo(String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacija");
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public static boolean showConfirmation(String header, String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrda");
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait().filter(ButtonType.OK::equals).isPresent();
    }
}

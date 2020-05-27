import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;

public class HallController {
    private final String password = "haslo123";
    private static boolean access = false;

    @FXML
    private PasswordField passwordField;

    @FXML
    private GridPane sectionFirst;

    @FXML
    private GridPane sectionSecond;

    public static boolean getAccess() {
        return access;
    }

    private void unlockSeats(Button[][] seats) {
        for (int i = 0; i < seats.length; i++)
            for (int j = 0; j < seats[i].length; j++)
                seats[i][j].setDisable(false);
    }

    public void onSubmitPassword(ActionEvent actionEvent) {
        if (passwordField.getText().equals(password)) {
            access = true;
            passwordField.setPromptText("zalogowano");
            passwordField.setDisable(true);

            unlockSeats(App.sectionFirstSeats);
            unlockSeats(App.sectionSecondSeats);
        } else
            App.blinkLogin(passwordField);

        passwordField.setText("");
    }
}

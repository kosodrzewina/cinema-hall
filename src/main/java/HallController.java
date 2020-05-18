import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

public class HallController {
    private final String password = "haslo123";
    private static boolean access = false;

    @FXML
    private PasswordField passwordField;

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
        }

        passwordField.setText("");
    }
}

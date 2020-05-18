import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class HallController {
    private final String password = "haslo123";
    private boolean access = false;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label test;

    public boolean getAccess() {
        return access;
    }

    public void onSubmitPassword(ActionEvent actionEvent) {
        if (passwordField.getText().equals(password)) {
            access = true;
            test.setText("granted");
        }

        passwordField.setText("");
    }
}

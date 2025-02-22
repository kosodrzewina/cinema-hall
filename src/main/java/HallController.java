import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
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

    @FXML
    private Label selectedSeat;

    @FXML
    private Label selectedShowing;

    @FXML
    private ChoiceBox movieBox;

    @FXML
    private Button submitButton;

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
            movieBox.setDisable(false);
            submitButton.setDisable(false);

            unlockSeats(App.sectionFirstSeats);
            unlockSeats(App.sectionSecondSeats);
        } else
            App.blinkLogin(passwordField);

        passwordField.setText("");
    }

    // returns 0 - first section or 1 - second section
    private static boolean determineSection(String seatMark) {
        return (seatMark.charAt(0) > 'C');
    }

    public void onSubmitButtonClick(ActionEvent actionEvent) {
        if (!selectedSeat.getText().equals("---") && !selectedShowing.getText().equals("---")) {
            boolean sectionIndex = determineSection(App.currentHighlightedSeat.getText());

            if (!sectionIndex) {
                DataManager.saveData(
                        selectedShowing.getText(),
                        App.currentHighlightedSeat.getText(),
                        false,
                        sectionFirst.getColumnCount()
                );

                App.updateSeats(
                        DataManager.loadSeatsState(
                                selectedShowing.getText(),
                                sectionFirst.getRowCount(),
                                sectionFirst.getColumnCount(),
                                sectionSecond.getRowCount(),
                                sectionSecond.getColumnCount()
                        )[0],
                        App.sectionFirstSeats
                );
            } else {
                DataManager.saveData(
                        selectedShowing.getText(),
                        App.currentHighlightedSeat.getText(),
                        true,
                        sectionSecond.getColumnCount()
                );

                App.updateSeats(
                        DataManager.loadSeatsState(
                                selectedShowing.getText(),
                                sectionFirst.getRowCount(),
                                sectionFirst.getColumnCount(),
                                sectionSecond.getRowCount(),
                                sectionSecond.getColumnCount()
                        )[1],
                        App.sectionSecondSeats
                );
            }
        }
    }
}

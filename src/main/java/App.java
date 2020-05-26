import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;

public class App extends Application {
    public static Button[][] sectionFirstSeats, sectionSecondSeats;

    // creates buttons in grid pane and returns array of these buttons
    public Button[][] fillSeats(GridPane gridPane, Label currentSeat, char firstRow) {
        Button[][] seats = new Button[gridPane.getRowCount()][gridPane.getColumnCount()];

        for (int i = 0; i < gridPane.getRowCount(); i++) {
            for (int j = 0; j < gridPane.getColumnCount(); j++) {
                Button temp = new Button(firstRow + Integer.toString(j + 1));
                temp.setId("button" + temp.getText());
                temp.setOnAction(actionEvent -> currentSeat.setText("Wybrane miejsce: " + temp.getText()));
                temp.setDisable(true);

                gridPane.add(temp, j, i);
                seats[i][j] = temp;
            }
            firstRow++;
        }

        return seats;
    }

    public static void updateSeats(boolean[][] data, Button[][] seats) {
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                if (data[i][j])
                    seats[i][j].setDisable(true);
            }
        }
    }

    public static void blinkLogin(PasswordField passwordField) {
        String originalStyle = "-fx-background-color: #FFFFFF";
        String flashStyle = "-fx-background-color: #D32F2F";

        new Thread(() -> {
            boolean blink = true;

            for (int i = 0; i < 6; i++) {
                passwordField.setStyle((blink) ? flashStyle : originalStyle);
                blink = !blink;

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Cinema Hall");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/hall.fxml"));
        Parent root = loader.load();

        BorderPane borderPane = (BorderPane) loader.getNamespace().get("borderPane");
        GridPane sectionFirst = (GridPane) loader.getNamespace().get("sectionFirst");
        GridPane sectionSecond = (GridPane) loader.getNamespace().get("sectionSecond");
        Label currentSeat = (Label) loader.getNamespace().get("currentSeat");
        ChoiceBox movieBox = (ChoiceBox) loader.getNamespace().get("movieBox");

        String[] movies = {"Now You See Me", "Vampire Assassin", "Order of the Black Eagle"};
        movieBox.getItems().addAll(movies);

        sectionFirstSeats = fillSeats(sectionFirst, currentSeat, 'A');
        sectionSecondSeats = fillSeats(sectionSecond, currentSeat, 'D');

        File seatsStateFile = new File("seats_state.txt");
        if (!seatsStateFile.exists())
            DataManager.generateFile(
                    movies,
                    sectionFirst.getRowCount(),
                    sectionFirst.getColumnCount(),
                    sectionSecond.getRowCount(),
                    sectionSecond.getColumnCount()
            );

        stage.setScene(new Scene(root, 1280, 720));
        stage.show();

        stage.setOnCloseRequest(windowEvent -> {
            if (!HallController.getAccess()) {
                blinkLogin((PasswordField) loader.getNamespace().get("passwordField"));
                windowEvent.consume();
            }
        });
    }
}

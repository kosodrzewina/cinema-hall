import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;

public class App extends Application {
    private static String standardButtonBackgroundStyle = "-fx-background-color:\n" +
            "#090a0c,\n" +
            "linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n" +
            "linear-gradient(#20262b, #191d22),\n" +
            "radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0))";
    public static Button[][] sectionFirstSeats, sectionSecondSeats;
    public static Button currentHighlightedSeat;
    private File thumbnailDir = new File("./src/main/resources/thumbnails");
    private File[] thumbnails = thumbnailDir.listFiles();

    // highlights selected seat and makes sure the old one goes back to the standard style
    private void highlightSeat(Button selectedSeat) {
        if (currentHighlightedSeat != null) {
            currentHighlightedSeat.setStyle(
                    (currentHighlightedSeat.isDisable())
                            ? "-fx-background-color:  #D32F2F"
                            : standardButtonBackgroundStyle);
        }

        selectedSeat.setStyle("-fx-background-color: #8BC34A");
    }

    // creates buttons in grid pane and returns array of these buttons
    public Button[][] fillSeats(GridPane gridPane, Label currentSeat, char firstRow) {
        Button[][] seats = new Button[gridPane.getRowCount()][gridPane.getColumnCount()];

        for (int i = 0; i < gridPane.getRowCount(); i++) {
            for (int j = 0; j < gridPane.getColumnCount(); j++) {
                Button temp = new Button(firstRow + Integer.toString(j + 1));
                temp.setId("button" + temp.getText());

                temp.setOnAction(actionEvent -> {
                    highlightSeat(temp);
                    currentHighlightedSeat = temp;
                    currentSeat.setText(temp.getText());
                });

                temp.setDisable(true);

                gridPane.add(temp, j, i);
                seats[i][j] = temp;
            }
            firstRow++;
        }

        return seats;
    }

    public static void updateSeats(boolean[][] data, Button[][] seats) {
        for (int i = 0; i < seats.length; i++)
            for (int j = 0; j < seats[i].length; j++) {
                if (data[i][j]) {
                    seats[i][j].setDisable(true);
                    seats[i][j].setStyle("-fx-background-color:  #D32F2F");
                } else {
                    seats[i][j].setDisable(false);
                    seats[i][j].setStyle(standardButtonBackgroundStyle);
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

        // fx components
        GridPane sectionFirst = (GridPane) loader.getNamespace().get("sectionFirst");
        GridPane sectionSecond = (GridPane) loader.getNamespace().get("sectionSecond");
        Label selectedSeat = (Label) loader.getNamespace().get("selectedSeat");
        Label selectedShowing = (Label) loader.getNamespace().get("selectedShowing");
        ChoiceBox movieBox = (ChoiceBox) loader.getNamespace().get("movieBox");
        ImageView thumbnailView = (ImageView) loader.getNamespace().get("thumbnailView");

        String[] movies = {"Now You See Me", "Vampire Assassin", "Order of the Black Eagle"};
        movieBox.getItems().addAll(movies);

        sectionFirstSeats = fillSeats(sectionFirst, selectedSeat, 'A');
        sectionSecondSeats = fillSeats(sectionSecond, selectedSeat, 'D');

        File seatsStateFile = new File("seats_state.txt");
        if (!seatsStateFile.exists())
            DataManager.generateFile(
                    movies,
                    sectionFirst.getRowCount(),
                    sectionFirst.getColumnCount(),
                    sectionSecond.getRowCount(),
                    sectionSecond.getColumnCount()
            );

        stage.setScene(new Scene(root, 1107, 680));
        stage.show();

        stage.setOnCloseRequest(windowEvent -> {
            if (!HallController.getAccess()) {
                blinkLogin((PasswordField) loader.getNamespace().get("passwordField"));
                windowEvent.consume();
            }
        });

        movieBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (HallController.getAccess()) {
                for (int i = 0; i < movies.length; i++) {
                    if (movies[i] == newValue) {
                        selectedShowing.setText(newValue.toString());
                        selectedSeat.setText("---");

                        for (int j = 0; j < thumbnails.length; j++) {
                            if ((newValue + ".jpg").equals(thumbnails[j].getName())) {
                                thumbnailView.setImage(new Image(getClass().getResource(
                                        "thumbnails/" + thumbnails[j].getName()
                                ).toString()));
                                break;
                            }
                        }

                        boolean[][][] seatsState = DataManager.loadSeatsState(
                                newValue.toString(),
                                sectionFirst.getRowCount(),
                                sectionFirst.getColumnCount(),
                                sectionSecond.getRowCount(),
                                sectionSecond.getColumnCount()
                        );

                        updateSeats(seatsState[0], sectionFirstSeats);
                        updateSeats(seatsState[1], sectionSecondSeats);
                    }
                }
            }
        });
    }
}

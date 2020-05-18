import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class App extends Application {
    public static Button[][] sectionFirstSeats, sectionSecondSeats;

    // creates buttons in grid pane and returns array of these buttons
    public Button[][] fillSeats(GridPane gridPane, char firstRow) {
        Button[][] seats = new Button[gridPane.getRowCount()][gridPane.getColumnCount()];

        for (int i = 0; i < gridPane.getRowCount(); i++) {
            for (int j = 0; j < gridPane.getColumnCount(); j++) {
                Button temp = new Button(firstRow + Integer.toString(j + 1));
                temp.setId("button" + temp.getText());
                temp.setOnAction(actionEvent -> System.out.println("seat " + temp.getText() + " clicked"));
                temp.setDisable(true);

                gridPane.add(temp, j, i);
                seats[i][j] = temp;
            }
            firstRow++;
        }

        return seats;
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
        GridPane sectionFirst =  (GridPane) loader.getNamespace().get("sectionFirst");
        GridPane sectionSecond =  (GridPane) loader.getNamespace().get("sectionSecond");

        sectionFirstSeats = fillSeats(sectionFirst, 'A');
        sectionSecondSeats = fillSeats(sectionSecond, 'D');

        stage.setScene(new Scene(root, 800, 600));
        stage.show();

        stage.setOnCloseRequest(windowEvent -> {
            if (!HallController.getAccess()) {
                blinkLogin((PasswordField) loader.getNamespace().get("passwordField"));
                windowEvent.consume();
            }
        });
    }
}

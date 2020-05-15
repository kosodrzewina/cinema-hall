import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class App extends Application {
    public Button[][] sectionFirstSeats, sectionSecondSeats;

    public Button[][] fillSeats(GridPane gridPane, char firstRow) {
        Button[][] seats = new Button[gridPane.getRowCount()][gridPane.getColumnCount()];

        for (int i = 0; i < gridPane.getRowCount(); i++) {
            for (int j = 0; j < gridPane.getColumnCount(); j++) {
                Button temp = new Button(firstRow + Integer.toString(j + 1));
                gridPane.add(temp, j, i);
                seats[i][j] = temp;
            }
            firstRow++;
        }

        return seats;
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/hall.fxml"));
        Parent root = loader.load();
        GridPane sectionFirst =  (GridPane) loader.getNamespace().get("sectionFirst");
        GridPane sectionSecond =  (GridPane) loader.getNamespace().get("sectionSecond");

        sectionFirstSeats = fillSeats(sectionFirst, 'A');
        sectionSecondSeats = fillSeats(sectionSecond, 'D');

        stage.setScene(new Scene(root, 1280, 720));
        stage.show();
    }
}

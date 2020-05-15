import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/hall.fxml"));
        Parent root = loader.load();
        GridPane sectionFirst =  (GridPane) loader.getNamespace().get("sectionFirst");
        GridPane sectionSecond =  (GridPane) loader.getNamespace().get("sectionSecond");

        stage.setScene(new Scene(root, 1280, 720));
        stage.show();
    }
}

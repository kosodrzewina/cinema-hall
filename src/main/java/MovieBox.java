import javafx.scene.control.ChoiceBox;

public class MovieBox extends ChoiceBox {
    String[] movies;

    public MovieBox(String[] movies) {
        this.movies = movies;
        getItems().addAll(movies);
    }
}

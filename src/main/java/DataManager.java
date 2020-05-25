import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DataManager {
    // @TODO
    public static boolean[][] getSeatState() {
        boolean[][] state = new boolean[0][0];
        return state;
    }

    // generates file with all seats available
    public static void generateFile(String[] movies, int seatingCapacity) {
        try {
            File seatsStateFile = new File("seats_state.txt");
            FileWriter fileWriter = new FileWriter(seatsStateFile, true);

            for (int i = 0; i < movies.length; i++) {
                fileWriter.write(movies[i] + "\n");

                for (int j = 0; j < seatingCapacity; j++)
                    fileWriter.write(0 + "\n");

                fileWriter.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

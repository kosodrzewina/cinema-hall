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
    public static void generateFile(String[] movies, int firstHeight, int firstWidth, int secondHeight, int secondWidth) {
        try {
            File seatsStateFile = new File("seats_state.txt");
            FileWriter fileWriter = new FileWriter(seatsStateFile, true);

            for (int i = 0; i < movies.length; i++) {
                fileWriter.write(movies[i] + " {\n");
                System.out.println("wrote: " + movies[i]);

                for (int j = 0; j < firstHeight; j++) {
                    for (int k = 0; k < firstWidth; k++) {
                        fileWriter.write("\t" + 0 + " ");
                    }
                    fileWriter.write("\n");
                }

                fileWriter.write("\n");

                for (int j = 0; j < secondHeight; j++) {
                    for (int k = 0; k < secondWidth; k++) {
                        fileWriter.write("\t" + 0 + " ");
                    }
                    fileWriter.write("\n");
                }

                fileWriter.write("}\n\n");
            }

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

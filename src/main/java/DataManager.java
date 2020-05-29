import java.io.*;

public class DataManager {
    // gets raw chunk and returns boolean array based on it
    private static boolean[][] dataToBoolArray(StringBuilder chunk, int height, int width) {
        boolean[][] bools = new boolean[height][width];
        StringBuilder cleanedChunk = new StringBuilder();

        // cleaning chunk
        for (int i = 0; i < chunk.length(); i++) {
            if (chunk.charAt(i) == '0' || chunk.charAt(i) == '1')
                cleanedChunk.append(chunk.charAt(i));
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++)
                bools[i][j] = (cleanedChunk.charAt((i * width) + j) != '0');
        }

        return bools;
    }

    // loads state of seats for a given movie
    public static boolean[][][] loadSeatsState(
            String movie, int firstHeight, int firstWidth, int secondHeight, int secondWidth
    ) {
        boolean[][][] state = new boolean[2][][];
        File seatsStateFile = new File("seats_state.txt");

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(seatsStateFile));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals(movie + " {")) {
                    StringBuilder dataChunk = new StringBuilder();

                    // get first chunk
                    while (!(line = bufferedReader.readLine()).equals("\t---"))
                        dataChunk.append(line);

                    state[0] = dataToBoolArray(dataChunk, firstHeight, firstWidth);
                    dataChunk = new StringBuilder();

                    // get second chunk
                    while (!(line = bufferedReader.readLine()).equals("}"))
                        dataChunk.append(line);

                    state[1] = dataToBoolArray(dataChunk, secondHeight, secondWidth);

                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return state;
    }

    // generates file with all seats available
    public static void generateFile(
            String[] movies, int firstHeight, int firstWidth, int secondHeight, int secondWidth
    ) {
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

                fileWriter.write("\t---\n");

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

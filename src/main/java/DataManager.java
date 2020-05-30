import java.io.*;

public class DataManager {
    // save seat as occupied (0 - first section, 1 - second section)
    public static void saveData(String movie, String seatMark, boolean section, int width) {
        File seatsStateFile = new File("seats_state.txt");
        String endChunk = (!section) ? "\t---" : "}";
        int seatPosition = seatMarkToPosition(seatMark, width);
        boolean modified = false, appended = false, reachedMovie = false;

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(seatsStateFile));
            StringBuffer stringBuffer = new StringBuffer();
            String line;

            // goes through the entire file
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");

                // when found sections for the given movie
                if (line.equals(movie + " {") || (line.equals("\t---") && reachedMovie)) {
                    reachedMovie = true;
                    int index = 0;

                    if ((line.equals(movie + " {") && !section) || (line.equals("\t---") && section)) {
                        // goes through seats until end of the chunk
                        while (!(line = bufferedReader.readLine()).equals(endChunk)) {
                            if (!modified) {
                                // checks every char in the line in order to find the one we're looking for
                                for (int i = 0; i < line.length(); i++) {
                                    if (line.charAt(i) == '0' || line.charAt(i) == '1') {
                                        if (index == seatPosition) {
                                            System.out.println("TAKEN: " + seatMark + " " + movie);

                                            // modifies the line and appends to the string buffer
                                            char[] currentLine = line.toCharArray();
                                            currentLine[i] = '1';
                                            stringBuffer.append(new String(currentLine));
                                            stringBuffer.append("\n");
                                            reachedMovie = false;

                                            modified = true;
                                            appended = true;
                                        }

                                        index++;
                                    }
                                }
                            }

                            if (!appended) {
                                stringBuffer.append(line);
                                stringBuffer.append("\n");
                            }

                            appended = false;
                        }
                    }

                    // after analysing the important section, appends the end chunk fragment
                    if (!reachedMovie) {
                        stringBuffer.append(endChunk);
                        stringBuffer.append("\n");
                    }
                }
            }

            writeToFile(seatsStateFile, stringBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeToFile(File file, StringBuffer stringBuffer) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(stringBuffer.toString().getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int seatMarkToPosition(String seatMark, int width) {
        int row = (seatMark.charAt(0) <= 'C') ? seatMark.charAt(0) - 65 : seatMark.charAt(0) - 68;
        int column;

        if (seatMark.length() == 2)
             column = Character.getNumericValue(seatMark.charAt(1)) - 1;
        else
            column = Character.getNumericValue(seatMark.charAt(1)) * 10
                     + Character.getNumericValue(seatMark.charAt(2)) - 1;


        return row * width + column;
    }

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
                    bufferedReader.close();

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

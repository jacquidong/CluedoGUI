import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Board class for the Cluedo Board game
 *
 * @author englaneliz1
 * @author dongjacq
 */

class Board {
    int width = 25; // board width
    int height = 24; // board height
    Map<String, List<Integer>> startingPos;
    Map<String, List<Integer>> doorPositions;
    Map<String, String> roomNames;

    String[][] boardMapping;

    public Board() {
        startingPos = new HashMap<>(); // starting positions of the characters
        boardMapping = new String[width][height]; // board mapping
        fillStartingPos();
        setDoorPositions();
        roomReference();
    }

    /**
     * Sets the positions of the doors and the positions before the doors
     */
    public void setDoorPositions() {
        doorPositions = new HashMap<>();
        // col row
        doorPositions.put("7 5", Arrays.asList(8, 5));
        doorPositions.put("16 5", Arrays.asList(15, 5));
        doorPositions.put("9 8", Arrays.asList(9, 7));
        doorPositions.put("14 8", Arrays.asList(14, 7));
        doorPositions.put("18 5", Arrays.asList(18, 4));
        doorPositions.put("4 7", Arrays.asList(4, 6));
        doorPositions.put("17 9", Arrays.asList(18, 9));
        doorPositions.put("22 13", Arrays.asList(22, 12));
        doorPositions.put("6 16", Arrays.asList(6, 15));
        doorPositions.put("8 12", Arrays.asList(7, 12));
        doorPositions.put("20 13", Arrays.asList(20, 14));
        doorPositions.put("11 17", Arrays.asList(11, 18));
        doorPositions.put("12 17", Arrays.asList(12, 18));
        doorPositions.put("15 20", Arrays.asList(14, 20));
        doorPositions.put("6 18", Arrays.asList(6, 19));
        doorPositions.put("17 20", Arrays.asList(17, 21));
        doorPositions.put("16 16", Arrays.asList(17, 16));
    }

    /**
     * Creates a Hashmap of the Room starting letters and the full names of the rooms
     */
    public void roomReference() {
        roomNames = new HashMap<>();
        roomNames.put("B", "BALLROOM");
        roomNames.put("S", "STUDY");
        roomNames.put("C", "CONSERVATORY");
        roomNames.put("K", "KITCHEN");
        roomNames.put("I", "BILLIARD ROOM");
        roomNames.put("N", "DINING ROOM");
        roomNames.put("L", "LIBRARY");
        roomNames.put("H", "HALL");
        roomNames.put("O", "LOUNGE");
    }

    /**
     * Places the tile on the board
     *
     * @param col   column value on the board
     * @param row   row value on the board
     * @param value String letter to place on the board at the specified row and column coordinate
     */
    public void placeOnBoard(int row, int col, String value) {
        boardMapping[row][col] = value;
//    printBoard();
    }

    /**
     * Prints the board
     */
    public void printBoard() {
        System.out.println();

        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                System.out.print("|" + boardMapping[row][col]);
            }
            System.out.print("|");
            System.out.println();
        }
    }

    /**
     * Fills the starting initial board
     *
     * @throws IOException on input error
     */
    public void fillBoard() {

        try {
            FileReader fr = new FileReader("src/boardmap.txt");
            BufferedReader dataReader = new BufferedReader(fr);

            String currentLine;
            while ((currentLine = dataReader.readLine()) != null) {
                String[] loadArray = currentLine.split(","); // splits the lines by the comma into an array for each line

                if (loadArray.length > 2) {
                    String value = loadArray[0];
                    if (value == null) {
                        break;
                    }
                    int col = Integer.parseInt(loadArray[1]); // col position
                    int row = Integer.parseInt(loadArray[2]); // row position
                    boardMapping[row][col] = value;
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }

    }

    /**
     * Checks if the move is valid
     *
     * @return String depending on which tile the player is on
     */
    public String validMove(int newRow, int newCol, int currentCount) {
        // boundary checks
        if (newRow < 0 || newCol < 0 || newRow >= width || newCol >= height) {
            return "INVALID";
        }
        // valid path move
        else if (boardMapping[newRow][newCol].equals(" ")) {
            return "PATH";
        }
        // valid door move
        else if (boardMapping[newRow][newCol].equals("D")) {
            return "DOOR";
        }
        return "INVALID";
    }

    /**
     * Sets the starting positions of the characters on the board
     */
    public void fillStartingPos() {
        startingPos.put("PLUM", Arrays.asList(23, 19));
        startingPos.put("GREEN", Arrays.asList(14, 0));
        startingPos.put("WHITE", Arrays.asList(9, 0));
        startingPos.put("SCARLETT", Arrays.asList(7, 24));
        startingPos.put("MUSTARD", Arrays.asList(0, 17));
        startingPos.put("PEACOCK", Arrays.asList(23, 6));
    }

    /**
     * Gets the row of the character
     *
     * @param name String name of the character
     * @return int row value
     */
    public int getRow(String name) {
        return startingPos.get(name.toUpperCase()).get(1);
    }

    /**
     * Gets the col of the character
     *
     * @param name String name of the character
     * @return int col value
     */
    public int getCol(String name) {
        return startingPos.get(name.toUpperCase()).get(0);
    }
}
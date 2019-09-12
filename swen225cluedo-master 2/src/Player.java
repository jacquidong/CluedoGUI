import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import java.util.*;

/**
 * Player class for the Cluedo Board Game
 *
 * @author englaneliz1
 * @author dongjacq
 */
class Player {

    HashSet<Card> personCards;
    HashSet<Card> weaponCards;
    HashSet<Card> roomCards;

    List<Card> handList;
    List<Card> winningCards = new ArrayList<>();
    int positionOnBoard; // position of the player
    int turnRanking; // turn order of the player
    boolean out = false;
    String name; // name of player
    String character; // character
    String characterLetter;
    int row;
    int col;
    RoomObject currentRoom; // current room that the player is
    boolean inRoom;
    String previousTile;
    boolean onDoor;
    HashSet<String> pastCords;
    List<Player> allPlayers1;
    GraphicalInterface gui;

    /**
     * Creates a player in the game
     *
     * @param name         name of the player
     * @param character    character the player is playing as
     * @param row          which row they start in
     * @param col          which column they start in
     * @param personCards  all the person cards
     * @param weaponCards  all the weapon cards
     * @param roomCards    all the room cards
     * @param winningCards all the winning cards
     */
    public Player(String name, String character, int row, int col, HashSet<Card> personCards, HashSet<Card> weaponCards, HashSet<Card> roomCards, List<Card> winningCards) {
        this.handList = new ArrayList<>();
        this.name = name; // player character name
        this.character = character;
        if (character.equalsIgnoreCase("PEACOCK")) {
            this.characterLetter = "e";
        } else {
            this.characterLetter = character.substring(0, 1).toLowerCase();
        }
        this.row = row;
        this.col = col;
        this.personCards = personCards;
        this.weaponCards = weaponCards;
        this.roomCards = roomCards;
        this.winningCards = winningCards;
    }

    /**
     * Adds a card to the players hand
     *
     * @param c card to add to handlist
     */
    public void setHandList(Card c) {
        handList.add(c);
    }

    /**
     * The turn of the player
     *
     * @param sc           This is a scanner used for user input
     * @param currentboard This is the current board
     * @param player       This is the current player
     * @param players      This is the list of all of the current players
     * @param allPlayers   This is the list of all of the players - including the eliminated players
     * @return int to determine if the player is being eliminated
     */
    public int turn(Scanner sc, Board currentboard, Player player, List<Player> players, List<Player> allPlayers) {
        pastCords = new HashSet<>(); // past coords of the player
        turnRanking = allPlayers.indexOf(this);
        currentboard.boardMapping[this.row][this.col] = player.characterLetter;

        Boolean correctInput = false;
        String input;
        while (!correctInput) {
            currentboard.printBoard();
            System.out.println("\n=== " + getName().toUpperCase() + " TURN ===");
            System.out.println("ROLL or SUGGESTION or ACCUSATION");
            input = sc.next();
            if (input.equalsIgnoreCase("roll")) {
                int x = diceRollingMoving(sc, currentboard, player, players, allPlayers);
                return x;
            } else if (input.equalsIgnoreCase("suggestion")) {
                if (inRoom) { //if in a room that isn't the cellar
                    System.out.println(showHand());
                    suggestionTurn(sc, currentboard, player, players, allPlayers);
                    return 0;
                } else {
                    System.out.println("You have to be in a room to make a suggestion");
                }
            } else if (input.equalsIgnoreCase("accusation")) {
                Accusation a = accusationMade(sc);
                if (compareAccusation(a)) {
                    System.out.println(getName() + ", you win!");
                    return 1;
                } else {
                    System.out.println(getName() + ", you are out of the game!");
                    return -1;
                }
            } else {
                System.out.println("Incorrect input");
            }
        }
        return 0;
    }

    /**
     * Allows the player to move within the room
     *
     * @param sc           This is a scanner used for user input
     * @param currentboard This is the current board
     * @param player       This is the current player
     * @return Nothing is returned
     */
    public void moveWithinRoom(Scanner sc, Board currentboard, Player player) {
        Boolean correctInput = false;
        Boolean match;

        String input;

        while (!correctInput) {
            System.out.println("Do you want to move within the room? (Y/N) ");
            input = sc.next();

            if (input.equalsIgnoreCase("Y")) { // player wants to move within the room
                Boolean atDoor = false;
                while (!atDoor) {
                    int colMovement = 0;
                    int rowMovement = 0;
                    match = false;
                    correctInput = true;
                    while (match != true) { // checks for incorrect input
                        System.out.println("Enter a direction to travel (N/E/S/W): ");
                        String direction = sc.next(); // Scans the direction to travel in
                        if (direction.equalsIgnoreCase("W")) {
                            match = true;
                            colMovement = -1;
                        } else if (direction.equalsIgnoreCase("N")) {
                            match = true;
                            rowMovement = -1;
                        } else if (direction.equalsIgnoreCase("E")) {
                            match = true;
                            colMovement = 1;
                        } else if (direction.equalsIgnoreCase("S")) {
                            match = true;
                            rowMovement = 1;
                        }
                        if (!match) {
                            System.out.println("Invalid direction, please enter again");
                        }
                    }
                    if (match) {
                        if (currentboard.boardMapping[this.row + rowMovement][this.col + colMovement].equals("D")) { // reached a door
                            System.out.println("Reached a door, roll to continue");
                            atDoor = true; // at a door
                            inRoom = true; // in a room
                            match = true;
                        } else if ((!currentboard.boardMapping[this.row + rowMovement][this.col + colMovement].equals(" ") &&
                                !currentboard.boardMapping[this.row + rowMovement][this.col + colMovement].equals("X")) &&
                                !currentboard.boardMapping[this.row + rowMovement][this.col + colMovement].equals("D")) { // move is valid

                            previousTile = currentboard.boardMapping[this.row + rowMovement][this.col + colMovement];
                            moveChar(currentboard, player, currentRoom.getName().substring(0, 1), rowMovement, colMovement); // move to the tile
                            match = true;

                        } else {
                            System.out.println("Invalid move");
                        }
                    }
                }
            } else if (input.equalsIgnoreCase("N")) { // doesn't want to move within the room
                return;
            }
            if (!correctInput) {
                System.out.println("Incorrect input, please input (Y/N)");
            }
        }
    }

    /**
     * Does the dice roll and moves the current player
     *
     * @param sc           This is a scanner used for user input
     * @param currentboard This is the current board
     * @param player       This is the current player
     * @param players      This is the list of all of the current players
     * @param allPlayers   This is the list of all of the players - including the eliminated players
     * @return int to determine if the player is being eliminated
     */
    public int diceRollingMoving(Scanner sc, Board currentboard, Player player, List<Player> players, List<Player> allPlayers) {

        if (inRoom) {
            moveWithinRoom(sc, currentboard, player);
            onDoor = false;
        }
        // roll the dice
        System.out.println("Rolling dice...");
        int rollNum = this.rollDice();
        System.out.println(this.character + " rolled a " + rollNum);
        int currentCount = 0;
        while (currentCount < rollNum) { // while they still have moves left
            int colMovement = 0;
            int rowMovement = 0;
            boolean match = false;

            while (match != true) { // checks for incorrect input

                if (onDoor) { // if the player is on the door
                    Boolean matchTWO = false;
                    while (!matchTWO) {
                        System.out.println("Do you want to enter? (Y/N) ");
                        String choice = sc.next(); // Scans if they want to enter the door

                        if (choice.equalsIgnoreCase("Y")) {  // ENTERING DOOR
                            previousTile = "D";
                            onDoor = false; // not on the door anymore
                            currentboard.placeOnBoard(this.row, this.col, "D");  // put the door tile back
                            int newCol = currentboard.doorPositions.get(this.col + " " + this.row).get(0); // new row coords
                            int newRow = currentboard.doorPositions.get(this.col + " " + this.row).get(1); // new col coords
                            this.currentRoom = new RoomObject(currentboard.roomNames.get(currentboard.boardMapping[newRow][newCol])); // sets the current room the player is in
                            inRoom = true; // move them into a room
                            this.col = newCol;
                            this.row = newRow;
                            currentboard.placeOnBoard(row, col, this.characterLetter);  // place the character in the new position
                            currentCount += 1; // increment move as they moved into the room
                            currentboard.printBoard(); // print the board
                            matchTWO = true;
                            match = true;
                            return accsugChecker(sc, currentboard, player, players, allPlayers); // ask if they want to do an accusation/suggestion or nothing as theyre in the room
                        } else if (choice.equalsIgnoreCase("N")) {
                            matchTWO = true;
                            onDoor = false; //
                        }
                        if (!matchTWO) {
                            System.out.println("Not found, please re-enter (Y/N) // Not enough moves to proceed into the door");
                        }
                    }
                } else {
                    System.out.println("MOVES LEFT : " + (rollNum - currentCount) + "\nEnter a direction to travel (N/E/S/W) : "); // prints the amount of moves left
                    String direction = sc.next(); // Scans the direction to travel in
                    if (direction.equalsIgnoreCase("W")) {
                        match = true;
                        colMovement = -1;
                    } else if (direction.equalsIgnoreCase("N")) {
                        match = true;
                        rowMovement = -1;
                    } else if (direction.equalsIgnoreCase("E")) {
                        match = true;
                        colMovement = 1;
                    } else if (direction.equalsIgnoreCase("S")) {
                        match = true;
                        rowMovement = 1;
                    } else {
                        System.out.println("Invalid move, please enter again. ");
                    }
                    if (match) {
                        // move onto the path while on a door
                        if (currentboard.validMove(this.row + rowMovement, this.col + colMovement, rollNum - currentCount).equals("PATH") && !inRoom) {
                            String value = " ";
                            previousTile = " ";

                            if (currentboard.doorPositions.containsKey(this.col + " " + this.row)) { // if the current position of the tile is a door, Tile value is "D"
                                value = "D";
                                previousTile = "D";
                            }
                            if (!pastCords.contains((this.row + rowMovement) + " " + (this.col + colMovement))) { // if the player has been on this tile before
                                currentCount += 1; // increment the amount of moves
                                pastCords.add(this.row + " " + this.col);
                                moveChar(currentboard, player, value, rowMovement, colMovement);
                            } else {
                                System.out.println("You are unable to move here again");
                            }
                            if (currentboard.doorPositions.containsKey(this.col + " " + this.row)) { // if the current position of the tile is a door, Tile value is "D"
                                onDoor = true;
                                System.out.println("ON DOOR");
                            }
                        }
                        // moves into door
                        else if (currentboard.validMove(this.row + rowMovement, this.col + colMovement, rollNum - currentCount).equals("DOOR")) {
                            boolean match2 = false;
                            //EXITING THE ROOM
                            if (player.inRoom) {
                                // if they want to move out of the room
                                while (!match2) {
                                    System.out.println("Would you like to exit the room ? (Y/N) ");
                                    String choice = sc.next(); // Scans the choice
                                    if (choice.equalsIgnoreCase("Y")) { // if the player is exiting a room
                                        previousTile = "D";
                                        currentCount += 1;
                                        moveChar(currentboard, player, currentRoom.getName().substring(0, 1), rowMovement, colMovement);
                                        inRoom = false; // move them into a room
                                        currentRoom = null; // reset the current room
                                        match2 = true;
                                        match = true;
                                    } else if (choice.equalsIgnoreCase("N")) {
                                        return accsugChecker(sc, currentboard, player, players, allPlayers);
                                    }
                                    if (!match2) {
                                        System.out.println("Not found, please re-enter (Y/N).");
                                    }
                                }
                            } else if (!player.inRoom) { // wanting to enter the room

                                match2 = false;
                                while (!match2) {
                                    System.out.println("Would you like to enter into the room ? (Y/N) ");
                                    String choice = sc.next(); // Scans the choice
                                    onDoor = true;
                                    if (choice.equalsIgnoreCase("N")) {
                                        previousTile = " ";
                                        String value = " ";
                                        if (currentboard.doorPositions.containsKey(this.col + " " + this.row)) { // if the current position of the tile is a door, Tile value is "D"
                                            value = "D";
                                            System.out.println("ON DOOR");
                                        }
                                        moveChar(currentboard, player, value, rowMovement, colMovement);
                                        System.out.println("NO");
                                        return 0;
                                    }

                                    if (choice.equalsIgnoreCase("Y") && (rollNum - currentCount) >= 2) {  // ENTERING DOOR
                                        if ((rollNum - currentCount) < 2) {
                                            System.out.println("Not enough moves");
                                            inRoom = false;
                                            break;
                                        }
                                        currentCount += 1;
                                        moveChar(currentboard, player, " ", rowMovement, colMovement);
                                        currentboard.placeOnBoard(this.row, this.col, "D");  // put the door tile back
                                        int newCol = currentboard.doorPositions.get(this.col + " " + this.row).get(0); // new coords
                                        int newRow = currentboard.doorPositions.get(this.col + " " + this.row).get(1); // new coords
                                        this.currentRoom = new RoomObject(currentboard.roomNames.get(currentboard.boardMapping[newRow][newCol]));
                                        this.col = newCol;
                                        this.row = newRow;
                                        currentboard.placeOnBoard(row, col, this.characterLetter);  // place the character in the new position
                                        inRoom = true; // move them into a room
                                        currentCount += 1; // as moved again into the door
                                        currentboard.printBoard(); // print the board
                                        System.out.println("NEW COUNT: " + currentCount);
                                        match2 = true;
                                        match = true;
                                        return accsugChecker(sc, currentboard, player, players, allPlayers); // asks if they want to do a accusation, suggestion or none
                                    }

                                    if (!match2) {
                                        System.out.println("Not found, please re-enter (Y/N) // Not enough moves");
                                    }
                                }
                            }
                        }
                    } else {
                        System.out.println("INVALID MOVE");
                    }
                }
            }
        }

        return 0;
    }

    /**
     * Creates a suggestion turn
     *
     * @param sc           This is a scanner used for user input
     * @param currentboard This is the current board
     * @param player       This is the current player
     * @param players      This is the list of all of the current players
     * @param allPlayers   This is the list of all of the players - including the eliminated players
     * @return Nothing to return
     */
    public void suggestionTurn(Scanner sc, Board currentboard, Player player, List<Player> players, List<Player> allPlayers) {
        if (inRoom) { //if in a room that isn't the cellar
            System.out.println("generating suggestion.......");
            Suggestion s = suggestionMade(sc);
            // loops through all the players
            Person p = (Person) s.getPerson(); // moves person from the suggestion into room
            for (Player player1 : allPlayers) {
                // if the character is the same as the name
                if (player1.character.equalsIgnoreCase(p.name)) {
                    player1.currentRoom = this.currentRoom;
                    currentboard.placeOnBoard(player1.row, player1.col, previousTile); // replaces old place in board
                    player1.inRoom = true;
                    player1.row = this.row; // sets the new row
                    player1.col = this.col; // sets the new col
                    currentboard.placeOnBoard(player1.row, player1.col, player1.characterLetter);
                    currentboard.printBoard(); // updates the board
                }
            }

            int nextPlayer;
            int next = turnRanking + 1;
            // checks each players hand until someone shows card, or no-one has card
            for (int count = 0; count < allPlayers.size() - 1; count++) {
                if (next == allPlayers.size()) {
                    next = 0;
                }

                Player checkPlayer = allPlayers.get(next);
                if (checkPlayer.compareHand(s, sc)) { // found a card that matches
                    break;
                } else {
                    System.out.println(checkPlayer.getName() + " doesn't have any cards.");
                }
                next = next + 1;


            }
        } else {
            System.out.println("You have to be in a room to make a suggestion");
        }
    }

//    /**
//     * Creates a suggestion turn
//     *
//     * @param sc           This is a scanner used for user input
//     * @param currentboard This is the current board
//     * @param player       This is the current player
//     * @param players      This is the list of all of the current players
//     * @param allPlayers   This is the list of all of the players - including the eliminated players
//     * @return Nothing to return
//     */
    public void suggestionMadeGUI(String selectedWeapon, String selectedCharacter, String currentRoom, int playerAmount) {
        // new cards
        Card weapon = new Card(selectedWeapon);
        Card character = new Card(selectedCharacter);
        Card room = new Card(currentRoom);
        System.out.println("generating suggestion.......");

            int nextPlayer;
            int next = turnRanking + 1;
            // checks each players hand until someone shows card, or no-one has card
            for (int count = 0; count < playerAmount-1; count++) {
                if (next == playerAmount) {
                    next = 0;
                }
                Suggestion s = new Suggestion(character, weapon, room);
                Player checkPlayer = allPlayers1.get(next);
                if (checkPlayer.compareHandGUI(s)) { // found a card that matches
                    break;
                } else {
                    System.out.println(checkPlayer.getName() + " doesn't have any cards.");
                }
                next = next + 1;



             }
//            else {
//            System.out.println("You have to be in a room to make a suggestion");
//        }
    }
    /**
     * Moves the character on the board
     *
     * @param currentboard This is the current board
     * @param player       This is the current player
     * @param value        String letter of the replacement tile
     * @param colMovement  Amount to move the column value
     * @param rowMovement  Amount to move the row value
     * @return Nothing to return
     */
    public void moveChar(Board currentboard, Player player, String value, int rowMovement, int colMovement) {
        currentboard.placeOnBoard(player.row, player.col, value); // set the current position
        this.col = this.col + colMovement; // update the col
        this.row = this.row + rowMovement; // update the row
        currentboard.placeOnBoard(this.row, this.col, this.characterLetter); // set the new one to the players character
        currentboard.printBoard(); // print the board
    }

    /**
     * Computes the dice roll of two dice
     *
     * @return int This returns the dice roll
     */
    public int rollDice() {
        int roll1 = (int) Math.ceil(Math.random() * 6);
        int roll2 = (int) Math.ceil(Math.random() * 6);
        return roll1 + roll2;
    }


    /**
     * Returns a string of the players hand
     *
     * @return String This returns the current hand of the player
     */
    public String showHand() {
        String hand = (getName().toUpperCase() + "'s hand: ");
        for (Card card : handList) {
            hand += (" " + card.getName());
        }
        return hand;
    }


    /**
     * Checks if the player is in a room
     *
     * @return Boolean This returns true if the player is in a room
     */
    public boolean inRoom() {
        return true;
    }

    /**
     * Asks the player if they want to make an accusation, suggestion or none
     *
     * @param sc           This is a scanner used for user input
     * @param currentboard This is the current board
     * @param player       This is the current player
     * @param players      This is the list of all of the current players
     * @param allPlayers   This is the list of all of the players - including the eliminated players
     * @return int to determine if the player is being eliminated
     */
    public int accsugChecker(Scanner sc, Board currentboard, Player player, List<Player> players, List<Player> allPlayers) {
        Scanner reader = new Scanner(System.in);
        String choice;

        boolean m1 = false;
        while (!m1) {
            System.out.println("Would you like to make an suggestion/accusation/no?");
            choice = reader.next(); // Scans the choice
            if (choice.equalsIgnoreCase("suggestion")) {
                System.out.println(showHand());
                suggestionTurn(sc, currentboard, player, players, allPlayers);
                return 0;
            } else if (choice.equalsIgnoreCase("accusation")) {
                Accusation a = accusationMade(sc);
                if (compareAccusation(a)) {
                    System.out.println(getName() + ", you win!");
                    return 1;
                } else {
                    System.out.println(getName() + ", you are out of the game!");
                    return -1;
                }
            } else if (choice.equalsIgnoreCase("no")) {
                return 0;
            }
            if (!m1) {
                System.out.println("Invalid input.");
            }
        }
        return 000;
    }


    /**
     * Creates an accusation
     *
     * @param sc This is a scanner used for user input
     * @return Accusation This returns an accusation
     */
    public Accusation accusationMade(Scanner sc) {
        Card personG = null;
        Card weaponG = null;
        Card roomG = null;
        personG = choosePerson(sc);  // choose person
        weaponG = chooseWeapon(sc); // choose weapon
        roomG = chooseRoom(sc); // choose room
        //creates accusation
        Accusation ac = new Accusation(personG, weaponG, roomG);
        return ac;
    }

    /**
     * Creates an suggestion
     *
     * @param sc This is a scanner used for user input
     * @return Suggestion This returns an suggestion
     */
    public Suggestion suggestionMade(Scanner sc) {
        Card personG = null;
        Card weaponG = null;
        personG = choosePerson(sc); //choose person
        weaponG = chooseWeapon(sc); // choose weapon
        // room is room you are currently in, creates the suggestion
        Card roomG = null;
        for (Card p : this.roomCards) {
            Room rr = (Room) p;
            if (p.getName().equalsIgnoreCase(currentRoom.getName())) {
                roomG = rr;
            }
        }
        return new Suggestion(personG, weaponG, roomG);
    }

    /**
     * Chooses the Person Card
     *
     * @param reader This is a scanner used for user input
     * @return Card This returns a Person Card selected
     */
    public Card choosePerson(Scanner reader) {
        Card person = null;
        String personGuess = null;
        boolean realPerson = false;
        while (!realPerson) { // will loop until the player input a person card name
            System.out.println("Person:");
            personGuess = reader.next();
            for (Card p : this.personCards) {
                Person pp = (Person) p;
                if (personGuess.equalsIgnoreCase(pp.getName())) { // card exists
                    person = pp;
                    realPerson = true;
                }
            }
            if (!realPerson) {
                System.out.println("Please enter a person");
            }
        }
        return person;
    }

    /**
     * The player chooses a weapon card for a suggestion or an  accusation
     *
     * @param reader Scanner used to read user input
     * @return Card Returns the weapon card the player selects
     */
    public Card chooseWeapon(Scanner reader) {
        Card weapon = null;
        String weaponGuess = null;
        boolean realWeapon = false;
        while (!realWeapon) { // will loop till the enter a weapon card name
            System.out.println("Weapon:");
            weaponGuess = reader.next();
            for (Card w : this.weaponCards) {
                Weapon ww = (Weapon) w;
                if (weaponGuess.equalsIgnoreCase(ww.getName())) { // card exists
                    realWeapon = true;
                    weapon = ww;
                }
            }
            if (!realWeapon) {
                System.out.println("Please enter a Weapon");
            }
        }
        return weapon;
    }


    /**
     * The player can choose a room for an accusation
     *
     * @param reader Scanner to read user input
     * @return Card returns a room card selected by player
     */
    public Card chooseRoom(Scanner reader) {
        Card room = null;
        String roomGuess = null;
        boolean realRoom = false;
        while (!realRoom) { // loops until a room card name is entered
            System.out.println("Room:");
            roomGuess = reader.next();
            for (Card r : this.roomCards) {
                Room rr = (Room) r;
                if (roomGuess.equalsIgnoreCase(rr.getName())) { // card exists
                    room = rr;
                    realRoom = true;
                }
            }
            if (!realRoom) {
                System.out.println("Please enter a room");
            }
        }
        return room;
    }

    /**
     * Player checks hand to see if any of their cards match the suggestion
     *
     * @param suggestion The suggestion the player is checking to see if they have matching cards
     * @param sc         Scanner reads user input
     * @return boolean Returns true if match is found, false if player doesn't have any matching cards
     */
    public boolean compareHand(Suggestion suggestion, Scanner sc) {
        List<Card> cardsThatMatch = new ArrayList<Card>();
        Person p = (Person) suggestion.getPerson();
        Weapon w = (Weapon) suggestion.getWeapon();
        Room r = (Room) suggestion.getRoom();
        System.out.println(p.getName() + " + " + w.getName() + " + " + r.getName());
        for (Card card : handList) { // finds if the cards are in the hand

            if (p.getName().equalsIgnoreCase(card.getName())) {
                cardsThatMatch.add(card);
            } else if (w.getName().equalsIgnoreCase(card.getName())) {
                cardsThatMatch.add(card);
            } else if (r.getName().equalsIgnoreCase(card.getName())) {
                cardsThatMatch.add(card);
            }
        }

        if (cardsThatMatch.size() == 0) { // no cards match
            return false;
        } else if (cardsThatMatch.size() == 1) { // player has one matching card
            System.out.println(name + " has " + cardsThatMatch.get(0).getName());
            return true;
        } else { // more than one matching card
            String text = " choose between ";
            for (Card card : cardsThatMatch) {
                text += (card.getName() + ", ");
            }
            System.out.println(getName() + text);

            boolean i = false;

            while (!i) { // will loop until they enter a real answer
                System.out.println("Which card do you want to reveal?");
                String answer = sc.next();
                if (answer.equalsIgnoreCase(p.getName()) || answer.equalsIgnoreCase(w.getName()) || answer.equalsIgnoreCase(r.getName())) {
                    System.out.println(name + " revealed that they have " + answer);
                    return true;
                } else {
                    System.out.println("Incorrect card input");
                }
            }
        }
        return false;
    }


    /**
     * Player checks hand to see if any of their cards match the suggestion
     *
     * @param suggestion The suggestion the player is checking to see if they have matching cards
     * @return boolean Returns true if match is found, false if player doesn't have any matching cards
     */
    public boolean compareHandGUI(Suggestion suggestion) {
        List<Card> cardsThatMatch = new ArrayList<Card>();
        Person p = (Person) suggestion.getPerson();
        Weapon w = (Weapon) suggestion.getWeapon();
        Room r = (Room) suggestion.getRoom();
        System.out.println(p.getName() + " + " + w.getName() + " + " + r.getName());
        for (Card card : handList) { // finds if the cards are in the hand

            if (p.getName().equalsIgnoreCase(card.getName())) {
                cardsThatMatch.add(card);
            } else if (w.getName().equalsIgnoreCase(card.getName())) {
                cardsThatMatch.add(card);
            } else if (r.getName().equalsIgnoreCase(card.getName())) {
                cardsThatMatch.add(card);
            }
        }

        if (cardsThatMatch.size() == 0) { // no cards match
            return false;
        } else if (cardsThatMatch.size() == 1) { // player has one matching card
            System.out.println(name + " has " + cardsThatMatch.get(0).getName());
            return true;
        } else { // more than one matching card
            String text = " choose between ";
//            gui.selectCard(cardsThatMatch);

            for (Card card : cardsThatMatch) {
                text += (card.getName() + ", ");
            }
            System.out.println(getName() + text);

            boolean i = false;

//            while (!i) { // will loop until they enter a real answer
//                System.out.println("Which card do you want to reveal?");
//                String answer = sc.next();
//                if (answer.equalsIgnoreCase(p.getName()) || answer.equalsIgnoreCase(w.getName()) || answer.equalsIgnoreCase(r.getName())) {
//                    System.out.println(name + " revealed that they have " + answer);
//                    return true;
//                } else {
//                    System.out.println("Incorrect card input");
//                }
//            }
        }
        return false;
    }

    /**
     * Compares an accusation with the winning cards
     *
     * @param accusation accusation the player wants to check against the winning cards
     * @return boolean returns true if all cards match the winning cards
     */
    public boolean compareAccusation(Accusation accusation) {
        if (!winningCards.get(1).getName().equalsIgnoreCase(accusation.getPerson().getName())) {
            return false;
        }
        if (!winningCards.get(2).getName().equalsIgnoreCase(accusation.getWeapon().getName())) {
            return false;
        }
        if (!winningCards.get(0).getName().equalsIgnoreCase(accusation.getRoom().getName())) {
            return false;
        }
        return true; // all cards match
    }

    /**
     * Returns the name of the player
     *
     * @return String returns the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the name of the character
     *
     * @return String returns the name of the character
     */
    public String getCharacter() {
        return character;
    }
    
    public boolean getInRoom(){return inRoom;}
    public void setInRoom(boolean t){inRoom=t;}
    

}
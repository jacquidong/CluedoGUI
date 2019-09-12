import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Main class for the Cluedo Board game
 *
 * @author englaneliz1
 * @author dongjacq
 */

class cluedoMain implements ActionListener {
    // full of all of the cards
    public HashSet<Card> personCards = new HashSet<>();
    public HashSet<Card> weaponCards = new HashSet<>();
    public HashSet<Card> roomCards = new HashSet<>();

    HashSet<Card> cards; //
    List<Card> winningCards;
    int noOfPlayers;

    // arraylists of players and characters
    ArrayList<Player> players;
    ArrayList<Player> allPlayers = new ArrayList<>();

    List<String> characters = new ArrayList<>();

    // altered lists
    List<Card> roomz = new ArrayList<>();
    List<Card> weaponz = new ArrayList<>();
    List<Card> peoplez = new ArrayList<>();
    List<Card> cardsToDeal = new ArrayList<>();
    boolean gameOver = false;
    Board currentboard;
    GraphicalInterface gui;

    public cluedoMain() {
        this.noOfPlayers = 0;
        players = new ArrayList<Player>();
        this.currentboard = new Board();
        generateCards();
        chooseWinning();
        addCharactersToList();
        gui = new GraphicalInterface(this);
        //newGame();
        newGame();
    }

    public static void main(String[] args) {
        cluedoMain m = new cluedoMain();

    }

    /**
     * Creates a new game
     */
    public void newGame() {
        Scanner sc = new Scanner(System.in);
        currentboard.fillBoard();
        shuffle();
        deal();
        for (String c : characters) { //place all of the left over characters on the board
            Player playernotUsed = new Player(null, c, currentboard.getRow(c), currentboard.getCol(c), personCards, weaponCards, roomCards, winningCards);
            System.out.println("adding " + playernotUsed.character);
            String charLetter = c.substring(0, 1).toLowerCase();
            if (c.equalsIgnoreCase("PEACOCK")) { // so peacock doesn't use p, because plum uses p
                charLetter = "e";
            }
            allPlayers.add(playernotUsed);
        }

        Player player1 = players.get(0);
        gui.createMainFrame(player1);

/*
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);

            gui.playerTurn(player,true);
            //System.out.println("YAYYAY");
            /*int no = player.turn(sc, currentboard, player, players, allPlayers);
            if (no == 1) {
                gameOver = true;
                i = players.size();
            } // win
            if (no != -1) {
                temp.add(players.get(i));
            }
            */
    }

    //playGame(sc);

    // addPlayers(sc);
    //autoPlayers();
    // shuffle();
    //deal();
    //playGame(sc);
    //sc.close();


    /**
     * Starts a new game
     *
     * @param reader scanner to read user input
     */
    public void playGame(Scanner reader){

        for (Player player : players) {
            System.out.println("row:" + player.row + " col:" + player.col + " letter: " + player.characterLetter);
            currentboard.placeOnBoard(player.row, player.col, player.characterLetter);
            // add the player to all of the players list
            allPlayers.add(player);
            characters.remove(player.character);
        }
        for (String c : characters) { //place all of the left over characters on the board
            Player playernotUsed = new Player(null, c, currentboard.getRow(c), currentboard.getCol(c), personCards, weaponCards, roomCards, winningCards);
            System.out.println("adding " + playernotUsed.character);
            String charLetter = c.substring(0, 1).toLowerCase();
            if (c.equalsIgnoreCase("PEACOCK")) { // so peacock doesn't use p, because plum uses p
                charLetter = "e";
            }
            currentboard.placeOnBoard(currentboard.startingPos.get(c).get(1), currentboard.startingPos.get(c).get(0), charLetter);
            // add the player to all of the players list
            allPlayers.add(playernotUsed);
        }
        // loops until someone wins
        while (!gameOver) {
            ArrayList<Player> temp = new ArrayList<Player>();
            currentboard.printBoard();
            // for all of the players
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);

                int no = player.turn(reader, currentboard, player, players, allPlayers);
                if (no == 1) {
                    gameOver = true;
                    i = players.size();
                } // win
                if (no != -1) {
                    temp.add(players.get(i));
                }
            }
            players = temp;
            if (players.size() == 1) { // if only one player left,  playing they win
                System.out.println(players.get(0).getName() + " wins the game!");
                gameOver = true;
            }
        }
    }

    /**
     * adds all characters to characters list
     */
    public void addCharactersToList () {
        // adding the characters to the list
        characters.add("SCARLETT");
        characters.add("PLUM");
        characters.add("GREEN");
        characters.add("WHITE");
        characters.add("PEACOCK");
        characters.add("MUSTARD");
    }

    /**
     * auto generates players
     */
    public void autoPlayers () {
        Player player1 = new Player("Jerry", "PLUM", currentboard.getRow("PLUM"), currentboard.getCol("PLUM"), personCards, weaponCards, roomCards, winningCards);
        players.add(player1);
        Player player2 = new Player("Leo", "WHITE", currentboard.getRow("WHITE"), currentboard.getCol("WHITE"), personCards, weaponCards, roomCards, winningCards);
        players.add(player2);
        Player player3 = new Player("Boo", "GREEN", currentboard.getRow("GREEN"), currentboard.getCol("GREEN"), personCards, weaponCards, roomCards, winningCards);
        players.add(player3);
        Player player4 = new Player("Ade", "SCARLETT", currentboard.getRow("SCARLETT"), currentboard.getCol("SCARLETT"), personCards, weaponCards, roomCards, winningCards);
        players.add(player4);
        Player player5 = new Player("f", "MUSTARD", currentboard.getRow("MUSTARD"), currentboard.getCol("MUSTARD"), personCards, weaponCards, roomCards, winningCards);
        players.add(player5);
        Player player6 = new Player("fFFFF", "PEACOCK", currentboard.getRow("PEACOCK"), currentboard.getCol("PEACOCK"), personCards, weaponCards, roomCards, winningCards);
        players.add(player6);
        noOfPlayers = players.size();
        allPlayers.add(player1);
        allPlayers.add(player2);
        allPlayers.add(player3);
        allPlayers.add(player4);
        allPlayers.add(player5);
        allPlayers.add(player6);


    }

    // edited
    /**
     * adds players to the game from the GUI panel
     *
     //     * @param reader Scanner reads user input
     */
    public void addPlayerGUI (String name, String character,int row, int col, HashSet<
            Card > personCards1, HashSet < Card > weaponCards1, HashSet < Card > roomCards1, List < Card > winningCards1){

        Player p = new Player(name, character, row, col, personCards, weaponCards, roomCards, winningCards);

        boolean match = false;
        int index = -1;
        while (match != true) { // checks for incorrect input
            for (String s : characters) { // checks to see if input is one of characters
                if (s.equalsIgnoreCase(character)) { // match found
                    System.out.println(name + " is " + s);
                    index = characters.indexOf(s);
                    match = true;
                }
            }
        }
        characters.remove(index); // removes character so that no other player can be character

        players.add(p);
        allPlayers.add(p);
        System.out.println("Added2: " + character);
//        System.out.println("Players: " + players);
    }


    /**
     * adds players to the game
     *
     * @param reader Scanner reads user input
     */
    public void addPlayers (Scanner reader){
        while (noOfPlayers < 3 || noOfPlayers > 6) {
            System.out.println("Enter a number of players (between 3-6): ");
            try { // checks for incorrect input
                this.noOfPlayers = Integer.parseInt(reader.next()); // Scans the number of players entered
            } catch (NumberFormatException e) {
            }

        }
        System.out.println("\n=== ADDING PLAYERS ===");
        for (int i = 0; i < this.noOfPlayers; i++) { //adds players
            int index = -1;
            boolean match = false;
            System.out.println("\n=== PLAYER " + (i + 1) + " ===");
            System.out.println("Enter your name: ");
            String name = reader.next();

            String text = null;
            while (match != true) { // checks for incorrect input
                System.out.println(characters);
                System.out.println("Choose your character: ");
                text = reader.next();
                for (String s : characters) { // checks to see if input is one of characters
                    if (s.equalsIgnoreCase(text)) { // match found
                        System.out.println(name + " is " + s);
                        index = characters.indexOf(s);
                        match = true;
                    }
                }
                if (!match) {
                    System.out.println("Not found, please re-enter.");
                }
            }
            characters.remove(index); // removes character so that no other player can be character

            Player newPlayer = new Player(name, text, currentboard.getRow(text), currentboard.getCol(text), personCards, weaponCards, roomCards, winningCards);
            players.add(newPlayer); // adds player to list of players
            allPlayers.add(newPlayer);
            System.out.println("Character completed");
        }

    }

    /**
     * Chooses the winning cards
     */
    public void chooseWinning () {

        this.winningCards = new ArrayList<>();

        // shuffle each of the lists of cards up
        Collections.shuffle(roomz);
        Collections.shuffle(peoplez);
        Collections.shuffle(weaponz);

        // adding the random cards to the winningCards list from the index
        winningCards.add(roomz.get(0));
        winningCards.add(peoplez.get(0));
        winningCards.add(weaponz.get(0));

        // removing the randomly selected cards from the specific lists
        peoplez.remove(0);
        roomz.remove(0);
        weaponz.remove(0);
        System.out.println(winningCards.toString());

    }

    /**
     * Shuffle and put all the cards into the deck
     */
    public void shuffle() {
        // inititalize the cards to deal list
        cardsToDeal = new ArrayList<>();

        // add all of the cards to the cards to deal list
        for (Card c : roomz) {
            cardsToDeal.add(c);
        }
        for (Card c : weaponz) {
            cardsToDeal.add(c);
        }
        for (Card c : peoplez) {
            cardsToDeal.add(c);
        }
        // shuffled the cards
        Collections.shuffle(cardsToDeal);
    }

    /**
     * Deals the cards to the players evenly
     */
    public void deal () {
        int play = 0;
        for (int i = 0; i < cardsToDeal.size(); i++) {
            players.get(play).setHandList(cardsToDeal.get(i));
            if (play == players.size() - 1) {
                play = 0;
            } else {
                play++;
            }
        }
        for (Player playa : allPlayers) { // prints each players hand
            System.out.println(playa.name + " : " + playa.handList);
        }
    }

    /**
     * Generates the cards
     */
    public void generateCards () {

        List<String> names = Arrays.asList("SCARLETT", "PLUM", "GREEN", "MUSTARD", "WHITE", "PEACOCK");
        for (int i = 0; i < names.size(); i++) {
            Person p = new Person(names.get(i));
            peoplez.add(p);
            personCards.add(p);
        }
        List<String> weapons = Arrays.asList("DAGGER", "CANDLESTICK", "PIPE", "REVOLVER", "ROPE", "SPANNER");
        for (int i = 0; i < weapons.size(); i++) {
            Weapon w = new Weapon(weapons.get(i));
            weaponz.add(w);
            weaponCards.add(w);

        }
        List<String> rooms = Arrays.asList("KITCHEN", "DINING", "LOUNGE", "HALL", "STUDY", "LIBRARY", "BILLIARD", "CONSERVATORY", "BALLROOM");
        for (int i = 0; i < rooms.size(); i++) {
            Room r = new Room(rooms.get(i));
            roomz.add(r);
            roomCards.add(r);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == gui.roll){
            gui.roll();
            gui.updateMainFrame();
            gui.canMove = true;
        }

        if(e.getSource() == gui.suggestion){
            gui.makeSuggestion();
            gui.turnOver =true;

        }

        if(e.getSource() == gui.accusation){
            gui.makeAccusation();
            gui.turnOver = true ;
        }

        if(e.getSource() == gui.confirmSuggestion){
            int count=0;

            Enumeration characterEle =gui.characterGroup.getElements();
            Enumeration weaponEle = gui.weaponGroup.getElements();
            while(characterEle.hasMoreElements()){
                JRadioButton button = (JRadioButton)characterEle.nextElement();
                if (button.isSelected()){
                    count++;
                    System.out.println("selected: " + button.getText());

                }
            }

            while(weaponEle.hasMoreElements()){
                JRadioButton button = (JRadioButton)weaponEle.nextElement();
                if (button.isSelected()){
                    count++;
                    System.out.println("selected: " + button.getText());

                }
            }

            if(count==2) {
                gui.suggestionFrame.setVisible(false);
            }

        }

        if(e.getSource() == gui.confirmAccusation){
            int count=0;
            System.out.println("Here we compare hands");
            Enumeration characterEle = gui.characterGroup.getElements();
            Enumeration weaponEle = gui.weaponGroup.getElements();
            Enumeration roomEle = gui.roomGroup.getElements();
            while(characterEle.hasMoreElements()){
                JRadioButton button = (JRadioButton)characterEle.nextElement();
                if (button.isSelected()){
                    count++;
                    System.out.println("selected: " + button.getText());

                }
            }

            while(weaponEle.hasMoreElements()){
                JRadioButton button = (JRadioButton)weaponEle.nextElement();
                if (button.isSelected()){
                    count++;
                    System.out.println("selected: " + button.getText());

                }
            }

            while(roomEle.hasMoreElements()){
                JRadioButton button = (JRadioButton)roomEle.nextElement();
                if (button.isSelected()){
                    count++;
                    System.out.println("selected: " + button.getText());

                }
            }

            if(count==3) {
                gui.accusationFrame.setVisible(false);
            }

        }


    }
}


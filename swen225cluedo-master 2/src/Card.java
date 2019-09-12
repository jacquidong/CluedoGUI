/**
 * Card class
 * @author englaneliz
 * @author dongjacq
 */
class Card {
    String name;

    /**
     * Card constructor
     * @param name
     */
    public Card(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the card
     * @return String of the name
     */
    public String getName() {
        return name;
    }
}
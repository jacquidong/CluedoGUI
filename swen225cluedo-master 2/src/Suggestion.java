/**
 * Suggestion class for Cluedo
 *
 * @author englaneliz
 * @author dongjacq
 */
class Suggestion {
    Card person;
    Card weapon;
    Card room;

    public Suggestion(Card person, Card weapon, Card room) {
        this.person = person;
        this.weapon = weapon;
        this.room = room;
    }

    /**
     * Formatted string of the suggestion
     *
     * @return String suggestion
     */
    public String toString() {
        String suggestion = (person.toString() + " in the " + room.toString() + " with the " + weapon.toString());
        return suggestion;
    }

    /**
     * Gets the Suggestion person
     *
     * @return Suggestion person
     */
    public Card getPerson() {
        return person;
    }

    /**
     * Gets the Suggestion weapon
     *
     * @return Suggestion weapon
     */
    public Card getWeapon() {
        return weapon;
    }

    /**
     * Gets the Suggestion room
     *
     * @return Suggestion room
     */
    public Card getRoom() {
        return room;
    }
}
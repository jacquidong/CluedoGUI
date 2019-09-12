/**
 * Accusation class for Cluedo
 *
 * @author englaneliz
 * @author dongjacq
 */
class Accusation {
    Card person;
    Card weapon;
    Card room;

    public Accusation(Card person, Card weapon, Card room) {
        this.person = person;
        this.weapon = weapon;
        this.room = room;

    }

    /**
     * Gets the Accusation name
     *
     * @return Accusation name
     */
    public Card getPerson() {
        return person;
    }

    /**
     * Gets the Accusation weapon
     *
     * @return Accusation weapon
     */
    public Card getWeapon() {
        return weapon;
    }

    /**
     * Gets the Accusation room
     *
     * @return Accusation room
     */
    public Card getRoom() {
        return room;
    }

}
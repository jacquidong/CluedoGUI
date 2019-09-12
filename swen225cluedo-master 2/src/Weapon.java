/**
 * Weapon class for Cluedo
 *
 * @author englaneliz
 * @author dongjacq
 */
class Weapon extends Card {

    public Weapon(String name) {
        super(name);
    }

    /**
     * Return the name of the Weapon Card
     *
     * @return String name of the Weapon Card
     */
    @Override
    public String toString() {
        return "Weapon: " + name;
    }
}
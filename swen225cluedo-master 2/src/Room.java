/**
 * Room class for Cluedo
 *
 * @author englaneliz
 * @author dongjacq
 */
class Room extends Card {

    public Room(String name) {
        super(name);
    }

    /**
     * Return the name of the Room Card
     *
     * @return String name of the Room Card
     */
    @Override
    public String toString() {
        return "Room: " + name;
    }

    @Override
    public String getName() {
        return super.getName();
    }
}
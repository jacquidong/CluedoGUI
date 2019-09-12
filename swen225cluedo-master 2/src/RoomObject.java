/**
 * Room Object class for Cluedo
 *
 * @author englaneliz
 * @author dongjacq
 */
class RoomObject {
    String name;

    public RoomObject(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the current Room Object
     *
     * @return String name of the current room object
     */
    public String getName() {
        return name;
    }
}
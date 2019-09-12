/**
 * Person class for Cluedo
 *
 * @author englaneliz
 * @author dongjacq
 */
class Person extends Card {

    public Person(String name) {
        super(name);
    }

    /**
     * Returns the name of the person Card
     *
     * @return String name of the person
     */
    @Override
    public String toString() {
        return "Person: " + name;
    }

}
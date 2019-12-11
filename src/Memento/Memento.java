/**
 * Memento, A snapshot of the address taken from the originator
 */

package Memento;


public class Memento {
    private String state;

    /**
     * Constructor for this class
     *
     * @param s
     */
    public Memento(String s) {
        state = s;
    }

    /**
     * Getter for state
     *
     * @return state
     */
    public String getState() {
        return state;
    }
}

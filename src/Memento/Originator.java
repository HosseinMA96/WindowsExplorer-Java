/**
 * This class is our program at one instance, which is consisted of model, view and control
 * As this class varies, snapshots of it are saved in mementos
 */
package Memento;

import Controller.Controller;
import Model.Model;
import View.View;


public class Originator {
    private Model model;
    private View view;
    private String state;
    private Controller controller;

    /**
     * Constructor for this class
     *
     * @param model
     * @param view
     * @param controller
     * @param state
     */
    public Originator(Model model, View view, Controller controller, String state) {
        this.model = model;
        this.view = view;
        this.controller = controller;
        this.state = state;
    }

    /**
     * A function to restore (go back or forward) our originator to a memento
     *
     * @param memento
     */
    public void restore(Memento memento) {
        state = memento.getState();
        model.setCurrentAddressWithoutCareTakerModification(state);
        controller.upgradeView();
    }

}

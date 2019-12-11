/**
 * This class holds an instance of originator, and controls the process of the flow of the originator back or forth
 */

package Memento;

import Controller.Controller;
import Model.Model;
import View.View;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CareTaker {
    private ArrayList<Memento> mementos;
    private Originator originator;
    private int currentIndex, numberOfFlashBacks;

    /**
     * Cunstructor for this class
     *
     * @param m
     * @param v
     * @param c
     * @param initialAddress
     * @param allowedFlashBacks
     */
    public CareTaker(Model m, View v, Controller c, String initialAddress, int allowedFlashBacks) {
        mementos = new ArrayList<>();
        mementos.add(new Memento(initialAddress));
        originator = new Originator(m, v, c, initialAddress);
        currentIndex = 0;
        numberOfFlashBacks = allowedFlashBacks + 1;

        v.getLeftArrow().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                goPrev();
            }
        });

        v.getRightArrow().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goNext();
            }
        });
    }

    /**
     * Setter for numberOfFlashBacks
     *
     * @param numberOfFlashBacks
     */
    public void setNumberOfFlashBacks(int numberOfFlashBacks) {
        this.numberOfFlashBacks = numberOfFlashBacks;

        while (mementos != null && mementos.size() > numberOfFlashBacks)
            mementos.remove(0);

    }

    /**
     * A Method to switch to previous state
     */
    public void goPrev() {
        if (currentIndex != 0) {
            currentIndex--;
            originator.restore(mementos.get(currentIndex));
        }
    }

    /**
     * A Method to switch to next state
     */
    public void goNext() {
        if (currentIndex + 1 < mementos.size()) {
            currentIndex++;
            originator.restore(mementos.get(currentIndex));
        }
    }

    /**
     * A method to open an address
     *
     * @param address
     */
    public void openNewAddress(String address) {
        if (mementos == null || mementos.size() == 0) {
            mementos.add(new Memento(address));
            currentIndex++;
            return;
        }

        if (mementos.get(mementos.size() - 1).getState().equals(address))
            return;

        while (currentIndex + 1 < mementos.size())
            mementos.remove(mementos.size() - 1);

        mementos.add(new Memento(address));
        currentIndex++;

        if (mementos.size() > numberOfFlashBacks) {
            mementos.remove(0);
            currentIndex--;
        }


    }


}

package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.exceptions.EmptyBagException;

public class NumberBag {

    private List<Integer> pendingNumbers;
    private List<Integer> pickedNumbers;
    private int numberAmount;
    private Random random;
    private List<NumberBagListener> numberBagListeners;

    public NumberBag(int numberAmount) {
        pendingNumbers = new ArrayList<>();
        pickedNumbers = new ArrayList<>();
        this.numberAmount = numberAmount;
        random = new Random();
        numberBagListeners = new ArrayList<>();
        reset();
    }

    public void reset() {
        pendingNumbers.clear();
        getPickedNumbers().clear();
        for (int i = 0; i < getNumberAmount(); i++) {
            pendingNumbers.add(i + 1);
        }
        notifyReset();
    }

    public int pickNumber() throws EmptyBagException {
        try {
            int position = random.nextInt(pendingNumbers.size());
            int number = pendingNumbers.remove(position);
            getPickedNumbers().add(number);
            notifyNumberPicked(number);
            if (pendingNumbers.isEmpty()) {
                notifyPickedLastNumber();
            }
            return number;
        } catch (IllegalArgumentException e) {
            throw new EmptyBagException();
        }
    }

    public void addNumberBagListener(NumberBagListener numberBagListener) {
        numberBagListeners.add(numberBagListener);
    }

    public void removeNumberBagListener(NumberBagListener numberBagListener) {
        numberBagListeners.remove(numberBagListener);
    }

    private void notifyReset() {
        for (NumberBagListener numberBagListener : numberBagListeners) {
            numberBagListener.reset();
        }
    }

    private void notifyNumberPicked(int number) {
        for (NumberBagListener numberBagListener : numberBagListeners) {
            numberBagListener.numberPicked(number);
        }
    }

    private void notifyPickedLastNumber() {
        for (NumberBagListener numberBagListener : numberBagListeners) {
            numberBagListener.pickedLastNumber();
        }
    }

    public int getNumberAmount() {
        return numberAmount;
    }

    public void setNumberAmount(int numberAmount) {
        this.numberAmount = numberAmount;
    }

    public List<Integer> getPickedNumbers() {
        return pickedNumbers;
    }

    public boolean hasMorePendingNumbers() {
        return !pendingNumbers.isEmpty();
    }

    public boolean isBagFull() {
        return pickedNumbers.isEmpty();
    }
}

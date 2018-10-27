package model;

public class Bingo {

    private NumberBag numberBag;
    private SettingsManager settingsManager;

    public Bingo(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
        numberBag = new NumberBag(settingsManager.getAmountOfNumbers());
    }

    public NumberBag getNumberBag() {
        return numberBag;
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    public void reset() {
        numberBag.setNumberAmount(settingsManager.getAmountOfNumbers());
        numberBag.reset();
    }

    public boolean getGameStarted() {
        return !numberBag.isBagFull();
    }
}

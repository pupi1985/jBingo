package controller;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import model.Bingo;
import model.NumberBagListener;
import model.exceptions.EmptyBagException;
import view.Messages;
import view.shells.MainView;

public class MainViewController implements NumberBagListener {

    private Bingo bingo;
    private MainView mainView;
    private Display display;

    private HashMap<Integer, String> numberNames;

    public MainViewController(Bingo bingo, Display display) {
        this.bingo = bingo;
        this.display = display;
        this.numberNames = bingo.getSettingsManager().getNumberNames();

        bingo.getNumberBag().addNumberBagListener(this);

        mainView = new MainView(bingo, display, this);
        mainView.open();
    }

    @Override
    public void numberPicked(int number) {
        mainView.pickNumber(number, numberNames.get(number));
    }

    @Override
    public void reset() {
        mainView.reset();
    }

    @Override
    public void pickedLastNumber() {
        mainView.pickedLastNumber();
    }

    public void pickNumberButtonAction() {
        try {
            bingo.getNumberBag().pickNumber();
        } catch (EmptyBagException e) {
        }
    }

    public void resetButtonAction() {
        MessageBox messageBox = new MessageBox(mainView.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
        messageBox.setText(Messages.getString("MainView.ResetConfirmationTitle")); //$NON-NLS-1$
        messageBox.setMessage(Messages.getString("MainView.ResetConfirmationText")); //$NON-NLS-1$
        int answer = messageBox.open();
        if (answer == SWT.YES) {
            bingo.getNumberBag().reset();
        }
    }

    public void preferencesButtonAction() {
        new PreferencesController(bingo, display, mainView);
    }

    public void historyButtonAction() {
        new HistoryController(bingo, display, mainView);
    }

    public void exitButtonAction() {
        mainView.getDisplay().dispose();
    }
}

package controller;

import org.eclipse.swt.widgets.Display;

import model.Bingo;
import view.shells.HistoryView;
import view.shells.MainView;

public class HistoryController {

    private HistoryView historyView;

    public HistoryController(Bingo bingo, Display display, MainView mainView) {
        historyView = new HistoryView(bingo, this, display, mainView.getShell());
        historyView.open();
    }

    public void okButtonAction() {
        historyView.getShell().dispose();
    }
}

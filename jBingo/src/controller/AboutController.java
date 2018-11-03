package controller;

import org.eclipse.swt.widgets.Display;

import view.shells.AboutView;
import view.shells.MainView;

public class AboutController {

    private AboutView aboutView;

    public AboutController(Display display, MainView mainView) {
        aboutView = new AboutView(this, display, mainView.getShell());
        aboutView.open();
    }

    public void okButtonAction() {
        aboutView.getShell().dispose();
    }
}

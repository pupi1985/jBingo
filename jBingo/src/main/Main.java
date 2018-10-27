package main;

import org.eclipse.swt.widgets.Display;

import controller.MainViewController;
import model.Bingo;
import model.SettingsManager;

public class Main {

	public static void main(String[] args) {
		try {
			Display display = Display.getDefault();
			SettingsManager settingsManager = new SettingsManager();
			settingsManager.loadFromFile(display);
			Bingo bingo = new Bingo(settingsManager);
			new MainViewController(bingo, display);
			settingsManager.saveToFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

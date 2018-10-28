package model.exceptions;

import view.Messages;

public class InvalidNumberNamesFontDataException extends Exception {

    public InvalidNumberNamesFontDataException() {
        super(Messages.getString("PreferencesView.ErrorInvalidNumberNamesFontData")); //$NON-NLS-1$
    }
}

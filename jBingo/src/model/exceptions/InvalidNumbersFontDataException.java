package model.exceptions;

import view.Messages;

public class InvalidNumbersFontDataException extends Exception {

    public InvalidNumbersFontDataException() {
        super(Messages.getString("PreferencesView.ErrorInvalidNumbersFontData")); //$NON-NLS-1$
    }
}

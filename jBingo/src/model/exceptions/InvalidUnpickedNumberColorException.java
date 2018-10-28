package model.exceptions;

import view.Messages;

public class InvalidUnpickedNumberColorException extends Exception {

    public InvalidUnpickedNumberColorException() {
        super(Messages.getString("PreferencesView.ErrorInvalidUnpickedNumberColor")); //$NON-NLS-1$
    }
}

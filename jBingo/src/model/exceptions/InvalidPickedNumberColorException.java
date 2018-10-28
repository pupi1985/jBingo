package model.exceptions;

import view.Messages;

public class InvalidPickedNumberColorException extends Exception {

    public InvalidPickedNumberColorException() {
        super(Messages.getString("PreferencesView.ErrorInvalidPickedNumberColor")); //$NON-NLS-1$
    }
}

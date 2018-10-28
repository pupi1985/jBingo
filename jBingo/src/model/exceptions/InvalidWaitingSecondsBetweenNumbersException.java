package model.exceptions;

import view.Messages;

public class InvalidWaitingSecondsBetweenNumbersException extends Exception {

    public InvalidWaitingSecondsBetweenNumbersException() {
        super(Messages.getString("PreferencesView.ErrorInvalidWaitingSecondsBetweenNumbers")); //$NON-NLS-1$
    }
}

package model.exceptions;

import view.Messages;

public class InvalidHistoryLengthException extends Exception {

    public InvalidHistoryLengthException() {
        super(Messages.getString("PreferencesView.ErrorInvalidHistoryLength")); //$NON-NLS-1$
    }
}

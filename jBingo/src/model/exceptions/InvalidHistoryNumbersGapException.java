package model.exceptions;

import view.Messages;

public class InvalidHistoryNumbersGapException extends Exception {

    public InvalidHistoryNumbersGapException() {
        super(Messages.getString("PreferencesView.ErrorInvalidHistoryNumbersGap")); //$NON-NLS-1$
    }
}

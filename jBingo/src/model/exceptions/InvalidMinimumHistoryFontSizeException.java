package model.exceptions;

import view.Messages;

public class InvalidMinimumHistoryFontSizeException extends Exception {

    public InvalidMinimumHistoryFontSizeException() {
        super(Messages.getString("PreferencesView.ErrorInvalidMinimumHistoryFontSize")); //$NON-NLS-1$
    }
}

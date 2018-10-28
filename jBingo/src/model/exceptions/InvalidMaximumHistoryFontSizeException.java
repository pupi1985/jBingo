package model.exceptions;

import view.Messages;

public class InvalidMaximumHistoryFontSizeException extends Exception {

    public InvalidMaximumHistoryFontSizeException() {
        super(Messages.getString("PreferencesView.ErrorInvalidMaximumHistoryFontSize")); //$NON-NLS-1$
    }
}

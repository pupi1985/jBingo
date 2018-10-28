package model.exceptions;

import view.Messages;

public class InvalidHistoryFontSizesException extends Exception {

    public InvalidHistoryFontSizesException() {
        super(Messages.getString("PreferencesView.ErrorInvalidHistoryFontSizes")); //$NON-NLS-1$
    }
}

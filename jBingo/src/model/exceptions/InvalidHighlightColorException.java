package model.exceptions;

import view.Messages;

public class InvalidHighlightColorException extends Exception {

    public InvalidHighlightColorException() {
        super(Messages.getString("PreferencesView.ErrorInvalidHighlightColor")); //$NON-NLS-1$
    }
}

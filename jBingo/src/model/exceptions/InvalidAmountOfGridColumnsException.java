package model.exceptions;

import view.Messages;

public class InvalidAmountOfGridColumnsException extends Exception {

    public InvalidAmountOfGridColumnsException() {
        super(Messages.getString("PreferencesView.ErrorInvalidAmountOfGridColumns")); //$NON-NLS-1$
    }
}

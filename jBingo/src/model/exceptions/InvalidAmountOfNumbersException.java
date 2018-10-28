package model.exceptions;

import view.Messages;

public class InvalidAmountOfNumbersException extends Exception {

    public InvalidAmountOfNumbersException() {
        super(Messages.getString("PreferencesView.ErrorInvalidAmountOfNumbers")); //$NON-NLS-1$
    }
}

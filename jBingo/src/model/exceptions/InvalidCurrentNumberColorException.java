package model.exceptions;

import view.Messages;

public class InvalidCurrentNumberColorException extends Exception {

    public InvalidCurrentNumberColorException() {
        super(Messages.getString("PreferencesView.ErrorInvalidCurrentNumberColor")); //$NON-NLS-1$
    }
}

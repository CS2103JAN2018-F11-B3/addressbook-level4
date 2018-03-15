package seedu.address.commons.exceptions;

public abstract class DataNotFoundException extends IllegalValueException {
    public DataNotFoundException(String message) {
        super(message);
    }
}

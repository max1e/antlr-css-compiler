package nl.han.ica.icss.transforms.exceptions;

public class VariableNotDefinedException extends RuntimeException {

    public VariableNotDefinedException(String message) {
        super("Variable reference: '".concat(message).concat("' does not exist."));
    }
}

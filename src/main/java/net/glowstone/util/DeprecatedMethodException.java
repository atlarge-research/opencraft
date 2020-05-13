package net.glowstone.util;

public class DeprecatedMethodException extends UnsupportedOperationException{

    /**
     * Constructs an DeprecatedMethodException with no detail message.
     */
    public DeprecatedMethodException() {
    }

    /**
     * Constructs an DeprecatedMethodException with the specified detail message.
     *
     * @param message the detail message
     */
    public DeprecatedMethodException(String message) {
        super(message);
    }
}

package bms.exceptions;

/**
 * Exception thrown when a sensor is added to a room that already contains a
 * sensor of the same type.
 */
public class DuplicateSensorException extends Exception {

    /**
     * Constructs a normal DuplicateSensorException with no error message
     * or cause.
     */
    public DuplicateSensorException() {
    }
}

package bms.exceptions;

/**
 * Exception thrown when a room is added to a floor, but the room number is
 * already taken on that floor.
 */
public class DuplicateRoomException extends Exception {

    /**
     * Constructs a normal DuplicateRoomException with no error message
     * or cause.
     */
    public DuplicateRoomException() { }
}

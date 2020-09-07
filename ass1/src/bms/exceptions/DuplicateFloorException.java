package bms.exceptions;

/**
 * Exception thrown when a floor is added to a building that already contains
 * a floor on that level.
 */
public class DuplicateFloorException extends Exception{

    /**
     * Constructs a normal DuplicateFloorException with no error message
     * or cause.
     */
    public DuplicateFloorException(){}
}

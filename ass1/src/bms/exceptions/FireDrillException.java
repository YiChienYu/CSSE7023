package bms.exceptions;

/**
 * Exception thrown when trying to call a fire drill on a building with
 * no rooms in it on any floors.
 */
public class FireDrillException extends Exception{

    /**
     * Constructs a normal FireDrillException with no error message or cause.
     */
    public FireDrillException(){
    }
}

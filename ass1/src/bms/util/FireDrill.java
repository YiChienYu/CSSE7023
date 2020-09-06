package bms.util;

import bms.room.RoomType;
import bms.exceptions.FireDrillException;

/**
 * Denotes a class containing a routine to carry out fire drills on rooms of a
 * given type.
 */
public interface FireDrill {

    /**
     * Set firedrill to given roomtype, null for all type.
     * @param roomType
     * @throws FireDrillException
     */
    void fireDrill(RoomType roomType) throws FireDrillException;
}
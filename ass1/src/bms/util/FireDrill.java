package bms.util;
import bms.room.RoomType;
import bms.exceptions.FireDrillException;

public interface FireDrill {
    void fireDrill(RoomType roomType) throws FireDrillException;
}

package bms.floor;

import bms.exceptions.DuplicateRoomException;
import bms.room.Room;
import bms.room.RoomType;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class FloorTest {
    private Floor floor;

    @Before
    public void setFloor() throws Exception{
        floor = new Floor(1, 10.5, 11);
    }

    @After
    public void tearDown() throws Exception{
        floor = null;
    }

    @Test
    public void getFloorNumber(){
        assertEquals(1, floor.getFloorNumber());
    }

    @Test
    public void getMinWidth(){
        assertEquals(5, Floor.getMinWidth(), 0.01);
    }

    @Test
    public void getMinLength(){
        assertEquals(5, Floor.getMinLength(), 0.01);
    }

    @Test
    public void getRooms(){
        assertEquals(new ArrayList<Room>(), floor.getRooms());
    }

    @Test
    public void getWidth(){
        assertEquals(10.5, floor.getWidth(), 0.01);
    }

    @Test
    public void getLength(){
        assertEquals(11, floor.getLength(), 0.01);
    }

    @Test
    public void getRoomByNumber(){
        assertEquals(null, floor.getRoomByNumber(5));
        Room room1 = new Room(4, RoomType.OFFICE, 6);
        try{floor.addRoom(room1);
        }catch (Exception e){}
        assertEquals(null, floor.getRoomByNumber(5));
        assertEquals(room1, floor.getRoomByNumber(4));

    }

    @Test
    public void calculateArea(){
        assertEquals(115.5, floor.calculateArea(), 0.01);
    }
}

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
    private Room room1;
    private Room room2;
    private Room room3;

    @Before
    public void setFloor() throws Exception{
        floor = new Floor(1, 10.5, 11);
        room1 = new Room(1, RoomType.STUDY, 50);
        room2 = new Room(2,RoomType.OFFICE, 40);
        room3 = new Room(3,RoomType.LABORATORY, 10);
    }

    @After
    public void tearDown() throws Exception{
        floor = null;
        room1 = null;
        room2 = null;
        room3 = null;
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
        ArrayList<Room> rooms = new ArrayList<Room>();
        rooms.add(room1);
        rooms.add(room2);
        rooms.add(room3);
        try{
            floor.addRoom(room1);
            floor.addRoom(room2);
            floor.addRoom(room3);
            assertEquals(rooms, floor.getRooms());
        }catch (Exception e){}
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
        try{
            floor.addRoom(room1);
        }catch (Exception e){
        }
        assertEquals(null, floor.getRoomByNumber(5));
        assertEquals(room1, floor.getRoomByNumber(1));

    }

    @Test
    public void calculateArea(){
        assertEquals(115.5, floor.calculateArea(), 0.01);
    }

    @Test
    public void occupiedArea(){
        try{
            floor.addRoom(room1);
            floor.addRoom(room2);
            floor.addRoom(room3);
        }catch (Exception e){}
        assertEquals(100, floor.occupiedArea(), 0.01);
    }

    @Test
    public void addRoom(){
        try{
            floor.addRoom(room1);
            floor.addRoom(room2);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void addIllegalRoom(){
        Room illegalRoom = new Room(3, RoomType.OFFICE, 4);
        try{
            floor.addRoom(illegalRoom);
        }catch (Exception e){
            if(e.getClass().getName()!= "java.lang.IllegalArgumentException"){
                fail();
            }
        }
    }

    @Test
    public void addDuplicateRoom(){
        Room duplicateRoom = new Room(2, RoomType.OFFICE, 16);
        try{
            floor.addRoom(room2);
            floor.addRoom(duplicateRoom);
        }catch (Exception e){
            if(e.getClass().getName()!= "bms.exceptions.DuplicateRoomException"){
                fail();
            }
        }
    }

    @Test
    public void addRoomNoSpace(){
        Room roomBiggerThanLimit = new Room(4, RoomType.OFFICE, 16);
        try{
            floor.addRoom(room1);
            floor.addRoom(room2);
            floor.addRoom(room3);
            floor.addRoom(roomBiggerThanLimit);
        }catch (Exception e){
            if(e.getClass().getName()!= "bms.exceptions.InsufficientSpaceException"){
                fail();
            }
        }
    }

    @Test
    public void fireDrillNull(){
        try{
            floor.addRoom(room1);
            floor.addRoom(room2);
            floor.addRoom(room3);
        }catch (Exception e){
            fail();
        }
        floor.fireDrill(null);
    }

    @Test
    public void fireDrill(){
        try{
            floor.addRoom(room1);
            floor.addRoom(room2);
            floor.addRoom(room3);
        }catch (Exception e){
            fail();
        }
        floor.fireDrill(RoomType.OFFICE);
        floor.fireDrill(RoomType.STUDY);
    }

    @Test
    public void cancelFireDrill(){
        try{
            floor.addRoom(room1);
            floor.addRoom(room2);
            floor.addRoom(room3);
        }catch (Exception e){
            fail();
        }
        floor.fireDrill(RoomType.STUDY);
        floor.cancelFireDrill();
    }

    @Test
    public void toStringTest(){
        try{
            floor.addRoom(room1);
            floor.addRoom(room2);
            floor.addRoom(room3);
        }catch (Exception e){
            fail();
        }
        assertEquals("Floor #1: width=10.50m, length=11.00m, rooms=3",
                floor.toString());
    }
}
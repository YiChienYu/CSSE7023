package bms.floor;

import bms.room.Room;
import bms.room.RoomType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FloorTest {
    private Floor floor;

    @Before
    public void setFloor() throws Exception{
        floor = new Floor(1, 10.5, 11);
    }

    @After
    public void tearDown() throws Exception{}

    @Test
    public void getFloorNumber(){

    }

    @Test
    public void getMinWidth(){

    }

    @Test
    public void getMinLength(){

    }

    @Test
    public void getRooms(){

    }

    @Test
    public void getWidth(){

    }

    @Test
    public void getLength(){

    }

    @Test
    public void getRoomByNumber(){
        Assert.assertEquals(null, floor.getRoomByNumber(5));
        Room r = new Room(4, RoomType.OFFICE, 6);
        try{floor.addRoom(r);
        }catch (Exception e){}
        Assert.assertEquals(null, floor.getRoomByNumber(5));
        Assert.assertEquals(r, floor.getRoomByNumber(4));
    }

    @Test
    public void calculateArea(){

    }
}
